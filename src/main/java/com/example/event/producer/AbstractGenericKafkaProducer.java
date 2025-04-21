package com.example.event.producer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import com.example.event.BaseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractGenericKafkaProducer<T> implements KafkaProducer<T> {

	private static final int MAX_KAFKA_MESSAGE_SIZE = 950_000;
	private static final String TOO_LARGE_MESSAGE_ERROR = "Message too large for topic {}, payload {}";

	protected final KafkaTemplate<String, BaseEvent<T>> kafkaTemplate;
	protected final ObjectMapper objectMapper = new ObjectMapper();
	protected final String topic;
	protected final Class<T> eventTypeClass;

	protected AbstractGenericKafkaProducer(KafkaTemplate<String, BaseEvent<T>> kafkaTemplate, String topic,
			Class<T> eventTypeClass) {
		this.kafkaTemplate = kafkaTemplate;
		this.topic = topic;
		this.eventTypeClass = eventTypeClass;
	}

	@Override
	public void send(BaseEvent<T> baseEvent) {
		send(baseEvent, null);
	}

	@Override
	public void send(BaseEvent<T> baseEvent, String key) {
		if (isWithinKafkaSizeLimit(baseEvent.getData())) {
			sendMessage(baseEvent, key);
		} else {
			log.error(TOO_LARGE_MESSAGE_ERROR, topic, baseEvent);
		}
	}

	@Override
	public void sendInBatch(BaseEvent<T> baseEvent, int batchSize) {
		sendInBatch(baseEvent, batchSize, null);
	}

	@Override
	public void sendInBatch(BaseEvent<T> baseEvent, int batchSize, String key) {
		List<List<T>> batches = partition(baseEvent.getData(), batchSize);
		for (List<T> batch : batches) {
			BaseEvent<T> batchEvent = new BaseEvent<>();
			batchEvent.setEventType(baseEvent.getEventType());
			batchEvent.setTimestamp(baseEvent.getTimestamp());
			batchEvent.setData(batch);
			if (isWithinKafkaSizeLimit(batch)) {
				sendMessage(batchEvent, key);
			} else {
				log.error(TOO_LARGE_MESSAGE_ERROR, topic, batchEvent);
			}
		}
	}

	private void sendMessage(BaseEvent<T> baseEvent, String key) {
		Message<BaseEvent<T>> message = MessageBuilder.withPayload(baseEvent).setHeader(KafkaHeaders.TOPIC, topic)
				.setHeader(KafkaHeaders.KEY, key).build();
		kafkaTemplate.send(message).whenComplete((result, ex) -> {
			if (ex == null) {
				log.info("Message sent successfully to topic {} with offset {}, payload {}", topic,
						result.getRecordMetadata().offset(), baseEvent);
			} else {
				log.error("Failed to send message to topic {}", topic, ex);
			}
		});
	}

	private boolean isWithinKafkaSizeLimit(Object payload) {
		try {
			byte[] jsonBytes = objectMapper.writeValueAsBytes(payload);
			return jsonBytes.length <= MAX_KAFKA_MESSAGE_SIZE;
		} catch (Exception e) {
			log.error("Failed to serialize event for size check", e);
			return false;
		}
	}

	private <X> List<List<X>> partition(List<X> list, int size) {
		List<List<X>> parts = new ArrayList<>();
		for (int i = 0; i < list.size(); i += size) {
			parts.add(new ArrayList<>(list.subList(i, Math.min(i + size, list.size()))));
		}
		return parts;
	}

	public Class<?> getEventTypeClass() {
		return this.eventTypeClass;
	}
}

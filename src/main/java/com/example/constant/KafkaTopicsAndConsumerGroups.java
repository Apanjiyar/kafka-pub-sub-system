package com.example.constant;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class KafkaTopicsAndConsumerGroups {
	
	public static final String ORDER_EVENTS_TOPIC = "order-events";
	public static final String ORDER_EVENTS_GROUP_ID = "order-events";
	
	public static final String PAYMENT_EVENTS_TOPIC = "payment-events";
	public static final String PAYMENT_EVENTS_GROUP_ID = "payment-events";

}

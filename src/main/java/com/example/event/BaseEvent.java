package com.example.event;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class BaseEvent<T> {
	private String timestamp;
    private String eventType;
    private String source;
    private List<T> data;
    
    public static <T> BaseEvent<T> of(List<T> data, String eventType, String source) {
    	return BaseEvent.<T>builder()
				.timestamp(String.valueOf(System.currentTimeMillis()))
				.eventType(eventType)
				.source(source)
				.data(data)
				.build();
    }
}

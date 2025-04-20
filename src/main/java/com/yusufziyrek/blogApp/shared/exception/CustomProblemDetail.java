package com.yusufziyrek.blogApp.shared.exception;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomProblemDetail {

	private int status;

	private String message;

	private String path;

	private String machine;

	private LocalDateTime timeStamp;

	private Map<String, String> validationErrors;

	public CustomProblemDetail(int status, String message, String path, String machine, LocalDateTime timeStamp) {
		this.status = status;
		this.message = message;
		this.path = path;
		this.machine = machine;
		this.timeStamp = timeStamp;
		this.validationErrors = null;
	}

}

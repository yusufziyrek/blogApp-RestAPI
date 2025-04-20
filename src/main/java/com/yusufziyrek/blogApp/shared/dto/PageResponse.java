package com.yusufziyrek.blogApp.shared.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {
	private List<T> items;
	private int page;
	private int size;
	private long totalItems;
	private int totalPages;
}

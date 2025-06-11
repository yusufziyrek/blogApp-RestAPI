package com.yusufziyrek.blogApp.identity.dto.responses;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetByIdUserResponse {

	private Long id;
	private String username;
	private String firstname;
	private String lastname;
	private String department;
	private int age;
	private List<String> titles;

}

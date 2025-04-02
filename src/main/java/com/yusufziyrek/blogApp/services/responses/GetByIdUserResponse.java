package com.yusufziyrek.blogApp.services.responses;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetByIdUserResponse {

	private Long id;
	private String userName;
	private String firstName;
	private String lastName;
	private String department;
	private int age;
	private List<String> titles;

}

package com.yusufziyrek.blogApp.services.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllUsersResponse {

	private Long id;
	private String userName;
	private String department;
	private int age;

}

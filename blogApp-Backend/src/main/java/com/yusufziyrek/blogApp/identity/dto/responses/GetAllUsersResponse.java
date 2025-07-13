package com.yusufziyrek.blogApp.identity.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetAllUsersResponse {

	private Long id;
	private String firstname;
	private String lastname;
	private String username;
	private String email;
	private String department;
	private int age;

}

package com.yusufziyrek.blogApp.identity.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetAllUsersResponse {

	private Long id;
	private String username;
	private String department;
	private int age;

}

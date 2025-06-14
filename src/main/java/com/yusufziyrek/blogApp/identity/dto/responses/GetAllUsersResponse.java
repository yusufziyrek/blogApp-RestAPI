package com.yusufziyrek.blogApp.identity.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetAllUsersResponse {

	private Long id;
	private String username;
	private String department;
	private int age;

}

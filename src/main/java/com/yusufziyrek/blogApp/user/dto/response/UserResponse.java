package com.yusufziyrek.blogApp.user.dto.response;

import com.yusufziyrek.blogApp.user.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    
    private Long id;
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private String department;
    private Integer age;
    private Role role;
    private boolean enabled;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    
    public String getFullName() {
        return (firstname != null ? firstname : "") + " " + (lastname != null ? lastname : "");
    }
}

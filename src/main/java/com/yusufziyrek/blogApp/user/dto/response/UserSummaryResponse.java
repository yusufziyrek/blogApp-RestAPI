package com.yusufziyrek.blogApp.user.dto.response;

import com.yusufziyrek.blogApp.user.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSummaryResponse {
    
    private Long id;
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private Role role;
    
    public String getFullName() {
        return (firstname != null ? firstname : "") + " " + (lastname != null ? lastname : "");
    }
}

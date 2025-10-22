package com.yusufziyrek.blogApp.user.domain;

import java.time.LocalDateTime;

/**
 * Domain model (framework bağımsız). İş kuralları burada.
 */
public class UserDomain {

    private Long id;
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private String password;
    private String department;
    private int age;
    private Role role;
    private boolean enabled;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    // Yeni kullanıcı için constructor
    public UserDomain(String firstname, String lastname, String username, 
                     String email, String password, String department, 
                     int age, Role role) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.email = email;
        this.password = password;
        this.department = department;
        this.age = age;
        this.role = role;
        this.enabled = true;
        this.createdDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
    }

    // Kalıcı veriden gelen kullanıcı için constructor
    public UserDomain(Long id, String firstname, String lastname, String username,
                     String email, String password, String department, int age,
                     Role role, boolean enabled, LocalDateTime createdDate,
                     LocalDateTime updatedDate) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.email = email;
        this.password = password;
        this.department = department;
        this.age = age;
        this.role = role;
        this.enabled = enabled;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    // Varsayılan constructor
    public UserDomain() {}

    // Domain iş metotları
    public void activate() {
        this.enabled = true;
        this.updatedDate = LocalDateTime.now();
    }

    public void deactivate() {
        this.enabled = false;
        this.updatedDate = LocalDateTime.now();
    }

    public boolean canUpdateProfile(Long requestUserId) {
        return this.id != null && this.id.equals(requestUserId);
    }

    public void updateProfile(String firstname, String lastname, String email, 
                            String department, int age) {
        if (firstname != null && !firstname.trim().isEmpty()) {
            this.firstname = firstname;
        }
        if (lastname != null && !lastname.trim().isEmpty()) {
            this.lastname = lastname;
        }
        if (email != null && !email.trim().isEmpty()) {
            this.email = email;
        }
        if (department != null) {
            this.department = department;
        }
        if (age > 0) {
            this.age = age;
        }
        this.updatedDate = LocalDateTime.now();
    }

    public String getFullName() {
        return (firstname != null ? firstname : "") + " " + 
               (lastname != null ? lastname : "");
    }

    public boolean isAdministrator() {
        return role == Role.ADMIN;
    }

    public boolean isRegularUser() {
        return role == Role.USER;
    }

    // Domain doğrulama metotları
    public boolean hasValidEmail() {
        return email != null && email.contains("@") && email.length() <= 320;
    }

    public boolean hasValidUsername() {
        return username != null && username.length() >= 3 && username.length() <= 64;
    }

    public boolean hasValidAge() {
        return age >= 18 && age <= 100;
    }

    // Getter'lar
    public Long getId() { return id; }
    public String getFirstname() { return firstname; }
    public String getLastname() { return lastname; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getDepartment() { return department; }
    public int getAge() { return age; }
    public Role getRole() { return role; }
    public boolean isEnabled() { return enabled; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public LocalDateTime getUpdatedDate() { return updatedDate; }

    // Setter'lar (iç kullanım)
    public void setId(Long id) { this.id = id; }
    public void setPassword(String password) { 
        this.password = password;
        this.updatedDate = LocalDateTime.now();
    }

    public void setEmail(String email) {
        this.email = email;
        this.updatedDate = LocalDateTime.now();
    }
}

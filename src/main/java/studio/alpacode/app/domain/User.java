package studio.alpacode.app.domain;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public class User {

    @Id
    private Long id;

    private String email;

    private String password;

    private String name;

    private String companyName;

    private Role role;

    private Status status;

    private String inviteToken;

    private LocalDateTime inviteTokenExpiry;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public User() {
    }

    public User(String email, String name, Role role, Status status) {
        this.email = email;
        this.name = name;
        this.role = role;
        this.status = status;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getInviteToken() {
        return inviteToken;
    }

    public void setInviteToken(String inviteToken) {
        this.inviteToken = inviteToken;
    }

    public LocalDateTime getInviteTokenExpiry() {
        return inviteTokenExpiry;
    }

    public void setInviteTokenExpiry(LocalDateTime inviteTokenExpiry) {
        this.inviteTokenExpiry = inviteTokenExpiry;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isActive() {
        return this.status == Status.ACTIVE;
    }

    public boolean isPending() {
        return this.status == Status.PENDING;
    }

    public boolean isAdmin() {
        return this.role == Role.ADMIN;
    }

    public boolean isClient() {
        return this.role == Role.CLIENT;
    }

    public boolean isInviteTokenValid() {
        return this.inviteToken != null
            && this.inviteTokenExpiry != null
            && this.inviteTokenExpiry.isAfter(LocalDateTime.now());
    }
}

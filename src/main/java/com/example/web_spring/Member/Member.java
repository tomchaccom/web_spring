package com.example.web_spring.Member;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String email;
    private String phone;
    private Role role; // 권한 (User, Admin 등)

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public static Member createMember(String username, String password, String email, String phone, Role role) {
        Member member = new Member();
        member.username = username;
        member.password = password;
        member.email = email;
        member.phone = phone;
        member.role = role; // 기본값을 ROLE_USER로 할당
        member.createdAt = LocalDateTime.now();
        return member;
    }
    protected void changePassword(String password) {
        this.password = password;
    }
    protected void changeUserInfo(String email, String phone) {
        this.email = email;
        this.phone = phone;
    }
}
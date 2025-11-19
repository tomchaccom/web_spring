package com.example.web_spring.Member.Dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberJoinDto {
    private String username;
    private String password;
    private String email;
    private String phone;

}

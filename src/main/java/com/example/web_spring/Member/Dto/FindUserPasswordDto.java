package com.example.web_spring.Member.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindUserPasswordDto {

    private String username;
    private String email;
    private String phone;
    private String newPassword;
}

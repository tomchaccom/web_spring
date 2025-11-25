package com.example.web_spring.Member.Dto;

import com.example.web_spring.Member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class GetUserInfoDto {

    private String username;
    private String email;
    private String phone;

    public GetUserInfoDto(Member member) {
        this.username = member.getUsername();
        this.email = member.getEmail();
        this.phone = member.getPhone();
    }
}

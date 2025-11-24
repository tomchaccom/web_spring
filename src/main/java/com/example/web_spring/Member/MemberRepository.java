package com.example.web_spring.Member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);

    Optional<Member> findByEmailAndPhone(String email, String phone);
    Optional<Member> findByUsernameAndEmail(String username, String email);
    Optional<Member> findByUsernameAndPhone(String username, String phone);

}

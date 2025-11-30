package com.example.web_spring.Inquiry;

import com.example.web_spring.Member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    List<Inquiry> findByMemberOrderByCreatedAtDesc(Member member);
    List<Inquiry> findAllByOrderByCreatedAtDesc();

}


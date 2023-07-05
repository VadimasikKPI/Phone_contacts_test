package com.example.contact.repository;

import com.example.contact.model.ContactEmail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactEmailRepository extends JpaRepository<ContactEmail, Long> {
    ContactEmail findContactEmailByEmail(String email);
}

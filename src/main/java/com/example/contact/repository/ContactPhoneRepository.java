package com.example.contact.repository;

import com.example.contact.model.ContactPhone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactPhoneRepository extends JpaRepository<ContactPhone, Long> {
    ContactPhone findContactPhoneByPhoneNumber(String phoneNumber);
}

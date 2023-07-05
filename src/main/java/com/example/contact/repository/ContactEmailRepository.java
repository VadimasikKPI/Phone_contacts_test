package com.example.contact.repository;

import com.example.contact.model.Contact;
import com.example.contact.model.ContactEmail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactEmailRepository extends JpaRepository<ContactEmail, Long> {
    ContactEmail findContactEmailByEmail(String email);
    ContactEmail findContactEmailByEmailAndContact(String email, Contact contact);
    List<ContactEmail> findContactEmailsByContact(Contact contact);

    void deleteAllEmailsByContact(Contact contact);
}

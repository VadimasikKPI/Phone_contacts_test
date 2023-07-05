package com.example.contact.repository;

import com.example.contact.model.Contact;
import com.example.contact.model.ContactPhone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactPhoneRepository extends JpaRepository<ContactPhone, Long> {
    ContactPhone findContactPhoneByPhoneNumber(String phoneNumber);

    ContactPhone findContactPhoneByPhoneNumberAndContact(String phoneNumber, Contact contact);

    List<ContactPhone> findContactPhonesByContact(Contact contact);

    void deleteAllPhonesByContact(Contact contact);
}

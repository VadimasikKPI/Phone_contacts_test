package com.example.contact.repository;

import com.example.contact.model.Contact;
import com.example.contact.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    Contact findContactByContactName(String contactName);
    Contact findContactByContactNameAndUser(String contactName, User user);


}

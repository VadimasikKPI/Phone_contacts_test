package com.example.contact.service;

import com.example.contact.dto.ContactDTO;
import com.example.contact.dto.ContactEmailDTO;
import com.example.contact.dto.ContactPhoneDTO;
import com.example.contact.model.Contact;
import com.example.contact.model.ContactEmail;
import com.example.contact.model.ContactPhone;
import com.example.contact.model.User;
import com.example.contact.repository.ContactEmailRepository;
import com.example.contact.repository.ContactPhoneRepository;
import com.example.contact.repository.ContactRepository;
import com.example.contact.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ContactService {
    private final ContactRepository contactRepository;
    private final ContactEmailRepository contactEmailRepository;
    private final ContactPhoneRepository contactPhoneRepository;
    private final UserRepository userRepository;

    public Contact createContact(String token, ContactDTO contactDTO, ContactEmailDTO contactEmailDTO, ContactPhoneDTO contactPhoneDTO){
        User user = userRepository.findUserByToken(token);
        Contact newContact = new Contact(contactDTO.getContactName(), user);
        ContactEmail contactEmail = new ContactEmail(contactEmailDTO.getEmail(), newContact);
        ContactPhone contactPhone = new ContactPhone(contactPhoneDTO.getPhone(), newContact);
        contactEmailRepository.save(contactEmail);
        contactPhoneRepository.save(contactPhone);
        return contactRepository.save(newContact);
    }
}

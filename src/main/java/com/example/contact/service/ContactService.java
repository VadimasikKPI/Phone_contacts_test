package com.example.contact.service;

import com.example.contact.dto.ContactDTO;
import com.example.contact.dto.ContactEmailDTO;
import com.example.contact.dto.ContactPhoneDTO;
import com.example.contact.dto.UserDTO;
import com.example.contact.exception.UniqueException;
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

import java.util.*;

@RequiredArgsConstructor
@Service
public class ContactService {
    private final ContactRepository contactRepository;
    private final ContactEmailRepository contactEmailRepository;
    private final ContactPhoneRepository contactPhoneRepository;
    private final UserRepository userRepository;

    public String createContact(String token, ContactDTO contactDTO){
        User user = userRepository.findUserByToken(token);
        if(contactRepository.findContactByContactName(contactDTO.getContactName())==null){
            Contact newContact = new Contact(contactDTO.getContactName(), user);
            contactRepository.save(newContact);
            for(ContactEmailDTO c: contactDTO.getEmails()){
                if(contactEmailRepository.findContactEmailByEmailAndContact(c.getEmail(), newContact)==null){
                    ContactEmail contactEmail = new ContactEmail(c.getEmail(), newContact);
                    contactEmailRepository.save(contactEmail);
                }
                else{
                    return c.getEmail()+ " in contact " + newContact.getContactName() + " already exist)";
                }

            }

            for(ContactPhoneDTO p: contactDTO.getPhones()){
                if(contactPhoneRepository.findContactPhoneByPhoneNumberAndContact(p.getPhone(), newContact)==null){
                    ContactPhone contactPhone = new ContactPhone(p.getPhone(), newContact);
                    contactPhoneRepository.save(contactPhone);
                }
                else{
                    return p.getPhone()+ " in contact " + newContact.getContactName() + " already exist)";
                }
            }
            return "Contact created";
        }
        else{
            return "Contact already exist";
        }
    }
    public ContactDTO getContact(String token,ContactDTO contactDTO){
        User user = userRepository.findUserByToken(token);
        if(user==null){
            return new ContactDTO();
        }
        Contact contact = contactRepository.findContactByContactNameAndUser(contactDTO.getContactName(), user);
        List<ContactEmail> emails = contactEmailRepository.findContactEmailsByContact(contact);
        List<ContactPhone> phones = contactPhoneRepository.findContactPhonesByContact(contact);
        List<ContactEmailDTO> emailDTOS = new ArrayList<>();
        List<ContactPhoneDTO> phoneDTOS = new ArrayList<>();
        for(ContactEmail e: emails){
            emailDTOS.add(new ContactEmailDTO(e.getEmail()));
        }
        for(ContactPhone p: phones){
            phoneDTOS.add(new ContactPhoneDTO(p.getPhoneNumber()));
        }
        return new ContactDTO(contact.getContactName(), emailDTOS, phoneDTOS);

    }

    public List<ContactDTO> getContacts(String token) {
        User user = userRepository.findUserByToken(token);
        if(user==null){
            return Collections.emptyList();
        }
        List<Contact> contacts = contactRepository.findAll();
        List<ContactDTO> contactDTOS = new ArrayList<>();
        for(Contact c: contacts){
            List<ContactEmail> emails = contactEmailRepository.findContactEmailsByContact(c);
            List<ContactPhone> phones = contactPhoneRepository.findContactPhonesByContact(c);
            List<ContactEmailDTO> emailDTOS = new ArrayList<>();
            List<ContactPhoneDTO> phoneDTOS = new ArrayList<>();
            for(ContactEmail e: emails){
                emailDTOS.add(new ContactEmailDTO(e.getEmail()));
            }
            for(ContactPhone p: phones){
                phoneDTOS.add(new ContactPhoneDTO(p.getPhoneNumber()));
            }
            contactDTOS.add(new ContactDTO(c.getContactName(), emailDTOS, phoneDTOS));
        }
        return contactDTOS;
    }
}

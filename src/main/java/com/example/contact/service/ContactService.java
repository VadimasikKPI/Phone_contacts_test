package com.example.contact.service;

import com.example.contact.dto.ContactDTO;
import com.example.contact.dto.ContactEmailDTO;
import com.example.contact.dto.ContactPhoneDTO;
import com.example.contact.exception.UniqueException;
import com.example.contact.exception.NotFoundException;
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

import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
@Service
public class ContactService {
    private final ContactRepository contactRepository;
    private final ContactEmailRepository contactEmailRepository;
    private final ContactPhoneRepository contactPhoneRepository;
    private final UserRepository userRepository;

    private final String PHONE_API_KEY = "989e2473d6f0cf6f1a432465db32d815";

    private final String EMAIL_API_KEY = "6abddb14f73e41b5bd2a5e554f324919";

    public String createContact(String token, ContactDTO contactDTO){
        try {
            User user = userRepository.findUserByToken(token);
            if(user == null){
                throw new NotFoundException("User not found");
            }
            Contact newContact = new Contact();
            ContactEmail contactEmail = new ContactEmail();
            ContactPhone contactPhone = new ContactPhone();
            if(contactRepository.findContactByContactNameAndUser(contactDTO.getContactName(), user)==null){
                newContact.setContactName(contactDTO.getContactName());
                newContact.setUser(user);
                contactRepository.save(newContact);
                for(ContactEmailDTO c: contactDTO.getEmails()){

                    if(contactEmailRepository.findContactEmailByEmailAndContact(c.getEmail(), newContact)==null && EmailAddressValidator.isEmailValid(c.getEmail(), EMAIL_API_KEY)){
                        contactEmail.setEmail(c.getEmail());
                        contactEmail.setContact(newContact);
                    }
                    else{
                        contactRepository.delete(newContact);
                        throw new UniqueException("Contact with email - " + c.getEmail() + " already defined or provide valid email");
                    }

                }

                for(ContactPhoneDTO p: contactDTO.getPhones()){
                    if(contactPhoneRepository.findContactPhoneByPhoneNumberAndContact(p.getPhone(), newContact)==null && PhoneNumberValidator.isPhoneNumberValid(p.getPhone(), PHONE_API_KEY)){
                        contactPhone.setPhoneNumber(p.getPhone());
                        contactPhone.setContact(newContact);
                    }
                    else{
                        contactRepository.delete(newContact);
                        throw new UniqueException("Contact with phone - " + p.getPhone() + " already defined or provide valid phone number");
                    }
                }

                contactEmailRepository.save(contactEmail);
                contactPhoneRepository.save(contactPhone);
                return "Contact created";
            }
            else{
                return "Contact already exist";
            }
        }
        catch (NotFoundException notFoundException){

            return notFoundException.getMessage();

        } catch (UniqueException uniqueException){

            return uniqueException.getMessage();
        }
        catch (IOException e) {
            return "Phone number not valid";
        }

    }
    public ContactDTO getContact(String token,ContactDTO contactDTO){
        try{
            User user = userRepository.findUserByToken(token);
            if(user==null){
                throw  new NotFoundException("User not found");
            }
            Contact contact = contactRepository.findContactByContactNameAndUser(contactDTO.getContactName(), user);
            if(contact==null){
                throw  new NotFoundException("Contact not found");
            }
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
        catch (NotFoundException notFoundException){
            return new ContactDTO();

        }

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

    public String updateContact(String token, ContactDTO contactDTO){
        try{
            User user = userRepository.findUserByToken(token);
            if(user==null){
                throw new NotFoundException("User not found");
            }
            Contact oldContact = contactRepository.findContactByContactNameAndUser(contactDTO.getContactName(), user);
            if(oldContact==null){
                return "Contact does not exist";
            }
            List<ContactEmail> contactEmails = new ArrayList<>();
            List<ContactPhone> contactPhones = new ArrayList<>();
            for(ContactEmailDTO c: contactDTO.getEmails()){
                if(EmailAddressValidator.isEmailValid(c.getEmail(), EMAIL_API_KEY)){
                    contactEmails.add(new ContactEmail(c.getEmail(), oldContact));
                }
                else{
                    throw new UniqueException("Email address not valid");
                }
            }
            for(ContactPhoneDTO p: contactDTO.getPhones()){
                if(PhoneNumberValidator.isPhoneNumberValid(p.getPhone(), PHONE_API_KEY)){
                    contactPhones.add(new ContactPhone(p.getPhone(), oldContact));
                }
                else{
                    throw new UniqueException("Phone number not valid");
                }
            }
            oldContact.setEmails(contactEmails);
            oldContact.setPhones(contactPhones);
            return "Contact updated";

        }
        catch (NotFoundException notFoundException){
            return notFoundException.getMessage();
        }
        catch (UniqueException uniqueException){
            return uniqueException.getMessage();
        }
        catch (IOException e) {
            return "Phone number not valid";
        }
    }

    public String deleteContact(String token, ContactDTO contactDTO){
        try{
            User user = userRepository.findUserByToken(token);
            if(user == null){
                throw new NotFoundException("User not found");
            }
            Contact oldContact = contactRepository.findContactByContactNameAndUser(contactDTO.getContactName(), user);
            if(oldContact==null){
                return "Contact does not exist";
            }
            contactRepository.delete(oldContact);
            contactEmailRepository.deleteAllEmailsByContact(oldContact);
            contactPhoneRepository.deleteAllPhonesByContact(oldContact);
            return "Contact deleted";
        }
        catch (NotFoundException notFoundException){
            return notFoundException.getMessage();
        }

    }
}

package com.example.contact.dto;

import com.example.contact.model.ContactEmail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContactDTO {
    private String contactName;

    private List<ContactEmailDTO> emails;

    private List<ContactPhoneDTO> phones;

    public List<ContactEmailDTO> getEmails() {
        return emails;
    }

    public List<ContactPhoneDTO> getPhones() {
        return phones;
    }
}

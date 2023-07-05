package com.example.contact.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class ContactPhone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long phoneId;


    private String phoneNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_id")
    private Contact contact;

    public ContactPhone(String phoneNumber, Contact contact) {
        this.phoneNumber = phoneNumber;
        this.contact = contact;
    }
}

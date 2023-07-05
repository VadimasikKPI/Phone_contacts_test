package com.example.contact.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contactId;



    private String contactName;

    @OneToMany(mappedBy = "contact", cascade = CascadeType.ALL)
    private List<ContactEmail> emails;

    @OneToMany(mappedBy = "contact", cascade = CascadeType.ALL)
    private List<ContactPhone> phones;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Contact(String contactName, User user) {
        this.contactName = contactName;
        this.user = user;
    }
}

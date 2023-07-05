package com.example.contact.controller;

import com.example.contact.dto.ContactDTO;
import com.example.contact.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ContactController {
    private final ContactService contactService;

    @PostMapping("/contacts")
    public ResponseEntity<?> createContact(@RequestParam String token, @RequestBody ContactDTO contactDTO){
        return ResponseEntity.ok(contactService.createContact(token, contactDTO));
    }

    @GetMapping("/contact")
    public ResponseEntity<?> getContacts(@RequestParam String token, @RequestBody ContactDTO contactDTO){
        return ResponseEntity.ok(contactService.getContact(token, contactDTO));
    }

    @GetMapping("/contacts")
    public ResponseEntity<?> getAllContacts(@RequestParam String token){
        return ResponseEntity.ok(contactService.getContacts(token));
    }

    @PutMapping("/contacts")
    public ResponseEntity<?> updateContact(@RequestParam String token, @RequestBody ContactDTO contactDTO){
        return ResponseEntity.ok(contactService.updateContact(token, contactDTO));
    }

    @DeleteMapping("/contacts")
    public ResponseEntity<?> deleteContact(@RequestParam String token, @RequestBody ContactDTO contactDTO){
        return ResponseEntity.ok(contactService.deleteContact(token, contactDTO));
    }

}

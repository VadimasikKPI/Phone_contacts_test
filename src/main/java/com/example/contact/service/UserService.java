package com.example.contact.service;

import com.example.contact.dto.UserDTO;
import com.example.contact.model.User;
import com.example.contact.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public String registerUser(UserDTO user){
        if(userRepository.findUserByUsername(user.getUsername())==null){
            User newUser = new User(user.getUsername(), passwordEncoder.encode(user.getPassword()));
            userRepository.save(newUser);
            return newUser.getUsername();
        }
        return "User already exist";
    }
    public User updateUser(User user){
        return userRepository.save(user);
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    public User findUserByUsername(String username){
        User user = userRepository.findUserByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("User with this user name not found");
        }
        return user;
    }


}

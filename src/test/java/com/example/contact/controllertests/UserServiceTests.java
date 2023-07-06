package com.example.contact.controllertests;

import com.example.contact.dto.UserDTO;
import com.example.contact.exception.NotFoundException;
import com.example.contact.model.User;
import com.example.contact.repository.UserRepository;
import com.example.contact.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;



@SpringBootTest
public class UserServiceTests {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    public void registerUserTest() {
        UserDTO userDTO = new UserDTO("anton", "password");
        User newUser = new User("anton", "encodedPassword");

        when(userRepository.findUserByUsername(userDTO.getUsername())).thenReturn(null);
        when(passwordEncoder.encode(userDTO.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        String result = userService.registerUser(userDTO);

        assertEquals(newUser.getUsername(), result);
        verify(userRepository).findUserByUsername(userDTO.getUsername());
        verify(passwordEncoder).encode(userDTO.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void registerUserTestWithExistingUser() {
        UserDTO userDTO = new UserDTO("john", "password");
        User existingUser = new User("john", "encodedPassword");

        when(userRepository.findUserByUsername(userDTO.getUsername())).thenReturn(existingUser);

        String result = userService.registerUser(userDTO);

        assertEquals("User already exist", result);
        verify(userRepository).findUserByUsername(userDTO.getUsername());
        verifyNoMoreInteractions(passwordEncoder, userRepository);
    }

    @Test
    public void findUserByUsernameTest() {
        String username = "john";
        User existingUser = new User("john", "encodedPassword");

        when(userRepository.findUserByUsername(username)).thenReturn(existingUser);

        User result = userService.findUserByUsername(username);

        assertEquals(existingUser, result);
        verify(userRepository).findUserByUsername(username);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void findNotExistentUserByUsernameTest() {
        String username = "john";

        when(userRepository.findUserByUsername(username)).thenReturn(null);
        UsernameNotFoundException usernameNotFoundException = assertThrows(UsernameNotFoundException.class, ()->userService.findUserByUsername(username));
        assertEquals(usernameNotFoundException.getMessage(), "User with this user name not found");

    }
}

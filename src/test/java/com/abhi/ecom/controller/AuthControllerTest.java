package com.abhi.ecom.controller;

import com.abhi.ecom.dto.AuthenticationRequest;
import com.abhi.ecom.dto.SignupRequest;
import com.abhi.ecom.dto.UserDto;
import com.abhi.ecom.enums.UserRole;
import com.abhi.ecom.model.User;
import com.abhi.ecom.utils.JwtUtil;
import com.abhi.ecom.filters.JwtRequestFilter;
import com.abhi.ecom.repository.UserRepository;
import com.abhi.ecom.services.auth.AuthService;
import com.abhi.ecom.services.jwt.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtRequestFilter jwtRequestFilter;
    @MockBean
    JwtUtil jwtUtil;

    @MockBean
    private AuthService authService;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    UserDetailsServiceImpl userDetailsService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    UserDetails userDetails;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createAuthenticationToken() throws Exception {
        // Arrange
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("testuser", "password");
        User user = User.builder().id(1L).email("testuser").role(UserRole.CUSTOMER).build();
        String jwtToken = "jwt-token";

        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(), authenticationRequest.getPassword()))).thenReturn(null);
        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);
        when(userRepository.findFirstByEmail("testuser")).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken("testuser")).thenReturn(jwtToken);
        when(userDetails.getUsername()).thenReturn("testuser");

        MockHttpServletResponse response = mockMvc.perform(post("/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        String jsonResponse = response.getContentAsString();
        JSONObject jsonObject = new JSONObject(jsonResponse);
        assertEquals(1L, jsonObject.getLong("userId"));
        assertEquals("CUSTOMER", jsonObject.getString("role"));
        assertEquals("Bearer " + jwtToken, response.getHeader("Authorization"));
    }

    @Test
    void signUpUser() throws Exception {

        SignupRequest signupRequest = new SignupRequest("newuser@example.com","name", "password");
        UserDto userDto = UserDto.builder().email("newuser@example.com").build();

        when(authService.hasUserWithEmail("newuser@example.com")).thenReturn(false);
        when(authService.createUser(signupRequest)).thenReturn(userDto);

        mockMvc.perform(post("/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("newuser@example.com"));


    }
}
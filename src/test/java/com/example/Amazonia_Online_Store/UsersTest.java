package com.example.Amazonia_Online_Store;

import com.example.Amazonia_Online_Store.dto.userDetails.AddUserDetailsDTO;
import com.example.Amazonia_Online_Store.dto.userDetails.AllDataUserDetailsDTO;
import com.example.Amazonia_Online_Store.models.ERole;
import com.example.Amazonia_Online_Store.models.UserDetailsEntity;
import com.example.Amazonia_Online_Store.models.UserEntity;
import com.example.Amazonia_Online_Store.repositories.UserDetailsRepository;
import com.example.Amazonia_Online_Store.repositories.UserRepository;
import com.example.Amazonia_Online_Store.security.utils.JwtUtils;
import com.example.Amazonia_Online_Store.services.UserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UsersTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserDetailsRepository userDetailsRepository;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private UserDetailsService userDetailsService;

    private UserEntity mockUser;

    private UserDetailsEntity mockUserDetailsEntity;

    private AllDataUserDetailsDTO allDataUserDetailsDTO;

    private AllDataUserDetailsDTO dataUserCustomerDTO;

    @BeforeEach
    void setUp(){
        UserEntity mockUser = new UserEntity();
        mockUser.setId(6L);
        mockUser.setUsername("lion");
        mockUser.setPassword("$2a$10$chMKB4Zho/T5U6gpQld2Yu/EkHLu.IKpUnPgxobdr0fADQYHDYKWa");
        mockUser.setEmail("mock@test.com");
        mockUser.setRole(ERole.CUSTOMER);

        UserDetailsEntity mockDetails = new UserDetailsEntity();
        mockDetails.setId(10L);
        mockDetails.setName("test");
        mockDetails.setLastname("mock");
        mockDetails.setAddress("mockito");
        mockDetails.setPhone("12345612345");
        mockDetails.setUser(mockUser);

        allDataUserDetailsDTO = new AllDataUserDetailsDTO(mockUser, mockDetails);

    }



    @Test
    @WithMockUser
    void testGetUserData() throws Exception {
        when(userDetailsService.getData(any(HttpServletRequest.class))).thenReturn(allDataUserDetailsDTO);

        mvc.perform(MockMvcRequestBuilders.get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(6L))
                .andExpect(jsonPath("$.name").value("test"))
                .andExpect(jsonPath("$.email").value("mock@test.com"));

    }


    @Test
    @WithMockUser
    @DisplayName("Test - Registrar usuario")
    void addUser() throws Exception {
        AddUserDetailsDTO dataUserDetails = new AddUserDetailsDTO("lion","lion",
                "cliente@test.com","serch","Gutierritos","Monterrey","1234567895");

        String json = objectMapper.writeValueAsString(dataUserDetails);

        mvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @WithMockUser
    @ExtendWith(MockitoExtension.class)
    @DisplayName("Test - Consulta de datos de usuario")
    @MockitoSettings(strictness = Strictness.LENIENT)
    void getUserData() throws Exception{

        UserEntity mockUser = new UserEntity();
        mockUser.setId(6L);
        mockUser.setUsername("lion");
        mockUser.setPassword("$2a$10$chMKB4Zho/T5U6gpQld2Yu/EkHLu.IKpUnPgxobdr0fADQYHDYKWa");
        mockUser.setEmail("mock@test.com");
        mockUser.setRole(ERole.CUSTOMER);

        UserDetailsEntity mockDetails = new UserDetailsEntity();
        mockDetails.setId(10L);
        mockDetails.setName("test");
        mockDetails.setLastname("mock");
        mockDetails.setAddress("mockito");
        mockDetails.setPhone("12345612345");
        mockDetails.setUser(mockUser);

        AllDataUserDetailsDTO userDetailsDto = new AllDataUserDetailsDTO(mockUser, mockDetails);

        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        when(requestMock.getHeader("Authorization")).thenReturn("Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJsaW9uIiwiaWF0IjoxNzIyNTUxMTA3LCJleHAiOjE3MjI2Mzc1MDd9.vej1TMbfP7uNR_5unC3W-g3x-tWEyY6ezbp9BjUWR6-8N4FnafZUDHT42XbUXEXm");

        UserEntity userEntityMock = mock(UserEntity.class);
        when(userRepository.findByUsername("mock")).thenReturn(Optional.of(userEntityMock));

        when(jwtUtils.getUserFromRequest(requestMock)).thenReturn(mockUser);
        when(userDetailsService.getData(any(HttpServletRequest.class))).thenReturn(new AllDataUserDetailsDTO(mockUser, mockDetails));

        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "lion", roles = {"CUSTOMER"})
    @DisplayName("Test - Datos de usuario 2")
    void getUserData2() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("GET");
        request.setRequestURI("/users");

        when(userDetailsService.getData(request)).thenReturn(allDataUserDetailsDTO);

        String content = objectMapper.writeValueAsString(allDataUserDetailsDTO);

        mvc.perform(get("/users")
                .header("Authorization","Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJsaW9uIiwiaWF0IjoxNzIyNjE1MTkwLCJleHAiOjE3MjI3MDE1OTB9.e8kFYV5kwDIgB7pwGt8Bj5PmcpsBBLqhb1AOo0UA8Dl4z0WhfHKiDP7swt5S2H52"))
                .andExpect(status().isOk())
                .andReturn();
    }
}

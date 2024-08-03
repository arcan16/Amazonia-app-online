package com.example.Amazonia_Online_Store.models;

import com.example.Amazonia_Online_Store.dto.userDetails.AddUserDetailsDTO;
import com.example.Amazonia_Online_Store.dto.users.DataToUpdateUserDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity(name = "UserEntity")
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String email;

    @Enumerated(EnumType.STRING)
    private ERole role;


    public UserEntity(AddUserDetailsDTO addUserDetailsDTO, PasswordEncoder passwordEncoder) {
        this.username = addUserDetailsDTO.username();
        this.password = passwordEncoder.encode(addUserDetailsDTO.password());
        this.email = addUserDetailsDTO.email();
        this.role = ERole.CUSTOMER;
    }

    public UserEntity(long l, String test, String mail, ERole eRole) {
        this.id = l;
        this.username = test;
        this.email = mail;
        this.role = eRole;
    }

    public void update(DataToUpdateUserDTO data, PasswordEncoder passwordEncoder) {
        if(data.username() != null)
            this.username = data.username();
        if(data.password() != null)
            this.password = passwordEncoder.encode(data.password());
        if(data.email() != null)
            this.email = data.email();

    }
}

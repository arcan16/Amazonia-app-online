package com.example.Amazonia_Online_Store.models;

import com.example.Amazonia_Online_Store.dto.userDetails.AddUserDetailsDTO;
import com.example.Amazonia_Online_Store.dto.users.DataToUpdateUserDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "UserDetailsEntity")
@Table(name = "user_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String lastname;

    private String address;

    private String phone;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public UserDetailsEntity(UserEntity user, AddUserDetailsDTO addUserDetailsDTO) {
        this.name = addUserDetailsDTO.name();
        this.lastname = addUserDetailsDTO.lastname();
        this.address = addUserDetailsDTO.address();
        this.phone = addUserDetailsDTO.phone();
        this.user = user;
    }

    public UserDetailsEntity(String name, String lastname, String address, String number, UserEntity user) {
        this.name = name;
        this.lastname = lastname;
        this.address = address;
        this.phone = number;
        this.user = user;
    }

    public void update(DataToUpdateUserDTO data) {
        if(data.name() != null)
            this.name = data.name();
        if(data.lastname() != null)
            this.lastname = data.lastname();
        if(data.address() != null)
            this.address = data.address();
        if(data.phone() != null)
            this.phone = data.phone();
    }
}

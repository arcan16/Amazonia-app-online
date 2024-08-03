package com.example.Amazonia_Online_Store.dto.userDetails;

import com.example.Amazonia_Online_Store.models.UserDetailsEntity;
import com.example.Amazonia_Online_Store.models.ERole;
import com.example.Amazonia_Online_Store.models.UserEntity;
import jakarta.validation.constraints.Email;

public record AllDataUserDetailsDTO(Long userId,
                                    String username,
                                    @Email String email,
                                    ERole role,
                                    Long customerId,
                                    String name,
                                    String lastname,
                                    String address,
                                    String phone) {

    public AllDataUserDetailsDTO(UserEntity user, UserDetailsEntity customer) {
        this(user.getId(), user.getUsername(), user.getEmail(), user.getRole(),
                customer.getId(),customer.getName(), customer.getLastname(),
                customer.getAddress(), customer.getPhone());
    }

    public AllDataUserDetailsDTO(UserDetailsEntity userDetails) {
        this(userDetails.getUser().getId(), userDetails.getUser().getUsername(), userDetails.getUser().getEmail(),
                userDetails.getUser().getRole(), userDetails.getId(), userDetails.getName(),
                userDetails.getLastname(), userDetails.getAddress(), userDetails.getPhone());
    }

}

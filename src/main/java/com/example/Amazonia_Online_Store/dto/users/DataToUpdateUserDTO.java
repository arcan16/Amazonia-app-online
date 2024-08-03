package com.example.Amazonia_Online_Store.dto.users;

import com.example.Amazonia_Online_Store.models.ERole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record DataToUpdateUserDTO(Long id,
                                  String username,
                                  String password,
                                  @Email String email,
                                  String name,
                                  String lastname,
                                  String address,
                                  String phone) {
}

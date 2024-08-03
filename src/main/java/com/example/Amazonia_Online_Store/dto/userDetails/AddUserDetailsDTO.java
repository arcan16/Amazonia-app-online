package com.example.Amazonia_Online_Store.dto.userDetails;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record AddUserDetailsDTO(@NotNull String username,
                                @NotNull String password,
                                @NotNull @Email String email,
                                @NotNull String name,
                                @NotNull String lastname,
                                @NotNull String address,
                                @NotNull String phone) {
}

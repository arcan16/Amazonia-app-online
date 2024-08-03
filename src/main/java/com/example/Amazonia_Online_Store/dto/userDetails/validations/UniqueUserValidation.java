package com.example.Amazonia_Online_Store.dto.userDetails.validations;

import com.example.Amazonia_Online_Store.dto.userDetails.AddUserDetailsDTO;
import com.example.Amazonia_Online_Store.repositories.UserRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueUserValidation implements AddCustomerValidations{

    @Autowired
    private UserRepository userRepository;

    @Override
    public void validate(AddUserDetailsDTO addUserDetailsDTO) {
        if(userRepository.existsByUsername(addUserDetailsDTO.username())){
            throw new ValidationException("El usuario ya se encuentra registrado!");
        }
    }
}

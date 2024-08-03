package com.example.Amazonia_Online_Store.dto.userDetails.validations;

import com.example.Amazonia_Online_Store.dto.userDetails.AddUserDetailsDTO;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PhoneValidation implements AddCustomerValidations{

    @Override
    public void validate(AddUserDetailsDTO addUserDetailsDTO) {

        Pattern pattern = Pattern.compile("\\d{10}");

        Matcher matcher = pattern.matcher(addUserDetailsDTO.phone().trim());

        if(!matcher.find()){
            throw new ValidationException("El telefono solo debe contener numeros");
        }

        if(addUserDetailsDTO.phone().trim().length() != 10)
            throw new ValidationException("El telefono debe contener" +
                    " 10 numeros en lugar de: " + addUserDetailsDTO.phone().length());
    }

    public void validate(String phone){
        Pattern pattern = Pattern.compile("\\d{10}");

        Matcher matcher = pattern.matcher(phone.trim());

        if(!matcher.find()){
            throw new ValidationException("El telefono solo debe contener numeros");
        }

        if(phone.trim().length() != 10)
            throw new ValidationException("El telefono debe contener" +
                    " 10 numeros en lugar de: " + phone.length());
    }
}

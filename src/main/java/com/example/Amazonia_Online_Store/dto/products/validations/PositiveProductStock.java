package com.example.Amazonia_Online_Store.dto.products.validations;

import com.example.Amazonia_Online_Store.dto.products.DataProductDTO;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class PositiveProductStock implements AddProductValidation{

    @Override
    public void validate(DataProductDTO data) {
        if(data.stock() < 0)
            throw new ValidationException("El stock deberia ser positivo al registrar un nuevo producto");
    }
}

package com.example.Amazonia_Online_Store.dto.products.validations;

import com.example.Amazonia_Online_Store.dto.products.DataProductDTO;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class PositiveProductPrice implements AddProductValidation{

    @Override
    public void validate(DataProductDTO data) {
        if(data.price() <= 0)
            throw new ValidationException("El precio debe ser positivo");
    }
}

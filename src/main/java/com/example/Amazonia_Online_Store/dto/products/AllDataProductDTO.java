package com.example.Amazonia_Online_Store.dto.products;

import com.example.Amazonia_Online_Store.models.ProductEntity;
import jakarta.validation.constraints.NotNull;

public record AllDataProductDTO(Long id,
                                String name,
                                String description,
                                Double price,
                                Integer stock) {
    public AllDataProductDTO(ProductEntity data) {
        this(data.getId(), data.getName(), data.getDescription(), data.getPrice(), data.getStock());
    }
}

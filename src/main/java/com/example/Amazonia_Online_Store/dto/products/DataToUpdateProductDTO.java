package com.example.Amazonia_Online_Store.dto.products;

import jakarta.validation.constraints.NotNull;

public record DataToUpdateProductDTO(@NotNull Long id,
                                     String name,
                                     String description,
                                     Double price,
                                     Integer stock) {
}

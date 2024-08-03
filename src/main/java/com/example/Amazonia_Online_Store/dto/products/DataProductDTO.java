package com.example.Amazonia_Online_Store.dto.products;

import jakarta.validation.constraints.NotNull;

public record DataProductDTO(@NotNull String name,
                             @NotNull String description,
                             @NotNull Double price,
                             @NotNull Integer stock) {
}

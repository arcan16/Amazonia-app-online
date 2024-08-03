package com.example.Amazonia_Online_Store.dto.orders;

import com.example.Amazonia_Online_Store.models.ProductEntity;

public record ProductOrderDetailsDTO(ProductEntity product,
                                     Integer quantity,
                                     Double price,
                                     Integer stock) {
    public ProductOrderDetailsDTO(OrderDetailsDTO p, ProductEntity pl) {
        this(pl, p.quantity(), pl.getPrice(), pl.getStock());
    }
}

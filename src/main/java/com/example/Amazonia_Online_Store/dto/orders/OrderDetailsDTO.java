package com.example.Amazonia_Online_Store.dto.orders;

import com.example.Amazonia_Online_Store.models.OrderDetailEntity;
import com.example.Amazonia_Online_Store.models.ProductEntity;

public record OrderDetailsDTO(Long idProduct,
                              Integer quantity,
                              Double price) {
    public OrderDetailsDTO(OrderDetailsDTO p, ProductEntity pl) {
        this(p.idProduct, p.quantity, pl.getPrice());
    }

    public OrderDetailsDTO(OrderDetailEntity orderDetailEntity) {
        this(orderDetailEntity.getId(), orderDetailEntity.getQuantity(), orderDetailEntity.getPrice());
    }
}

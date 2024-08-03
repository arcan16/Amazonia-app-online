package com.example.Amazonia_Online_Store.dto.orders;

import com.example.Amazonia_Online_Store.models.OrderDetailEntity;

/**
 * DTO que desserializa l
 * @param productId
 * @param name
 * @param description
 * @param quantity
 * @param unitPrice
 */
public record AllDataOrderDetailsDTO (Long productId,
                                      String name,
                                      String description,
                                      Integer quantity,
                                      Double unitPrice){
    public AllDataOrderDetailsDTO(OrderDetailEntity order){
        this(order.getProduct().getId(), order.getProduct().getName(),
                order.getProduct().getDescription(), order.getQuantity(), order.getPrice());
    }
}

package com.example.Amazonia_Online_Store.dto.orders;

import com.example.Amazonia_Online_Store.models.EStatus;
import com.example.Amazonia_Online_Store.models.OrderEntity;

import java.time.LocalDateTime;
import java.util.List;

public record AllDataOrderDTO(Long id,
                              LocalDateTime date,
                              Double total,
                              EStatus status,
                              List<AllDataOrderDetailsDTO> orderDetailsList) {
    public AllDataOrderDTO(OrderEntity order){
        this(order.getId(), order.getDate(), order.getTotal(), order.getStatus(),
                order.getOrderDetails().stream().map(AllDataOrderDetailsDTO::new).toList());
    }
}

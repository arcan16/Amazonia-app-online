package com.example.Amazonia_Online_Store.models;

import com.example.Amazonia_Online_Store.dto.orders.OrderDetailsDTO;
import com.example.Amazonia_Online_Store.dto.orders.ProductOrderDetailsDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "OrderDetailsEntity")
@Table(name = "order_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    private Integer quantity;

    @Column(name = "unit_price")
    private Double price;

    public OrderDetailEntity(OrderEntity order, ProductOrderDetailsDTO pl) {
        this.order = order;
        this.product = pl.product();
        this.quantity = pl.quantity();
        this.price = pl.price();
    }
}

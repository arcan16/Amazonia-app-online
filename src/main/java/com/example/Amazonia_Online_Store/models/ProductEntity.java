package com.example.Amazonia_Online_Store.models;

import com.example.Amazonia_Online_Store.dto.products.DataProductDTO;
import com.example.Amazonia_Online_Store.dto.products.DataToUpdateProductDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "ProductEntity")
@Table(name = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private Double price;

    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "author")
    private UserEntity author;

    public ProductEntity(DataProductDTO dataProductDTO, UserEntity author) {
        this.name = dataProductDTO.name();
        this.description = dataProductDTO.description();
        this.price = dataProductDTO.price();
        this.stock = dataProductDTO.stock();
        this.author = author;
    }

    public ProductEntity(OrderDetailEntity order) {
        this.name = order.getProduct().getName();
        this.description = order.getProduct().getDescription();
        this.price = order.getProduct().getPrice();
        this.stock = order.getProduct().getStock();
        this.author = order.getProduct().getAuthor();
    }

    public ProductEntity(long l, String name, String description, double price, int stock) {
        this.id = l;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
    }

    public void update(DataToUpdateProductDTO data, UserEntity author) {
        if(data.name() != null)
            this.name = data.name();
        if(data.description() != null)
            this.description = data.description();
        if(data.price() != null && data.price() > 0)
            this.price = data.price();
        if(data.stock() != null && data.stock() > 0)
            this.stock = data.stock();
        this.author = author;
    }
}

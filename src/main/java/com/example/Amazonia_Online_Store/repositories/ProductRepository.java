package com.example.Amazonia_Online_Store.repositories;

import com.example.Amazonia_Online_Store.models.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findAllByIdIn(List<Long> productIdList);
}

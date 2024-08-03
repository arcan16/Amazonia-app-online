package com.example.Amazonia_Online_Store.repositories;

import com.example.Amazonia_Online_Store.models.OrderDetailEntity;
import com.example.Amazonia_Online_Store.models.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findAllByUserId(Long id);
}

package com.example.Amazonia_Online_Store.repositories;

import com.example.Amazonia_Online_Store.models.UserDetailsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetailsEntity, Long> {
    Optional<UserDetailsEntity> findByUserId(Long id);

    @Query("""
            SELECT u FROM UserDetailsEntity u WHERE u.user.role = 'CUSTOMER'
            """)
    Page<UserDetailsEntity> findAllCustomers(Pageable page);

    @Query("""
            SELECT u FROM UserDetailsEntity u WHERE u.user.role = 'EMPLOYEE'
            """)
    Page<UserDetailsEntity> findAllEmployees(Pageable page);
}

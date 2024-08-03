package com.example.Amazonia_Online_Store.repositories;

import com.example.Amazonia_Online_Store.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    boolean existsByUsername(String username);

    @Query("""
            SELECT u FROM UserEntity u WHERE u.username = :username
            AND u.id != :id
            """)
    Optional<UserEntity> getReferenceByUsername(String username, Long id);
}

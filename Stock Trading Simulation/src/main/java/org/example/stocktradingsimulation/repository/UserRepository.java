package org.example.stocktradingsimulation.repository;

import java.util.Optional;

import org.example.stocktradingsimulation.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}

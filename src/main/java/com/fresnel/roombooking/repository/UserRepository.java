package com.fresnel.roombooking.repository;

import com.fresnel.roombooking.model.User;
import com.fresnel.roombooking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    void deleteByEmail(String email);

   Optional<User> findByEmail(String email);

    Optional<User> findById(Long userId);
}

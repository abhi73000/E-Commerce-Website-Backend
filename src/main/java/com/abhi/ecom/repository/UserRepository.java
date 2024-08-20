package com.abhi.ecom.repository;

import java.util.Optional;

import com.abhi.ecom.enums.UserRole;
import com.abhi.ecom.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findFirstByEmail(String email);

    Optional<User> findByRole(UserRole userRole);

}

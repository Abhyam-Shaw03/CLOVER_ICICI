package com.bankapp.user_service.repository;

import com.bankapp.user_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

    User findByUserId(String userId);
}

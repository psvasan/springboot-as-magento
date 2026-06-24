package com.dualsession.vasan.user.repository;

import com.dualsession.vasan.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    public  User findByUsername(String username);
}

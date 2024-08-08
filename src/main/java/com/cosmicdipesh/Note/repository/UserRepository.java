package com.cosmicdipesh.Note.repository;

import com.cosmicdipesh.Note.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {

    Optional<User> findByUsername(String username);

}

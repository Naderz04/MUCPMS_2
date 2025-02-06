package com.MUCPMS.MUCPMS.repository;

import com.MUCPMS.MUCPMS.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {


    User findByEmail(String email);
}

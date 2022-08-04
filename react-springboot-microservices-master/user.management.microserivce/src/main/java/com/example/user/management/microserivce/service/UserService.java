package com.example.user.management.microserivce.service;

import com.example.user.management.microserivce.model.User;
import com.example.user.management.microserivce.repository.UserRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User saveUser(User user);

    User findByUsername(String username);

    List<String> findUsers(List<Long> idList);
}

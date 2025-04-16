package com.jihyuk.mojong_v2.repository;

import com.jihyuk.mojong_v2.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameAndEnabledTrue(String username);

    boolean existsByUsername(String username);

}

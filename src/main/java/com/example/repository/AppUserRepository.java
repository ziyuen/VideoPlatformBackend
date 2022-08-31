package com.example.repository;

import com.example.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface AppUserRepository
        extends JpaRepository<AppUser, Integer> {
        Optional<AppUser> findByEmail(String email);

        @Transactional
        @Modifying
        // "?1" represents the first parameter of the method, which is "email"
        @Query("update AppUser a set a.enabled=true where a.email=?1")
        int enableAppUser(String email);

}

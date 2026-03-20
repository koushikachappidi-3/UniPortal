package org.university.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.university.model.AppUser;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUsername(String username);

    boolean existsByUsername(String username); //
}


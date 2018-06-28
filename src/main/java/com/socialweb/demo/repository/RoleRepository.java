package com.socialweb.demo.repository;

import com.socialweb.demo.model.RoleNames;
import com.socialweb.demo.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Roles,Long> {

    Optional<Roles> findByName(RoleNames roleNames);
}

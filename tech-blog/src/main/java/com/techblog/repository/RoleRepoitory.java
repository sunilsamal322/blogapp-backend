package com.techblog.repository;

import com.techblog.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepoitory extends JpaRepository<Role,Integer> {
}

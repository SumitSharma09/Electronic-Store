package com.sumit.electronic.store.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sumit.electronic.store.entities.Role;

public interface RoleRepository extends JpaRepository<Role,String>{

}

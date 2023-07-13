package com.role.ecommerce.dao;

import com.role.ecommerce.entity.Role;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface RoleDao extends CrudRepository<Role, String> {

}

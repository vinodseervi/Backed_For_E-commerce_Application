package com.role.ecommerce.dao;

import com.role.ecommerce.entity.Cart;
import com.role.ecommerce.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartDao extends CrudRepository<Cart, Integer> {

        public List<Cart> findByUser(User user);
}

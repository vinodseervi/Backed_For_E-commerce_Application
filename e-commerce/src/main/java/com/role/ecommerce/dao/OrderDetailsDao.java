package com.role.ecommerce.dao;

import com.role.ecommerce.entity.OrderDetail;
import com.role.ecommerce.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderDetailsDao extends CrudRepository<OrderDetail, Integer> {

    public List<OrderDetail> findByUser(User user);

}

package com.role.ecommerce.dao;

import com.role.ecommerce.entity.OrderDetail;
import org.springframework.data.repository.CrudRepository;

public interface OrderDetailsDao extends CrudRepository<OrderDetail, Integer> {

}

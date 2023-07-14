package com.role.ecommerce.service;

import com.role.ecommerce.configuration.JwtRequestFilter;
import com.role.ecommerce.dao.OrderDetailsDao;
import com.role.ecommerce.dao.ProductDao;
import com.role.ecommerce.dao.UserDao;
import com.role.ecommerce.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailsService {

    private static final String ORDER_PLACED = "Placed";

    @Autowired
    private OrderDetailsDao orderDetailsDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private UserDao userDao;
    public void placeOrder(OrderInput orderInput){
        List<OrderProductQuantity> ProductQuantityList =  orderInput.getOrderProductQuantityList();
        for(OrderProductQuantity o: ProductQuantityList){
            Product product =  productDao.findById(o.getProductId()).get();
            String currentUser =  JwtRequestFilter.CURRENT_USER;
            User user = userDao.findById(currentUser).get();
            OrderDetail orderDetail = new OrderDetail(
                    orderInput.getFullName(),
                    orderInput.getFullAddress(),
                    orderInput.getContactNumber(),
                    orderInput.getAlternateContactNumber(),
                    ORDER_PLACED,
                     product.getProductDiscountedPrice() * o.getQuantity(),
                    product,
                    user

            );

            orderDetailsDao.save(orderDetail);
        }

    }
}

package com.role.ecommerce.service;

import com.role.ecommerce.configuration.JwtRequestFilter;
import com.role.ecommerce.dao.CartDao;
import com.role.ecommerce.dao.OrderDetailsDao;
import com.role.ecommerce.dao.ProductDao;
import com.role.ecommerce.dao.UserDao;
import com.role.ecommerce.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class OrderDetailsService {

    private static final String ORDER_PLACED = "Placed";

    @Autowired
    private OrderDetailsDao orderDetailsDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private CartDao cartDao;

    @Autowired
    private UserDao userDao;


    public void markOrderAsDelivered(Integer orderId){
        OrderDetail orderDetail =orderDetailsDao.findById(orderId).get();
        if(orderDetail!= null){
            orderDetail.setOrderStatus(("Delivered"));
            orderDetailsDao.save(orderDetail);
        }

    }


    public List<OrderDetail> getAllOrderDetails(String status){
        List<OrderDetail> orderDetails = new ArrayList<>();
        if(status.equals("All")){
            orderDetailsDao.findAll().forEach(
                    x-> orderDetails.add(x)
            );
        } else {
            orderDetailsDao.findByOrderStatus(status).forEach(
                    x-> orderDetails.add(x)
            );
        }
        return orderDetails;
    }


    public List<OrderDetail> getOrderDetails(){
        String currentUser = JwtRequestFilter.CURRENT_USER;
        User user = userDao.findById(currentUser).get();

        return orderDetailsDao.findByUser(user);

    }


    public void placeOrder(OrderInput orderInput, boolean isSingleProductCheckout){
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

            //empty the cart.
            if(!isSingleProductCheckout){
               List<Cart> carts =  cartDao.findByUser(user);
               carts.stream().forEach(x-> cartDao.deleteById(x.getCartId()));
            }

            orderDetailsDao.save(orderDetail);
        }

    }
}

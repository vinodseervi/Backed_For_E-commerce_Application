package com.ecommerce.service;

import com.ecommerce.configuration.JwtRequestFilter;
import com.ecommerce.dao.CartDao;
import com.ecommerce.dao.OrderDetailsDao;
import com.ecommerce.dao.ProductDao;
import com.ecommerce.dao.UserDao;
import com.ecommerce.entity.*;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderDetailService {

    private static final String ORDER_PLACED = "Placed";
    private static final String KEY = "rzp_test_hwljW9u7S9InOW";
    private static final String KEY_SECRET = "bzeKg6Ne34v0Ck6ThIYMpEDM";
    private static final String CURRENCY = "INR";

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
                    orderInput.getAlternativeContactNumber(),
                    ORDER_PLACED,
                    product.getProductDiscountedPrice() * o.getQuantity(),
                    orderInput.getTransactionId(),
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

    public TransactionDetails createTransaction(Double amount){
        //key
        //currency
        //amount
        //secret key
       try {

           JSONObject jsonObject = new JSONObject();
           jsonObject.put("amount",(amount*100));
           jsonObject.put("currency",CURRENCY);
           RazorpayClient razorpayClient = new RazorpayClient(KEY, KEY_SECRET);
           Order order =  razorpayClient.orders.create(jsonObject);
           System.out.println(order);

           TransactionDetails transactionDetails =prepareTransactionDetails(order);
           return transactionDetails;
       } catch (Exception e){
           System.out.println(e.getMessage());
       }
        return null;
    }

    private TransactionDetails prepareTransactionDetails(Order order){
        String orderId = order.get("id");
        String currency = order.get("currency");
        Integer amount = order.get("amount");

        TransactionDetails transactionDetails = new TransactionDetails(orderId,currency,amount, KEY);
        return transactionDetails;
    }

}
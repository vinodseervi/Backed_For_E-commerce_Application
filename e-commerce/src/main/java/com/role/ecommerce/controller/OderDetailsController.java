package com.role.ecommerce.controller;

import com.role.ecommerce.entity.OrderDetail;
import com.role.ecommerce.entity.OrderInput;
import com.role.ecommerce.service.OrderDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class OderDetailsController {

    @Autowired
    private OrderDetailsService orderDetailsService;

    @PreAuthorize("hasRole('User')")
    @PostMapping({"/placeOrder/{isSingleProductCheckout}"})
    public void placeOrder(@PathVariable(name = "isSingleProductCheckout") boolean isSingleProductCheckout, @RequestBody OrderInput orderInput){
        orderDetailsService.placeOrder(orderInput, isSingleProductCheckout);

    }
    @PreAuthorize("hasRole('User')")
    @GetMapping({"/getOrderDetails"})
    public List<OrderDetail> fetOrderDetails(){
        return orderDetailsService.getOrderDetails();
    }

    @PreAuthorize("hasRole('Admin')")
    @GetMapping({"/getAllOrderDetails/{status}"})
    public List<OrderDetail> getAllOrderDetails(@PathVariable(name = "status") String status){
        return orderDetailsService.getAllOrderDetails(status);

    }
    @PreAuthorize("hasRole('Admin')")
    @GetMapping({"/markOrderAsDelivered/{orderId}"})
    public void markOrderAsDelivered(@PathVariable(name = "orderId") Integer orderId){
        orderDetailsService.markOrderAsDelivered(orderId);
    }


}

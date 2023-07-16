package com.role.ecommerce.service;

import com.role.ecommerce.configuration.JwtRequestFilter;
import com.role.ecommerce.dao.CartDao;
import com.role.ecommerce.dao.ProductDao;
import com.role.ecommerce.dao.UserDao;
import com.role.ecommerce.entity.Cart;
import com.role.ecommerce.entity.Product;
import com.role.ecommerce.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private CartDao cartDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private UserDao userDao;

    public Cart addToCart(Integer productId){
        Product product = productDao.findById(productId).get();
        String username = JwtRequestFilter.CURRENT_USER;
        User user = null;
        if(username != null) {
             user = userDao.findById(username).get();
        }

        List<Cart> cartList = cartDao.findByUser(user);
        List<Cart> filterList = cartList.stream().filter(x-> x.getProduct().getProductId() == productId).collect(Collectors.toList());

        if(filterList.size() > 0){
            return null;
        }

        if(product != null && user != null){
            Cart cart = new Cart(product,user);
            return cartDao.save(cart);
        }
        return null;
    }

    public List<Cart> getCartDetails(){
        String username = JwtRequestFilter.CURRENT_USER;
        User user = userDao.findById(username).get();
        return cartDao.findByUser(user);

    }

    public void deleteCartItem(Integer cartId){
        cartDao.deleteById(cartId);
    }




}

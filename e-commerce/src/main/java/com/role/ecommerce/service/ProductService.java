package com.role.ecommerce.service;

import com.role.ecommerce.dao.ProductDao;
import com.role.ecommerce.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductDao productDao;

    public Product addNewProduct(Product product){
        return productDao.save(product);
    }

    public List<Product> getAllProduct(){
        return (List<Product>)productDao.findAll();
    }

    public void deleteProductDetails(Integer ProductId){
        productDao.deleteById(ProductId);
    }

    public Product getProductDetailsById(Integer productId){
        return productDao.findById(productId).get();
    }

   
}

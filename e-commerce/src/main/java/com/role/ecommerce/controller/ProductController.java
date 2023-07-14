package com.role.ecommerce.controller;

import com.role.ecommerce.entity.Product;
import com.role.ecommerce.entity.ProductImage;
import com.role.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class ProductController {
    @Autowired
    private ProductService productService;


    @PreAuthorize("hasRole('Admin')")
    @PostMapping(value = {"/addNewProduct"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Product addNewProduct(@RequestPart("Product") Product product,
                                 @RequestPart("imageFile") MultipartFile[] file) {
        //  return productService.addNewProduct(product);
        try {
            Set<ProductImage> images = uploadImage(file);
            product.setProductImages(images);
            return productService.addNewProduct(product);
        } catch (Exception e) {
            System.out.println(e.getMessage() + "Vinod error are coming ");
            return null;
        }

    }

    public Set<ProductImage> uploadImage(MultipartFile[] multipartFiles) throws IOException {
        Set<ProductImage> productImages = new HashSet<>();
        for (MultipartFile file : multipartFiles) {
            ProductImage productImage = new ProductImage(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes()
            );
            productImages.add(productImage);
        }
        return productImages;
    }


    @GetMapping({"/getAllProducts"})
    public List<Product> getAllProduct(){
       return  productService.getAllProduct();
    }

    @PreAuthorize("hasRole('Admin')")
    @DeleteMapping({"/deleteProductDetails/{productId}"})
    public void deleteProductDetails(@PathVariable("productId") Integer productId){
            productService.deleteProductDetails(productId);
    }
    @GetMapping({"/getProductDetailsById/{productId}"})
    public Product getProductDetailsById(@PathVariable("productId") Integer productId){
            return productService.getProductDetailsById(productId);
    }

    @PreAuthorize("hasRole('User')")
    @GetMapping({"/getProductDetails/{isSingleProductCheckout}/{productId}"})
    public List<Product> getProductDetails(@PathVariable(name = "isSingleProductCheckout") boolean isSingleProductCheckout,@PathVariable(name = "productId") Integer productId) {
        return productService.getProductDetails(isSingleProductCheckout,productId);
    }



}

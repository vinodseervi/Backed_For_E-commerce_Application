package com.role.ecommerce.controller;

import com.role.ecommerce.entity.JwtRequest;
import com.role.ecommerce.entity.JwtResponse;
import com.role.ecommerce.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class JwtController {

    @Autowired
    private JwtService jwtService;

    @PostMapping({"/authenticate"})
    public JwtResponse createJwtToken(@RequestBody JwtRequest jwtRequest) throws Exception {
        System.out.println(jwtRequest.getUserName());
        System.out.println(jwtRequest.getUserPassword());
        return jwtService.createJwtToken(jwtRequest);
    }
}

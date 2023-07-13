package com.role.ecommerce.service;

import com.role.ecommerce.dao.RoleDao;
import com.role.ecommerce.dao.UserDao;
import com.role.ecommerce.entity.Role;
import com.role.ecommerce.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerNewUser(User user){
        Role role = roleDao.findById("User").get();
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRole(roles);

        user.setUserPassword(getEncodedPassword(user.getUserName()));
        return userDao.save(user);
    }

    public void initRolesAndUser(){
        System.out.println("come into init function");
        Role adminRole = new Role();
        adminRole.setRoleName("Admin");
        adminRole.setRoleDescription("admin role for application");
        roleDao.save(adminRole);

        Role userRole = new Role();
        userRole.setRoleName("User");
        userRole.setRoleDescription("User role for application");
        roleDao.save(userRole);

        User adminUser = new User();
        adminUser.setUserName("admin123");
        adminUser.setUserFirstName("admin");
        adminUser.setUserLastName("admin");
        adminUser.setUserPassword(getEncodedPassword("admin@123"));
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);
        adminUser.setRole(adminRoles);
        userDao.save(adminUser);

        User user = new User();
        user.setUserName("vinod123");
        user.setUserLastName("vinod");
        user.setUserFirstName("vinod");
        user.setUserPassword(getEncodedPassword("vinod@123"));
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(userRole);
        user.setRole(userRoles);
        userDao.save(user);


    }

    public String getEncodedPassword(String password){
        return passwordEncoder.encode(password);
    }
}

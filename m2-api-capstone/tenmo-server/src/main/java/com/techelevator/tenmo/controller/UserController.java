package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.auth.dao.JdbcUserDAO;
import com.techelevator.tenmo.auth.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class UserController {

    @Autowired
    JdbcUserDAO jdbcUserDAO;

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(path = "/users/",method = RequestMethod.GET )
    public List<User> retrieveAllUsers() {

        List<User> allUser = jdbcUserDAO.findAll();
        List<User> listToSend = new ArrayList<>();

        for (User user: allUser) {

            User listableUser = new User();

            listableUser.setId(user.getId());
            listableUser.setUsername(user.getUsername());

            listToSend.add(listableUser);
        }

        return listToSend;
    }




}

package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.TenmoApplication;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
//@PreAuthorize("isAuthenticated()")
@RequestMapping("/user")
public class TEnmoController {

    private UserDao userDao;

    public TEnmoController(UserDao userDao) {
        this.userDao = userDao;
    }

    // return the list of users
    @RequestMapping(method = RequestMethod.GET)
    public Map<Long, String> list() {
        return userDao.findAllUsernames();
    }


    // display user's current balance
    @RequestMapping(value = "/balance", method = RequestMethod.GET)
    public BigDecimal userBalance(Principal principal) {
        return userDao.getBalance(principal.getName());

    }

    @RequestMapping(path = "/whoami")
    public String whoAmI(Principal principal) {

        return principal.getName();
    }

//
//
//
//
//    // return the list of a specific users transactions
//    @RequestMapping(method = RequestMethod.GET)
//
//
//    // return transfers by ID
//    @RequestMapping(value = "/account/transfer")
//
//
//    // returns transferred TE Bucks
//    @RequestMapping(value = "/account/transfer")

}

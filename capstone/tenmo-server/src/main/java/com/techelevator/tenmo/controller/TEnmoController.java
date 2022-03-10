package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.TenmoApplication;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.security.UserNotActivatedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    private User user;

    public TEnmoController(UserDao userDao) {
        this.userDao = userDao;
    }

    // return the list of users
    @RequestMapping(method = RequestMethod.GET)
    public Map<Long, String> list() {
        return userDao.findAllUsernames();
    }


    // display user's current balance
    @RequestMapping(value = "/balance/{id}", method = RequestMethod.GET)
    public BigDecimal userBalance(@PathVariable long id) throws UserNotActivatedException {
        return userDao.getBalance(id);
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

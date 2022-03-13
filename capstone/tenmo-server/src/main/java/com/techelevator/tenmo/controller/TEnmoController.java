package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.TenmoApplication;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transaction;
import com.techelevator.tenmo.model.TransactionList;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.security.UserNotActivatedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.TransactionException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    // Return the list of users
    @RequestMapping(method = RequestMethod.GET)
    public Map<Long, String> list() {
        return userDao.findAllUsernames();
    }

    // Display user's current balance
    @RequestMapping(value = "/balance/{id}", method = RequestMethod.GET)
    public BigDecimal userBalance(@PathVariable long id) throws UserNotActivatedException {
        return userDao.getBalance(id);
    }
    // Transfer of TE bucks (authenticated users)
    @RequestMapping(value = "/{id}/transfer/{targetId}/{amount}", method = RequestMethod.PUT)
    public void transaction(@PathVariable long id, @PathVariable long targetId, @PathVariable BigDecimal amount) throws UsernameNotFoundException {
        userDao.transferTo(id, targetId, amount);
    }
    // Prints log of a user's transactions
    @RequestMapping(value = "/{id}/log", method = RequestMethod.GET)
    public TransactionList getUserLog(@PathVariable long id) throws UsernameNotFoundException {
        return userDao.getLog(id);
    }

    // Print transfer details (step 6)
    @RequestMapping(value = "/{id}/transfer/{transactionId}", method = RequestMethod.GET)
    public Transaction transferDetails(@PathVariable long id, @PathVariable long transferId) throws TransactionException {
       return userDao.getTransferDetails(id, transferId);
    }

    @RequestMapping(value = "/account/{accountId}", method = RequestMethod.GET)
    public String username(@PathVariable int accountId) throws TransactionException {
        return userDao.getUsername(accountId);
    }
    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    public int getId(@PathVariable String username) throws UsernameNotFoundException {
        return userDao.findIdByUsername(username);
    }



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
//


}

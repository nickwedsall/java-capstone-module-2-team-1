package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.TenmoApplication;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transaction;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.security.UserNotActivatedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    public List<Transaction> getUserLog(@PathVariable long id) throws UsernameNotFoundException {
        return userDao.getLog(id);
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





//
//    public int getBalance() {
//        return balance;
//    }
//
//    public int deposit(int amountToDeposit) {
//        balance = balance + amountToDeposit;
//        return balance;
//    }
//
//    public int withdraw(int amountToWithdraw) {
//        balance = balance - amountToWithdraw;
//        return balance;
//    }
//
//    public int transferTo(BankAccount destinationAccount, int transferAmount) {
//        if (balance >= transferAmount) {
//            withdraw(transferAmount);
//            destinationAccount.deposit(transferAmount);
//        }
//        return getBalance();
//    }

}

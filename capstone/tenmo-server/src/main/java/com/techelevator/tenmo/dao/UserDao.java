package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface UserDao {

    List<User> findAll();

    Map<Long, String> findAllUsernames();

    User findByUsername(String username);

    int findIdByUsername(String username);

    boolean create(String username, String password);

    BigDecimal getBalance(String username);

    String getPrincipal(Principal principal);
}

package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transaction;
import com.techelevator.tenmo.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JdbcUserDao implements UserDao {

    private static final BigDecimal STARTING_BALANCE = new BigDecimal("1000.00");
    private JdbcTemplate jdbcTemplate;

    public JdbcUserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int findIdByUsername(String username) {
        String sql = "SELECT user_id FROM tenmo_user WHERE username ILIKE ?;";
        Integer id = jdbcTemplate.queryForObject(sql, Integer.class, username);
        if (id != null) {
            return id;
        } else {
            return -1;
        }
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id, username, password_hash FROM tenmo_user;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            User user = mapRowToUser(results);
            users.add(user);
        }
        return users;
    }

    @Override
    public Map<Long, String> findAllUsernames() {
        List<User> users = findAll();
        Map<Long, String> mappedUsers = new HashMap<>();
        for (User user : users) {
            String value = user.getUsername();
            long key = user.getId();
            mappedUsers.put(key, value);
        }
        return mappedUsers;
    }

    @Override
    public User findByUsername(String username) throws UsernameNotFoundException {
        String sql = "SELECT user_id, username, password_hash FROM tenmo_user WHERE username ILIKE ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, username);
        if (rowSet.next()) {
            return mapRowToUser(rowSet);
        }
        throw new UsernameNotFoundException("User " + username + " was not found.");
    }

    @Override
    public boolean create(String username, String password) {

        // create user
        String sql = "INSERT INTO tenmo_user (username, password_hash) VALUES (?, ?) RETURNING user_id";
        String password_hash = new BCryptPasswordEncoder().encode(password);
        Integer newUserId;
        try {
            newUserId = jdbcTemplate.queryForObject(sql, Integer.class, username, password_hash);
        } catch (DataAccessException e) {
            return false;
        }

        // create account
        sql = "INSERT INTO account (user_id, balance) values(?, ?)";
        try {
            jdbcTemplate.update(sql, newUserId, STARTING_BALANCE);
        } catch (DataAccessException e) {
            return false;
        }

        return true;
    }

    @Override
    public BigDecimal getBalance(long id) {
        String sql = "SELECT balance " +
                "FROM account " +
                "WHERE user_id = ?";
        BigDecimal balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, id);
        return balance;
    }

    @Override
    public String getPrincipal(Principal principal) {
        return principal.getName();
    }

    @Override
    public void transferTo(long id, long targetId, BigDecimal amount) {
        String sql = "UPDATE account " +
                "SET balance = balance - ? " +
                "WHERE user_id = ?; " +
                "UPDATE account " +
                "SET balance = balance + ? " +
                "WHERE user_id = ?; " +
                "INSERT INTO transfer (transfer_type_id, transfer_status_id, " +
                "account_from, account_to, amount) " +
                "VALUES (2, 2, (SELECT account_id FROM account WHERE user_id = ?), " +
                "(SELECT account_id FROM account WHERE user_id = ?), ?) ;";
        jdbcTemplate.update(sql, amount, id, amount, targetId, id, targetId, amount);
    }
/* TODO add Joins - tenmo_user, account, transfer; hopefully result in username = callable in client sout
      redo transaction models to match SQL variables    */

    @Override
    public List<Transaction> getLog (long id) {
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, tenmo_user.username AS account_to_user, amount " +
        "FROM transfer " +
        "JOIN account ON account.account_id = transfer.account_from " +
        "JOIN tenmo_user ON account.user_id = tenmo_user.user_id " +
        "WHERE transfer.account_from = (SELECT account_id FROM account WHERE user_id = ?);";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);
        List<Transaction> transactions = new ArrayList<>();
        if (rowSet.next()) {
            mapRowToTransaction(rowSet);
            for (Transaction transaction : transactions) {
                transactions.add(transaction);
            }
        }  return transactions;
    }


    private User mapRowToUser(SqlRowSet rs) {
        User user = new User();
        user.setId(rs.getLong("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password_hash"));
        user.setActivated(true);
        user.setAuthorities("USER");
        return user;
    }

    private Transaction mapRowToTransaction(SqlRowSet rs) {
        Transaction transaction = new Transaction();
        transaction.setTransferId(rs.getInt("transfer_id"));
        transaction.setTransferTypeId(rs.getInt("transfer_type_id"));
        transaction.setTransferStatusId(rs.getInt("transfer_status_id"));
        transaction.setAccountFrom(rs.getInt("account_from"));
        transaction.setAccountToUsername(rs.getString("account_to_user"));
        transaction.setAmount(rs.getBigDecimal("amount"));
        return transaction;
    }

}

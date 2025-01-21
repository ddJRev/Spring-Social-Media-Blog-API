package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;


@Service
public class AccountService {
    
    @Autowired
    private AccountRepository accountRepository;

    /**
     * 
     * @param accountRepository
     */
    public void setAccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }
    /**
     * 
     * @param id
     * @return Optional<Account> - Account object if found
     */
    public Optional<Account> findById(Integer id){
        return accountRepository.findById(id);
    }
    /**
     * 
     * @param account
     * @return
     */
    public Account persistAccount(Account account){
        return accountRepository.save(account);
    }

    /**
     * 
     * @param username
     * @param password
     * @return
     */
    public Account authenticateLogin(String username, String password){ 
        Account retrievedAccount = accountRepository.findByUsername(username);
        return (retrievedAccount != null && retrievedAccount.getPassword().equals(password)) ? retrievedAccount : null;

    }
    public Account findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }
}

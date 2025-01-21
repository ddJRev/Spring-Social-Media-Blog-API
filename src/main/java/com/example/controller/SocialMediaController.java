package com.example.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import com.example.entity.Account;

/**
 * You will need to write your own endpoints and handlers for your 
 * controller using Spring. The endpoints you will need can be found in 
 * readme.md as well as the test cases. You be required to use the 
 * -@GET/POST/PUT/DELETE/etc Mapping annotations where applicable as 
 * well as the @ResponseBody and @PathVariable annotations. 
 */
@RestController
public class SocialMediaController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private AccountService accountService;



    /**
     * The creation of the message will be successful 
     * if and only if the messageText is not blank, 
     * is not over 255 characters, and postedBy 
     * refers to a real, existing user. 
     * If successful, the response body should contain 
     * a JSON of the message, including its messageId. 
     * The response status should be 200, which is the default. 
     * The new message should be persisted to the database.
     * 
     * If the creation of the message is not successful, 
     * the response status should be 400. (Client error
     * 
     * @param message
     * @return ResponseEntity.status(400).body(null) - client error, failure
     * @return ResponseEntity.status(200).body(message) - success
     */ 
    @PostMapping("/messages")
    public ResponseEntity<Message> createMessageHandler(@RequestBody Message message){ 
 
        Account validAccount = accountService.findById(message.getPostedBy()).orElse(null);
        // if any conditions met, return client error, else return message
        if (message.getMessageText() == null || message.getMessageText().trim().isEmpty() || message.getMessageText().length() > 255 || validAccount == null) { 
            return ResponseEntity.status(400).body(null);
        }
        Message newMessage = messageService.persistMessage(message);
        return ResponseEntity.status(200).body(newMessage);
    } 
    
    /**
     * The response body should contain a JSON representation 
     * of a list containing all messages retrieved from the 
     * database. It is expected for the list to simply be empty 
     * if there are no messages. The response status 
     * should always be 200, which is the default
     * 
     * @return ResponseEntity.status(200).body(foundMessages) - response status and list of messages or null
     */
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessagesHandler(){ 
 
        List<Message> messages = messageService.findAll();
        
        return ResponseEntity.status(200).body(messages);    
    }

    /**
     * 
     * The response body should contain a JSON representation 
     * of the message identified by the messageId. It is 
     * expected for the response body to simply be empty if 
     * there is no such message. The response status should 
     * always be 200, which is the default.
     * 
     * @param messageId
     * @return ResponseEntity.status(200).body(foundMessage) - response status and message / null
     */
    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageByIdHandler(@PathVariable Integer messageId){ 
 
        Message foundMessage = messageService.findById(messageId).orElse(null);
        if (foundMessage == null){
            return ResponseEntity.status(200).body(null);
        }
        return ResponseEntity.status(200).body(foundMessage);    
    }

    /**
     * 
     * The deletion of an existing message should remove an 
     * existing message from the database. If the message existed, 
     * the response body should contain the number of rows updated (1). 
     * The response status should be 200, which is the default.
     * 
     * If the message did not exist, the response status should be 200, 
     * but the response body should be empty. This is because the 
     * DELETE verb is intended to be idempotent, ie, multiple calls to 
     * the DELETE endpoint should respond with the same type of response. 
     * 
     * @param messageId
     * @return ResponseEntity.status(200).body(rowsUpdated) - response status and int of rows updated or null
     */ 
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessageByIdHandler(@PathVariable Integer messageId){ 
        
        Message foundMessage = messageService.findById(messageId).orElse(null);
        if (foundMessage == null){
            return ResponseEntity.status(200).body(null);
        }
        
        messageService.deleteMessageById(messageId);
        return ResponseEntity.status(200).body(1);  // return success status and int rows updated
    }
    

    /**
     * 
     * The update of a message should be successful if and only 
     * if the message id already exists and the new messageText 
     * is not blank and is not over 255 characters. 
     * If the update is successful, the response body should 
     * contain the number of rows updated (1), and the response 
     * status should be 200, which is the default. The message 
     * existing on the database should have the updated messageText.
     * 
     * If the update of the message is not successful for any reason, 
     * the response status should be 400. (Client error)
     * 
     * 
     * 
     * @param messageId
     * @param newMessage
     * @return ResponseEntity.status(200).body(rowsUpdated) - response status and int of rows updated or null
     */
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> updateMessageByIdHandler(@PathVariable Integer messageId, @RequestBody Message newMessage){ 
        
        Message foundMessage = messageService.findById(messageId).orElse(null);

        // if any conditions met, return client error, else return message
        if (newMessage.getMessageText() == null || newMessage.getMessageText().trim().isEmpty() || newMessage.getMessageText().length() > 255 || foundMessage == null) { 
            return ResponseEntity.status(400).body(null);
        }

        // set the matching messages text to new text & persist changes
        foundMessage.setMessageText(newMessage.getMessageText());
        messageService.persistMessage(foundMessage);

        return ResponseEntity.status(200).body(1); // return success status and int rows updated

    }
       
 
    /**
     * 
     *  The response body should contain a JSON representation 
     * of a list containing all messages posted by a particular 
     * user, which is retrieved from the database. It is expected 
     * for the list to simply be empty if there are no messages. 
     * The response status should always be 200, which is the default.
     * 
     * @param accountId
     * @return ResponseEntity.status(200).body(foundMessages) - response status and list of messages
     */
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getAllMessagesByAccountIdHandler(@PathVariable Integer accountId){ 
         
        List<Message> foundMessages = messageService.findByPostedBy(accountId);
        return ResponseEntity.status(200).body(foundMessages);   

    } 
 

    /**
     * Successful if and only if the username and password 
     * provided in the request body JSON match a real account 
     * existing on the database. If successful, the response 
     * body should contain a JSON of the account in the response 
     * body, including its accountId. The response status should 
     * be 200 OK, which is the default.
     * 
     * If the login is not successful, the response status should be 401
     * 
     * @param Account account object
     * @return ResponseEntity.status(200).body(authenticatedAccount) - success status and authenticated account obj
     * @return ResponseEntity.status(401).body(null) - fail status and null
     */
    @PostMapping("/login")
    public ResponseEntity<Account> authenticateLoginHandler(@RequestBody Account account){ 
 
        Account authenticatedAccount = accountService.authenticateLogin(account.getUsername(), account.getPassword());
        // if authAccount not null, return ok response and account obj, else failed response and null obj/bodyp
        return (authenticatedAccount != null) ? ResponseEntity.status(200).body(authenticatedAccount) : ResponseEntity.status(401).body(null);

    } 
   
    /**
     * Successful if and only if the username is not blank, 
     * the password is at least 4 characters long, and an 
     * Account with that username does not already exist. 
     * If all these conditions are met, the response body 
     * should contain a JSON of the Account, including its 
     * accountId. The response status should be 200 OK, 
     * which is the default. The new account should be 
     * persisted to the database.
     * 
     * If the registration is not successful due to a 
     * duplicate username, the response status should be 409. (Conflict)
     * 
     * If the registration is not successful for some other reason, 
     * the response status should be 400. (Client error)
     * 
     * @param Account account object
     * @return ResponseEntity.status(200).body(authenticatedAccount) - success status and persisted new account obj
     * @return ResponseEntity.status(409).body(null) - fail status and null
     */
    @PostMapping("/register")
    public ResponseEntity<Account> registerAccountHandler(@RequestBody Account account){ 
        if (account.getUsername() == null || account.getPassword() == null || account.getUsername().trim().isEmpty()|| account.getPassword().length() < 4){
            ResponseEntity.status(400).body(null); 
        }
        Account matchingAccount = accountService.findByUsername(account.getUsername());
        // if no existing / matching account found for username, return success status and persisted account obj, else return fail status and null
        return (matchingAccount == null) ? ResponseEntity.status(200).body(accountService.persistAccount(account)) : ResponseEntity.status(409).body(null);  
    }

}

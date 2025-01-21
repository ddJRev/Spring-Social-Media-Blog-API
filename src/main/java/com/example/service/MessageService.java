package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    /**
     * 
     */
    public void setAccountService(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }
    
    /**
     * 
     * @param id
     * @return
     */
    public Optional<Message> findById(Integer id){
        return messageRepository.findById(id);
        
    }
    /**
     * 
     * @param message 
     * @return
     */
    public Message persistMessage(Message message){
        return messageRepository.save(message);
    }
    
    /**
     * 
     * @return
     */
    public List<Message> findAll(){
        return messageRepository.findAll();
    }
    /**
     * 
     * @param accountId
     * @return
     */
    public List<Message> findByPostedBy(Integer accountId){
        return messageRepository.findByPostedBy(accountId);
    }

    /**
     * 
     * @param messageId
     */
    public void deleteMessageById(Integer messageId){
        messageRepository.deleteById(messageId);
    }

}

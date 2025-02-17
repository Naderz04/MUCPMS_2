package com.MUCPMS.MUCPMS.service;

import com.MUCPMS.MUCPMS.model.Message;
import com.MUCPMS.MUCPMS.model.User;
import com.MUCPMS.MUCPMS.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    private List<Message> messages = new ArrayList<>();


    public Message sendMessage(User sender, User receiver, String content) {
        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());
        return messageRepository.save(message);
    }
    public MessageService() {
        // Adding some default messages to simulate the chat history
        messages.add(new Message("Alice", "Hello!"));
        messages.add(new Message("Bob", "Hi there!"));
        messages.add(new Message("Alice", "How are you?"));
    }

    public Iterable<Message> getAllMessages() {
        return messages;
    }

    public void addMessage(Message message) {
        messages.add(message);
    }
}

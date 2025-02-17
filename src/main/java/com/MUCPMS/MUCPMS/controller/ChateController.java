package com.MUCPMS.MUCPMS.controller;


import ch.qos.logback.core.model.Model;
import com.MUCPMS.MUCPMS.model.Message;
import com.MUCPMS.MUCPMS.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatController {

    private final MessageService messageService;

    @Autowired
    public ChatController(MessageService messageService) {
        this.messageService = messageService;
    }

    // Method to display the chat page and pass messages to the Thymeleaf template
    @GetMapping("/chat")
    public String chatPage(Model model) {
        // Fetch all messages (you can replace this with your own message fetching logic)
        Iterable<Message> messages = messageService.getAllMessages();  // Assuming you have a MessageService that retrieves messages
        model.addAttribute("messages", messages);  // Add messages to the model to pass to Thymeleaf template
        return "chat";  // Thymeleaf template name
    }
}



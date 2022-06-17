package com.github.manojesus.messagesender.controller;

import com.github.manojesus.messagesender.model.EmailByUserFolder;
import com.github.manojesus.messagesender.model.Message;
import com.github.manojesus.messagesender.model.primarykey.EmailByUserFolderPrimaryKey;
import com.github.manojesus.messagesender.repository.MessageRepository;
import com.github.manojesus.messagesender.repository.UnreadEmailStatsRepository;
import com.github.manojesus.messagesender.service.EmailByUserFolderService;
import com.github.manojesus.messagesender.service.LoadDefaultModelService;
import com.github.manojesus.messagesender.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

import static com.github.manojesus.messagesender.util.constants.AttributesNames.EMAIL;
import static com.github.manojesus.messagesender.util.constants.AttributesNames.TO_LIST;
import static com.github.manojesus.messagesender.util.constants.UrlNames.HOME_URL;
import static com.github.manojesus.messagesender.util.constants.UrlNames.MESSAGE_VIEW_URL_WITH_PARAMETER;
import static com.github.manojesus.messagesender.util.constants.ViewNames.MESSAGE_VIEW;

@Controller
@AllArgsConstructor
public class MessageViewController {

    private final UserService userService;
    private final MessageRepository messageRepository;
    private final EmailByUserFolderService emailByUserFolderService;
    private final LoadDefaultModelService loadDefaultModel;
    private final UnreadEmailStatsRepository unreadEmailStatsRepository;

    @GetMapping(MESSAGE_VIEW_URL_WITH_PARAMETER)
    public String messageView(Model model,
                              @PathVariable UUID messageId,
                              @RequestParam String folder, Principal principal){
        String username = principal.getName();
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        if(optionalMessage.isEmpty()){
            return "redirect:"+HOME_URL;
        }
        Message message = optionalMessage.get();
        String listTo = String.join(", ",message.getTo());

        if(!username.equals(message.getFrom()) && !listTo.contains(username)){
            return "redirect:"+HOME_URL;
        }

        model.addAttribute(EMAIL, message);
        model.addAttribute(TO_LIST, listTo);

        //Making the email as read

        EmailByUserFolderPrimaryKey pk = new EmailByUserFolderPrimaryKey(username,folder,message.getMessageId());

        Optional<EmailByUserFolder> emailByUserFolderOpt = emailByUserFolderService.getById(pk);
        if(emailByUserFolderOpt.isPresent()) {
            EmailByUserFolder emailByUserFolder = emailByUserFolderOpt.get();
            if (!emailByUserFolder.isRead()) {
                emailByUserFolder.setRead(true);
                emailByUserFolderService.saveEmailByUserFolder(emailByUserFolder);
                unreadEmailStatsRepository.decrementCounter(username, folder);
            }
        }

        //Loading default email folder list view
        loadDefaultModel.loadTemplateWithEmails(username,model);
        return MESSAGE_VIEW.getName();
    }

}

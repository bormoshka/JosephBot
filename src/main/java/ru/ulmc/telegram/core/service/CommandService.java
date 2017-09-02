package ru.ulmc.telegram.core.service;

import com.pengrad.telegrambot.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ulmc.telegram.data.ChatDao;
import ru.ulmc.telegram.data.UserDao;
import ru.ulmc.telegram.data.entity.ChatStat;
import ru.ulmc.telegram.data.entity.UserStats;

@Service
public class CommandService {
    private final MessageService messageService;
    private final UserDao userDao;
    private final ChatDao chatDao;

    @Autowired
    public CommandService(MessageService messageService,
                          UserDao userDao, ChatDao chatDao) {
        this.messageService = messageService;
        this.userDao = userDao;
        this.chatDao = chatDao;
    }

    public Boolean getStats(Message msg) {
        UserStats stats = userDao.getUserStats(Long.valueOf(msg.from().id()));
        messageService.send(msg.chat().id(), stats.toString());
        return true;
    }

    public Boolean getChatStats(Message msg) {
        ChatStat stats = chatDao.getChatStats(msg.chat().id());
        messageService.send(msg.chat().id(), stats.toString());
        return true;
    }
}

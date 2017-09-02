package ru.ulmc.telegram.core;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class Client {
    private static final Logger LOG = LoggerFactory.getLogger(Client.class);
    @Value("${bot.token}")
    private String botToken;

    @Getter
    private TelegramBot bot;

    @PostConstruct
    public void init() {
        LOG.debug("BOT STARTED");
        bot = TelegramBotAdapter.build(botToken);
    }
}

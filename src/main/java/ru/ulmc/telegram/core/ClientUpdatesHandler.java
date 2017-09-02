package ru.ulmc.telegram.core;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.MessageEntity;
import com.pengrad.telegrambot.model.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import ru.ulmc.telegram.core.event.BotCommandEvent;
import ru.ulmc.telegram.core.event.StickerEvent;
import ru.ulmc.telegram.core.event.TextMessageEvent;

import javax.annotation.PostConstruct;

@Component
public class ClientUpdatesHandler {
    private static final Logger LOG = LoggerFactory.getLogger(Client.class);
    private final ApplicationEventPublisher publisher;
    private final Client client;
    private TelegramBot bot;

    @Autowired
    public ClientUpdatesHandler(ApplicationEventPublisher publisher,
                                Client client) {
        this.publisher = publisher;
        this.client = client;
    }

    @PostConstruct
    private void init() {
        bot = client.getBot();
        subscribeForUpdates();
    }

    private void subscribeForUpdates() {
        bot.setUpdatesListener(updates -> {
            LOG.debug("Receiving updates");
            updates.forEach(update -> {
                processUpdate(update);
                LOG.debug(update.toString());
            });
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
        LOG.debug("Subscribing for updates");
    }

    private void processUpdate(Update update) {
        if (update.message() != null) {
            if (update.message().entities() != null) {
                for (MessageEntity messageEntity : update.message().entities()) {
                    if (MessageEntity.Type.bot_command.equals(messageEntity.type())) {
                        publisher.publishEvent(new BotCommandEvent(update));
                        return;
                    }
                }
            }
            if (update.message().sticker() != null) {
                publisher.publishEvent(new StickerEvent(update));
            } else if (update.message().text() != null) {
                publisher.publishEvent(new TextMessageEvent(update));
            }
        }
    }
}

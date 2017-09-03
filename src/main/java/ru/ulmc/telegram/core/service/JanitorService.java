package ru.ulmc.telegram.core.service;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import ru.ulmc.telegram.core.Client;
import ru.ulmc.telegram.core.event.PurgeEvent;
import ru.ulmc.telegram.data.BotConfig;
import ru.ulmc.telegram.data.entity.MessageWrap;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

@Service
public class JanitorService {
    private static final Logger LOG = LoggerFactory.getLogger(JanitorService.class);
    private final Client client;
    private final BotConfig botConfig;
    private final ApplicationEventPublisher publisher;
    @Getter
    private Map<Long, TreeSet<MessageWrap>> chatToMessage = new HashMap<>();

    @Autowired
    public JanitorService(Client client, BotConfig botConfig, ApplicationEventPublisher publisher) {
        this.client = client;
        this.botConfig = botConfig;
        this.publisher = publisher;
    }

    public void addMessage(MessageWrap message) {
        LOG.debug("JanitorService msgId: " + message.messageId());
        try {
            TreeSet<MessageWrap> set = chatToMessage.computeIfAbsent(message.chat().id(), k -> new TreeSet<>());
            set.add(message);
            if (set.size() >= botConfig.getPurgeAtMsgCount()) {
                publisher.publishEvent(new PurgeEvent(message.chat(), set));
            }
        } catch (Throwable th) {
            LOG.error("Error: ", th);
        }
    }
}

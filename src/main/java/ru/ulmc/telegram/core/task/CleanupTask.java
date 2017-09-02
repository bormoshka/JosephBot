package ru.ulmc.telegram.core.task;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.ulmc.telegram.core.Client;
import ru.ulmc.telegram.core.event.PurgeEvent;
import ru.ulmc.telegram.core.service.JanitorService;
import ru.ulmc.telegram.core.service.MessageService;
import ru.ulmc.telegram.data.BotConfig;
import ru.ulmc.telegram.data.ChatDao;
import ru.ulmc.telegram.data.entity.MessageWrap;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@Component
public class CleanupTask {
    private static final Logger LOG = LoggerFactory.getLogger(CleanupTask.class);

    private final JanitorService service;
    private final Client client;
    private final BotConfig config;
    private final MessageService messageService;
    private final ChatDao chatDao;

    @Autowired
    public CleanupTask(JanitorService service, Client client, BotConfig config,
                       MessageService messageService, ChatDao chatDao) {
        this.service = service;
        this.client = client;
        this.config = config;
        this.messageService = messageService;
        this.chatDao = chatDao;
    }

    @EventListener
    public void onEvent(PurgeEvent event) {
        LOG.trace("CleanUp on Purge Event");
        cleanUpStickers(event);
    }

    @Scheduled(initialDelay = 10000, fixedDelay = 30000)
    public void cleanUpStickers() {
        LOG.trace("CleanUp on Timer");
        cleanUpStickers(null);
    }

    private synchronized void cleanUpStickers(PurgeEvent event) {
        Map<Long, TreeSet<MessageWrap>> all = service.getChatToMessage();
        if (event != null) {
            processMessages(event.getSet());
        } else {
            all.forEach((chat, messages) -> {
                processMessages(messages);
            });
        }
    }

    private void processMessages(TreeSet<MessageWrap> messages) {
        if (messages.size() < config.getMaxStickersAtOnce() ||
                messages.size() < config.getMaxStickersFromOneUserAtOnce()) {
            return;
        }
        boolean exit = false;
        while (!messages.isEmpty() && !exit) {
            CleanUpBasket basket = new CleanUpBasket();
            Set<MessageWrap> cleanSet = new HashSet<>();
            messages.forEach(msg -> {
                if (!msg.isSticker() && basket.getLastMsgId() == Integer.MIN_VALUE) {
                    cleanSet.add(msg);
                    return;
                }
                if ((msg.messageId() - basket.getLastMsgId() == 1 || basket.getLastMsgId() == Integer.MIN_VALUE) && msg.isSticker()) {
                    basket.setLastMsgId(msg.messageId());
                    basket.getMessagesToDelete().add(msg);
                } else {
                    basket.close();
                    cleanSet.addAll(basket.getMessagesToDelete());
                }
            });
            if (basket.getMessagesToDelete().size() >= config.getMaxStrikersAtRow()) {
                basket.close();
                cleanSet.addAll(basket.getMessagesToDelete());
            }
            exit = !basket.isClosed();
            messages.removeAll(cleanSet);
        }
    }

    @Data
    private class CleanUpBasket {
        private Set<MessageWrap> messagesToDelete = new HashSet<>();
        private int lastMsgId = Integer.MIN_VALUE;
        private boolean isClosed = false;

        void close() {
            isClosed = true;
            if (messagesToDelete.size() >= config.getMaxStickersAtOnce()) {
                LOG.debug("Removing " + messagesToDelete.size() + " msgs");
                messagesToDelete.forEach(message -> {
                    try {
                        messageService.delete(message.chat().id(), message.messageId());
                        chatDao.incrementRemovedStickers(message.chat().id());
                    } catch (Throwable th) {
                        LOG.error("Error while trying to remove msg", th);
                        service.getChatToMessage().get(message.chat().id()).remove(message);
                    }
                });
            }
        }
    }
}

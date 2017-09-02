package ru.ulmc.telegram.core.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.ulmc.telegram.core.event.StickerEvent;
import ru.ulmc.telegram.core.event.TextMessageEvent;
import ru.ulmc.telegram.core.service.JanitorService;
import ru.ulmc.telegram.data.UserDao;
import ru.ulmc.telegram.data.entity.MessageWrap;

@Component
public class TextAndStickerListener {
    private final UserDao userDao;
    private final JanitorService janitorService;

    @Autowired
    public TextAndStickerListener(UserDao userDao, JanitorService janitorService) {
        this.userDao = userDao;
        this.janitorService = janitorService;
    }

    @EventListener
    public void onEvent(StickerEvent event) {
        userDao.incrementStickerCount(Long.valueOf(event.getUpdate().message().from().id()));
        janitorService.addMessage(new MessageWrap(event.getUpdate().message(), true));
    }

    @EventListener
    public void onEvent(TextMessageEvent event) {
        userDao.incrementMessageCount(Long.valueOf(event.getUpdate().message().from().id()));
        janitorService.addMessage(new MessageWrap(event.getUpdate().message(), false));
    }
}

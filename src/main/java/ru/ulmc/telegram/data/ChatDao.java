package ru.ulmc.telegram.data;

import ru.ulmc.telegram.data.entity.ChatStat;

public interface ChatDao {
    ChatStat getChatStats(Long chatId);

    void saveChatStat(Long chatId, ChatStat userStats);

    void incrementRemovedStickers(Long chatId);

}

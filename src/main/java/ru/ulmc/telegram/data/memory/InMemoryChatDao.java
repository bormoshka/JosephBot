package ru.ulmc.telegram.data.memory;

import org.springframework.stereotype.Service;
import ru.ulmc.telegram.data.ChatDao;
import ru.ulmc.telegram.data.entity.ChatStat;

import java.util.HashMap;
import java.util.Map;

@Service
public class InMemoryChatDao implements ChatDao {
    private Map<Long, ChatStat> data = new HashMap<>();

    @Override
    public ChatStat getChatStats(Long chatId) {
        ChatStat userStats = data.get(chatId);
        if (userStats == null) {
            data.put(chatId, userStats = new ChatStat());
        }
        return userStats;
    }

    @Override
    public void saveChatStat(Long chatId, ChatStat chatStat) {
        data.put(chatId, chatStat);
    }

    @Override
    public void incrementRemovedStickers(Long chatId) {
        getChatStats(chatId).incrementS();
    }
}

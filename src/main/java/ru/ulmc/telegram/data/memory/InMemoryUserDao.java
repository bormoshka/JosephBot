package ru.ulmc.telegram.data.memory;

import org.springframework.stereotype.Service;
import ru.ulmc.telegram.data.UserDao;
import ru.ulmc.telegram.data.entity.UserStats;

import java.util.HashMap;
import java.util.Map;

@Service
public class InMemoryUserDao implements UserDao {
    private Map<Long, UserStats> data = new HashMap<>();

    @Override
    public UserStats getUserStats(Long userId) {
        UserStats userStats = data.get(userId);
        if (userStats == null) {
            data.put(userId, userStats = new UserStats());
        }
        return userStats;
    }

    @Override
    public void saveUserStat(Long userId, UserStats userStats) {
        data.put(userId, userStats);
    }

    @Override
    public void incrementMessageCount(Long userId) {
        getUserStats(userId).incrementM();
    }

    @Override
    public void incrementStickerCount(Long userId) {
        incrementMessageCount(userId);
        getUserStats(userId).incrementS();
    }
}

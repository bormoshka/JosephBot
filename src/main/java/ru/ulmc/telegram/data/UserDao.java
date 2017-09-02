package ru.ulmc.telegram.data;

import ru.ulmc.telegram.data.entity.UserStats;

public interface UserDao {
    UserStats getUserStats(Long userId);

    void saveUserStat(Long userId, UserStats userStats);

    void incrementMessageCount(Long userId);

    void incrementStickerCount(Long userId);
}

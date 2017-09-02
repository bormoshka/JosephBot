package ru.ulmc.telegram.data.entity;

import lombok.Data;

@Data
public class UserStats {
    private int totalStickerCount;
    private int totalMessages;

    public void incrementS() {
        totalStickerCount++;
    }
    public void incrementM() {
        totalMessages++;
    }
}

package ru.ulmc.telegram.data.entity;

import lombok.Data;

@Data
public class ChatStat {
    private int removedStickers = 0;

    public void incrementS() {
        removedStickers++;
    }
}

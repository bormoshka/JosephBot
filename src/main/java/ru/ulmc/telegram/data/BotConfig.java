package ru.ulmc.telegram.data;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class BotConfig {
    @Value("${bot.stickers.fromOneUserAtOnce}")
    private int maxStickersFromOneUserAtOnce;
    @Value("${bot.stickers.atRow}")
    private int maxStickersAtOnce;
    @Value("${bot.stickers.fromUserPerMinute}")
    private int maxStickersFromUserPerMinute;
    @Value("${bot.stickers.maxStrikersAtRow}")
    private int maxStrikersAtRow;

    private int purgeAtMsgCount = 5;

}

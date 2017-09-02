package ru.ulmc.telegram.core.service;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ulmc.telegram.core.Client;

@Service
public class MessageService {
    private static final Logger LOG = LoggerFactory.getLogger(MessageService.class);
    private final Client client;

    @Autowired
    public MessageService(Client client) {
        this.client = client;
    }

    public boolean send(long chatId, String text) {
        SendResponse sendResponse = client.getBot().execute(write(chatId, text));
        boolean ok = sendResponse.isOk();
        Message message = sendResponse.message();
        if (message != null) {
            LOG.debug(message.toString());
        }
        return ok;
    }

    public boolean delete(long chatId, int messageId) {
        DeleteMessage deleteMessage = new DeleteMessage(chatId, messageId);
        BaseResponse response = client.getBot().execute(deleteMessage);
        if (!response.isOk()) {
            LOG.warn(response.description());
        }
        return response.isOk();
    }

    public void sendAsync(long chatId, String text, Callback<SendMessage, SendResponse> callback) {
        client.getBot().execute(write(chatId, text), callback);
    }

    public boolean reply(long chatId, int replyToId, String text) {
        SendResponse sendResponse = client.getBot().execute(writeReply(chatId, replyToId, text));
        boolean ok = sendResponse.isOk();
        Message message = sendResponse.message();
        if (message != null) {
            LOG.debug(message.toString());
        }
        return ok;
    }

    public void replyAsync(long chatId, int replyToId, String text, Callback<SendMessage, SendResponse> callback) {
        client.getBot().execute(writeReply(chatId, replyToId, text), callback);
    }

    private SendMessage write(long chatId, String text) {
        return new SendMessage(chatId, text)
                .parseMode(ParseMode.HTML);
    }

    private SendMessage writeReply(long chatId, int replyToId, String text) {
        return write(chatId, text).replyToMessageId(replyToId);
    }
}

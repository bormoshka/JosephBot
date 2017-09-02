package ru.ulmc.telegram.core.service;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ulmc.telegram.core.exception.UnknownCommandException;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class CommandsRouter {
    private final CommandService service;
    private final MessageService messageService;
    private Map<String, Function<Message, Boolean>> commands = new HashMap<>();
    private Map<String, String> description = new HashMap<>();

    @Autowired
    public CommandsRouter(CommandService service, MessageService messageService) {
        this.service = service;
        this.messageService = messageService;
    }

    @PostConstruct
    private void init() {
        createCommand("/myStats", "Prints your's stats", service::getStats);
        createCommand("/chatStats", "Prints chat's stats", service::getChatStats);
        createCommand("/help", "Available commands", this::help);
    }

    private void createCommand(String command, String desc, Function<Message, Boolean> function) {
        commands.put(command, function);
        description.put(command, desc);
    }

    public Boolean route(String cmd, Update update) throws UnknownCommandException {
        Function<Message, Boolean> function = commands.get(cmd);
        if (function == null) {
            throw new UnknownCommandException(cmd);
        }
        return function.apply(update.message());
    }

    public Boolean help(Message msg) {
        StringBuilder sb = new StringBuilder("Available commands:\n");
        commands.forEach((s, updateBooleanFunction) -> {
            sb.append("<b>")
                    .append(s)
                    .append("</b> ")
                    .append(description.get(s)).append("\n");
        });
        messageService.send(msg.chat().id(), sb.toString());
        return true;
    }
}

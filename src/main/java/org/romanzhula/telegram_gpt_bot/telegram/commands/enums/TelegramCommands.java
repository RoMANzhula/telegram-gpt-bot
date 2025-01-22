package org.romanzhula.telegram_gpt_bot.telegram.commands.enums;

public enum TelegramCommands {

    CLEAR_COMMAND("/clear"),
    HELP_COMMAND("/help"),
    START_COMMAND("/start")
    ;

    private final String value;

    TelegramCommands(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}

package br.com.emanuelgabriel.notbot2.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "prop.telegram")
public class TelegramProperties {

    private String chatId;
    private String botToken;
    private String botUsername;


    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getBotToken() {
        return botToken;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    public String getBotUsername() {
        return botUsername;
    }

    public void setBotUsername(String botUsername) {
        this.botUsername = botUsername;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TelegramProperties{");
        sb.append("chatId='").append(chatId).append('\'');
        sb.append(", botToken='").append(botToken).append('\'');
        sb.append(", botUsername='").append(botUsername).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

package br.com.emanuelgabriel.notbot2.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "prop.telegram")
@Getter
@Setter
public class TelegramProperties {

    private String chatId;
    private String botToken;
    private String botUsername;

}

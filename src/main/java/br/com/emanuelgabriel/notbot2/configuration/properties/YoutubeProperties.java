package br.com.emanuelgabriel.notbot2.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "prop.youtube")
@Getter
@Setter
public class YoutubeProperties {

    private String feedUrl;
    private String channelId;


}

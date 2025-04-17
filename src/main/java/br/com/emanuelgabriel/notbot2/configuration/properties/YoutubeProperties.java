package br.com.emanuelgabriel.notbot2.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "prop.youtube")
public class YoutubeProperties {

    private String feedUrl;
    private String channelId;

    public String getFeedUrl() {
        return feedUrl;
    }

    public void setFeedUrl(String feedUrl) {
        this.feedUrl = feedUrl;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("YoutubeProperties{");
        sb.append("feedUrl='").append(feedUrl).append('\'');
        sb.append(", channelId='").append(channelId).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

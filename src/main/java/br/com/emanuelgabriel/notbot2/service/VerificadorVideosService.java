package br.com.emanuelgabriel.notbot2.service;

import br.com.emanuelgabriel.notbot2.configuration.properties.YoutubeProperties;
import br.com.emanuelgabriel.notbot2.model.NotificadorDiscord;
import br.com.emanuelgabriel.notbot2.model.NotificadorTelegram;
import br.com.emanuelgabriel.notbot2.storage.FileStorage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class VerificadorVideosService {

    private static final Logger LOGGER = Logger.getLogger(VerificadorVideosService.class.getName());
    private static final Set<String> notificaVideos = FileStorage.carregarIds();

    private final NotificadorTelegram notificadorTelegram;
    private final NotificadorDiscord notificadorDiscord;
    private final YoutubeProperties youtubeProperties;

    public VerificadorVideosService(NotificadorTelegram notificadorTelegram, NotificadorDiscord notificadorDiscord, YoutubeProperties youtubeProperties) {
        this.notificadorTelegram = notificadorTelegram;
        this.notificadorDiscord = notificadorDiscord;
        this.youtubeProperties = youtubeProperties;
    }

    public void verificarNovosVideosDaSala57() {
        try {

            // Conectar ao feed RSS e obter o documento
            var url = youtubeProperties.getFeedUrl().concat("=").concat(youtubeProperties.getChannelId());

            var doc = Jsoup.connect(url).get();

            var entry = doc.selectFirst("entry");
            if (entry == null) {
                LOGGER.info("Nenhum vídeo encontrado no feed RSS.");
                return;
            }

            // Extrair informações do vídeo
            String videoId = Objects.requireNonNull(entry.selectFirst("id")).text();
            String title = Objects.requireNonNull(entry.selectFirst("title")).text();
            String link = Objects.requireNonNull(entry.selectFirst("link")).attr("href");
            String publishedDate = Objects.requireNonNull(entry.selectFirst("published")).text();

            var publishedDateTime = OffsetDateTime.parse(publishedDate).toLocalDateTime();
            var dataPublicacao = publishedDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            if (!notificaVideos.contains(videoId)) {
                LOGGER.info(String.format("Novo vídeo encontrado: %s (Publicado em: %s)", title, dataPublicacao));

                // Enviar notificação via Telegram
                notificadorTelegram.enviarMensagem(title, link, dataPublicacao);

                // Enviar notificação via Discord
                notificadorDiscord.enviarMensagem(title, link, dataPublicacao);

                notificaVideos.add(videoId);

                FileStorage.salvarIds(notificaVideos);

            } else {
                LOGGER.info(String.format("Nenhum novo vídeo publicado na data %s.", dataPublicacao));
            }

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, String.format("Erro ao buscar feed RSS: %s", e.getMessage()));
        }
    }
}


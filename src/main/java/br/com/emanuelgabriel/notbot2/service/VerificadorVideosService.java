package br.com.emanuelgabriel.notbot2.service;

import br.com.emanuelgabriel.notbot2.configuration.properties.YoutubeProperties;
import br.com.emanuelgabriel.notbot2.model.NotificadorDiscord;
import br.com.emanuelgabriel.notbot2.model.NotificadorTelegram;
import br.com.emanuelgabriel.notbot2.storage.FileStorage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
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

            var url = youtubeProperties.getFeedUrl().concat("=").concat(youtubeProperties.getChannelId());
            var doc = Jsoup.connect(url).get();

            var entries = doc.select("entry");
            if (entries.isEmpty()) {
                LOGGER.info("Nenhum vídeo encontrado no feed RSS.");
                return;
            }

            var maisRecenteComViews = entries.stream()
                    .filter(entry -> getViews(entry) > 0) // filtra só os vídeos quem tem/possui views > 0. Ou seja, descarta todos os vídeos sem visualizações.
                    .flatMap(entry -> getTextFromTag(entry, "published").stream().map(published -> Map.entry(entry, OffsetDateTime.parse(published)))) // Se não tiver "published", simplesmente ignora
                    .max(Map.Entry.comparingByValue()) // Busca o entry cujo OffsetDateTime (data publicada) é o mais recente (.max). Ou seja, compara as datas e pega a maior.
                    .map(Map.Entry::getKey)
                    .orElse(null);

            if (maisRecenteComViews == null) {
                LOGGER.warning("Nenhum vídeo com data de publicação válida encontrado.");
                return;
            }

            try {

                // Extrair informações
                String videoId = getTextFromTag(maisRecenteComViews, "id").orElse(null);
                String title = getTextFromTag(maisRecenteComViews, "title").orElse("Título indisponível");
                String link = getAttrFromTag(maisRecenteComViews).orElse(null);
                String publishedDate = getTextFromTag(maisRecenteComViews, "published").orElse(null);

                if (videoId == null || link == null || publishedDate == null) {
                    LOGGER.warning(String.format("Dados incompletos para o vídeo: %s. Ignorando...", title));
                    return;
                }

                var publishedDateTime = OffsetDateTime.parse(publishedDate);
                var dataPublicacao = publishedDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                if (!notificaVideos.contains(videoId)) {
                    LOGGER.info(String.format("Novo vídeo encontrado: %s (Publicado em: %s)", title, dataPublicacao));

                    notificadorTelegram.enviarMensagem(title, link, dataPublicacao);
                    notificadorDiscord.enviarMensagem(title, link, dataPublicacao);

                    notificaVideos.add(videoId);
                    FileStorage.salvarIds(notificaVideos);

                } else {
                    LOGGER.info(String.format("Vídeo já notificado anteriormente: %s - (Data/Publicação: %s)", title, dataPublicacao));
                }

            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Erro ao processar vídeo: " + e.getMessage(), e);
            }

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, String.format("Erro ao buscar feed RSS: %s", e.getMessage()), e);
        }
    }

    /**
     * Obtém texto de uma tag de forma segura.
     */
    private Optional<String> getTextFromTag(Element parent, String tagName) {
        var element = parent.selectFirst(tagName);
        if (element != null) {
            var text = element.text();
            if (!text.isBlank()) {
                return Optional.of(text);
            }
        }
        return Optional.empty();
    }

    /**
     * Obtém atributo de uma tag de forma segura.
     */
    private Optional<String> getAttrFromTag(Element parent) {
        var element = parent.selectFirst("link");
        if (element != null) {
            var attrValue = element.attr("href");
            if (!attrValue.isBlank()) {
                return Optional.of(attrValue);
            }
        }
        return Optional.empty();
    }

    /**
     * Extrai número de views com tratamento de exceções.
     */
    private long getViews(Element entry) {
        try {
            var statisticsElement = entry.selectFirst("media|statistics");
            if (statisticsElement != null) {
                var viewsAttr = statisticsElement.attr("views");
                if (!viewsAttr.isBlank()) {
                    return Long.parseLong(viewsAttr);
                }
            } else {
                LOGGER.info("Tag <media:statistics> não encontrada para vídeo.");
            }
        } catch (NumberFormatException e) {
            LOGGER.warning("Número de views inválido: " + e.getMessage());
        }
        return 0L;
    }

}




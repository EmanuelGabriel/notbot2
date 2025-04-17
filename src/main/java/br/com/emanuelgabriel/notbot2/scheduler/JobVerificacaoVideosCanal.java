package br.com.emanuelgabriel.notbot2.scheduler;


import br.com.emanuelgabriel.notbot2.model.NotificadorTelegram;
import org.jsoup.Jsoup;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Emanuel Gabriel
 * Classe responsável por verificar novos vídeos no canal do YouTube.
 */
@Component
public class JobVerificacaoVideosCanal {

    private static final Logger LOGGER = Logger.getLogger(JobVerificacaoVideosCanal.class.getName());
    private static final Set<String> notificaVideos = new HashSet<>();

    @Scheduled(fixedRate = 60000)
    public void verificarNovosVideos() {
        LOGGER.info("Job/Início -> iniciando verificação de novos vídeos...");

        verificarNovosVideosDaSala57();

        LOGGER.info("Job/Fim -> verificação de novos vídeos concluída com sucesso...");
    }

    private static void verificarNovosVideosDaSala57() {
        try {

            final String channelId = "UCaAxyxJLvQ5kKoQoV5O1cuw";

            // Conectar ao feed RSS e obter o documento
            var doc = Jsoup.connect("https://www.youtube.com/feeds/videos.xml?channel_id=".concat(channelId)).get();

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
                LOGGER.info(String.format("Novo vídeo encontrado: %s (Publicado em: %s) %s", title, dataPublicacao, link));

                // Enviar notificação via Telegram
                var notificador = new NotificadorTelegram();
                notificador.enviarMensagem(title, link, dataPublicacao);

                notificaVideos.add(videoId);
            } else {
                LOGGER.info(String.format("Nenhum novo vídeo publicado na data %s.", dataPublicacao));
            }

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, String.format("Erro ao buscar feed RSS: %s", e.getMessage()));
        }
    }

}

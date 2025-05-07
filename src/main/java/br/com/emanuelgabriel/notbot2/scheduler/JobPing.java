package br.com.emanuelgabriel.notbot2.scheduler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Emanuel Gabriel
 */
@Component
public class JobPing {

    private static final Logger LOGGER = Logger.getLogger(JobPing.class.getName());
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Value("${prop.pingUrl}")
    private String PING_URL;

    /**
     * Método responsável por enviar um ping para o servidor a cada 45 segundos.
     */
    @Scheduled(fixedRate = 45000)
    public void ping() {
        LOGGER.info("Job/Ping -> inicializado...");

        pingToServer();

        LOGGER.info("Job/Ping -> finalizado...");
    }

    private void pingToServer() {
        try {

            var request = HttpRequest.newBuilder()
                    .uri(URI.create(PING_URL))
                    .GET()
                    .build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            LOGGER.log(Level.INFO, "✅ ping enviado com sucesso! Resposta: {0}", response.body());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "❌ Erro ao enviar ping: {0}", e.getMessage());
        }
    }
}

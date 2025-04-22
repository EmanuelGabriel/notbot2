package br.com.emanuelgabriel.notbot2;

import br.com.emanuelgabriel.notbot2.configuration.properties.TelegramProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@SpringBootTest
class Notbot2ApplicationTests {

    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final String CONTENT_TYPE = "application/json";

    @Autowired
    private TelegramProperties telegramProperties;

    @Test
    void obterChatIdTelegram() {

        try {

            var url = String.format("https://api.telegram.org/bot%s/getUpdates", telegramProperties.getBotToken());
            var request = HttpRequest.newBuilder()
                    .uri(URI.create(url)).header("Content-Type", CONTENT_TYPE)
                    .GET()
                    .build();

            // Enviar a requisição e obter a resposta
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("Resposta: " + response.body());
            } else {
                System.err.println("Erro ao obter chatId: " + response.statusCode() + " - " + response.body());
            }


        } catch (Exception e) {
            System.err.println("Erro ao fazer a requisição: " + e.getMessage());
        }


    }
}



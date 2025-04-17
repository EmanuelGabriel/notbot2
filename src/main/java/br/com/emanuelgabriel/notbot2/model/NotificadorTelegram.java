package br.com.emanuelgabriel.notbot2.model;


import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Emanuel Gabriel
 * Classe responsável por enviar notificações via Telegram.
 */
public class NotificadorTelegram extends TelegramLongPollingBot implements Notificador {

    private static final Logger LOGGER = Logger.getLogger(NotificadorTelegram.class.getName());

    public NotificadorTelegram() {
    }

    @Override
    public void enviarMensagem(String titulo, String link, final String dataPublicacao) {
        try {

            var texto = String.format("Novo vídeo: **%s** (Publicado em: %s) 🔗 %s", titulo, dataPublicacao, link);

            var sendMessage = new SendMessage();
            sendMessage.setChatId("30888055");
            sendMessage.setText(texto);
            sendMessage.enableMarkdown(true);

            execute(sendMessage);

            LOGGER.log(Level.INFO, "Mensagem enviada com sucesso para o Telegram.");

        } catch (TelegramApiException e) {
            LOGGER.log(Level.SEVERE, "Erro: {0}", e.getMessage());
        }
    }

    /**
     * Return username of this bot
     */
    @Override
    public String getBotUsername() {
        return "TesteSala57";
    }

    /**
     * Returns the token of the bot to be able to perform Telegram Api Requests
     *
     * @return Token of the bot
     */
    @Override
    public String getBotToken() {
        return "5632032958:AAEwkfO5MwrkfljMRzOD7UJRRMMVeWvlK-c";
    }

    /**
     * This method is called when receiving updates via GetUpdates method
     *
     * @param update Update received
     */
    @Override
    public void onUpdateReceived(Update update) {
        // Método chamado quando uma atualização é recebida
        if (update.hasMessage() && update.getMessage().hasText()) {
            var chatId = update.getMessage().getChatId();
            LOGGER.log(Level.INFO, "Mensagem recebida: {0}", chatId);
        }
    }
}

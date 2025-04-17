package br.com.emanuelgabriel.notbot2.model;

public interface Notificador {

    void enviarMensagem(String titulo, String link, final String dataPublicacao);

}


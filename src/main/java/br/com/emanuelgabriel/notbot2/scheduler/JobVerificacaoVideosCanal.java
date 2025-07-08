package br.com.emanuelgabriel.notbot2.scheduler;


import br.com.emanuelgabriel.notbot2.service.VerificadorVideosService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

/**
 * @author Emanuel Gabriel
 * Classe responsável por verificar novos vídeos no canal do YouTube.
 */
@Component
public class JobVerificacaoVideosCanal {

    private static final Logger LOGGER = Logger.getLogger(JobVerificacaoVideosCanal.class.getName());

    private final VerificadorVideosService verificadorVideosService;

    public JobVerificacaoVideosCanal(VerificadorVideosService verificadorVideosService) {
        this.verificadorVideosService = verificadorVideosService;
    }

    @Scheduled(cron = "${prop.cron.time}", zone = "America/Sao_Paulo")
    public void verificarNovosVideos() {
        LOGGER.info("Job/Inicializado -> iniciando verificação de novos vídeos...");

        verificadorVideosService.verificarNovosVideosDaSala57();

        LOGGER.info("Job/Finalizado -> verificação de novos vídeos concluída com sucesso...");
    }

}

package br.com.emanuelgabriel.notbot2.controller;

import br.com.emanuelgabriel.notbot2.dto.response.HealthDTO;
import br.com.emanuelgabriel.notbot2.dto.response.MensagemDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Emanuel Gabriel
 */

@RestController
@RequestMapping(value = "/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class IndexController {

    private static final Logger logger = Logger.getLogger(IndexController.class.getName());

    @GetMapping
    public ResponseEntity<?> index() {
        logger.log(Level.INFO, "GET /v1/");
        return ResponseEntity.ok(new MensagemDTO("UP! NotBot2 API"));
    }

    @GetMapping("/health")
    public ResponseEntity<HealthDTO> health() {
        logger.log(Level.INFO, "GET /v1/health");
        return ResponseEntity.ok(new HealthDTO());
    }

}

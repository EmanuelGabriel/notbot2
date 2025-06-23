package br.com.emanuelgabriel.notbot2.controller;

import br.com.emanuelgabriel.notbot2.dto.response.MensagemDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Emanuel Gabriel
 */

@RestController
@RequestMapping(value = "/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class IndexController {

    @GetMapping
    public ResponseEntity<?> index() {
        return ResponseEntity.ok(new MensagemDTO("UP! NotBot2 API"));
    }


}

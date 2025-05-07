package br.com.emanuelgabriel.notbot2.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class HealthDTO {

    private String mensagem;
    private String status;

    public HealthDTO() {
        this.mensagem = "OK";
        this.status = HttpStatus.OK.toString();
    }

}

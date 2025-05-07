package br.com.emanuelgabriel.notbot2.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HealthDTO {

    private String mensagem;
    private String status;

}

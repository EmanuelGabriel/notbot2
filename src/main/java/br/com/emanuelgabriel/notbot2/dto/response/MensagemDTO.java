package br.com.emanuelgabriel.notbot2.dto.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MensagemDTO {

    private String mensagem;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime dataHora = LocalDateTime.now();

    public MensagemDTO(String mensagem) {
        this.mensagem = mensagem;
    }

    /**
     * @param erros
     * @return List<MensagemApiResponseDTO>
     */
    public static List<MensagemDTO> paraLista(BindingResult erros) {
        if (erros.hasErrors()) {
            List<MensagemDTO> mensagens = new ArrayList<>();
            erros.getAllErrors().forEach((e) -> {
                mensagens.add(new MensagemDTO(e.getDefaultMessage()));
            });
            return mensagens;
        }
        return new ArrayList<>();
    }


}

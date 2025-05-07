package br.com.emanuelgabriel.notbot2.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class HealthDTO {

    private String status;
    private String environment;
    private String version;

    public HealthDTO(Environment environment) {
        this.status = HttpStatus.OK.toString();
        this.environment = environment.getProperty("spring.profiles.active");
        this.version = environment.getProperty("app.version");
    }

    public HealthDTO() {
        this.status = HttpStatus.OK.toString();
    }

}

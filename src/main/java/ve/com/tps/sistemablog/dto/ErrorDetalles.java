package ve.com.tps.sistemablog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
public class ErrorDetalles {

    private LocalDateTime marcaTiempo;
    private String mensaje;
    private String detalles;


}

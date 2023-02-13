package ve.com.tps.sistemablog.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class ComentarioDTO {

    private Integer id;

    @NotEmpty(message = "El cuerpo no debe ser vacio o nulo")
    @Size(min = 10, message = "El cuerpo del comentario debe tener al menos 10 caracteres" )
    private String cuerpo;

    @NotEmpty(message = "El email no debe ser vacio o nulo")
    @Email
    private String email;

    @NotEmpty(message = "El nombre no debe ser vacio o nulo")
    private String nombre;

    private PublicacionDTO publicacion;
}

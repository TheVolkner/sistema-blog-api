package ve.com.tps.sistemablog.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import ve.com.tps.sistemablog.entities.Comentarios;

import java.util.Set;

@Data
public class PublicacionDTO {

    private Integer id;

    @NotEmpty(message = "El titulo no debe ser vacio o nulo")
    @Size(min = 2,message = "El titulo debe tener al menos 2 caracteres.")
    private String titulo;

    @NotEmpty(message = "La descripcion no debe ser vacia o nula")
    @Size(min = 10,message = "La descripcion debe tener al menos 10 caracteres.")
    private String descripcion;

    @NotEmpty(message = "El contenido no debe ser vacio o nulo")
    @Size(min = 20,message = "El contenido debe tener al menos 20 caracteres.")
    private String contenido;

    private Set<Comentarios> comentarios;


}

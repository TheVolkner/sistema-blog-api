package ve.com.tps.sistemablog.dto;

import lombok.Data;

import java.util.List;

@Data
//ESTE DTO ES UNA HERRAMIENTA PARA MANEJAR PAGINACION DE MANERA CÓMODA Y EFICAZ, VA DE LA MANO CON
//EL METODO DE LISTAR EN SERVICES.
public class PagePublicacion {

    private List<PublicacionDTO> contenido;
    private Integer nroPagina;
    private Integer medidaPagina;
    private Long totalElementos;
    private Integer totalPaginas;
    private Boolean ultima;
}

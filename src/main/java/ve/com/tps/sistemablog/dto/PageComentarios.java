package ve.com.tps.sistemablog.dto;


import lombok.Data;

import java.util.List;

@Data
public class PageComentarios {

    private List<ComentarioDTO> contenido;
    private Integer nroPagina;
    private Integer medidaPagina;
    private Long totalElementos;
    private Integer totalPaginas;
    private Boolean ultima;
}

package ve.com.tps.sistemablog.services;

import org.springframework.data.domain.Page;
import ve.com.tps.sistemablog.dto.PagePublicacion;
import ve.com.tps.sistemablog.dto.PublicacionDTO;

import java.util.List;

public interface PublicacionServicio {

    public PublicacionDTO crearPublicacion(PublicacionDTO publicacion);

    public PagePublicacion listarPublicaciones(Integer numeroPagina, Integer medidaPagina,String ordenarPor,String ordenDir);

    public PublicacionDTO buscarPublicacionPorId(Integer id);

    public PublicacionDTO modificarPublicacion(PublicacionDTO publicacion);

    public void eliminarPublicacion(PublicacionDTO publicacionDTO);
}

package ve.com.tps.sistemablog.services;

import ve.com.tps.sistemablog.dto.ComentarioDTO;
import ve.com.tps.sistemablog.dto.PageComentarios;
import ve.com.tps.sistemablog.dto.PublicacionDTO;
import ve.com.tps.sistemablog.entities.Comentarios;
import ve.com.tps.sistemablog.entities.Publicacion;

import java.util.List;

public interface ComentariosServicio {

    public ComentarioDTO agregarComentario(ComentarioDTO comentarios, PublicacionDTO publicacionDTO);

    public void eliminarComentarioPorId(Integer id);

    public ComentarioDTO modificarComentario(ComentarioDTO comentarios,PublicacionDTO publicacionDTO);

    public PageComentarios listarComentariosDePublicacion(Integer id, Integer numeroPagina, Integer medidaPagina, String ordenarPor, String ordenDir);

    public ComentarioDTO buscarComentarioPorIdYPublicacionId(Integer id,Integer idPublicacion);
}

package ve.com.tps.sistemablog.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ve.com.tps.sistemablog.entities.Comentarios;

import java.util.List;

public interface ComentariosRepository extends JpaRepository<Comentarios,Integer> {

    //LISTAR SEGUN EL ID DE LA PUBLICACION DE LOS COMENTARIOS
    public List<Comentarios> findByPublicacionId(Integer publicacionId);

    //LISTAR UN COMENTARIO SEGUN EL ID DE SU PUBLICACION Y EL SUYO PROPIO
    public Comentarios findByIdAndPublicacionId(Integer id,Integer publicacionId);
}

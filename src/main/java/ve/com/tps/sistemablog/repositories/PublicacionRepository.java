package ve.com.tps.sistemablog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ve.com.tps.sistemablog.entities.Publicacion;

public interface PublicacionRepository extends JpaRepository<Publicacion,Integer> {


}

package ve.com.tps.sistemablog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ve.com.tps.sistemablog.entities.Roles;

import java.util.Optional;

public interface RolesRepository extends JpaRepository<Roles,Integer> {

    public Optional<Roles> findByNombre(String nombre);
}

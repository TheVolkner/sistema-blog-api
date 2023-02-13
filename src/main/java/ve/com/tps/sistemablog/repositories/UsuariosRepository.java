package ve.com.tps.sistemablog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ve.com.tps.sistemablog.entities.Usuarios;

import java.util.Optional;

public interface UsuariosRepository extends JpaRepository<Usuarios,Integer> {

    public Optional<Usuarios> findByUsername(String username);

    public Optional<Usuarios> findByEmail(String email);

    public Optional<Usuarios> findByUsernameOrEmail(String username,String email);

    public Boolean existsByUsername(String username);

    public Boolean existsByEmail(String email);
}


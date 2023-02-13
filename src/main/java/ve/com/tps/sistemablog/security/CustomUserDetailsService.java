package ve.com.tps.sistemablog.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ve.com.tps.sistemablog.entities.Roles;
import ve.com.tps.sistemablog.entities.Usuarios;
import ve.com.tps.sistemablog.repositories.UsuariosRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        Optional<Usuarios> usuariosOptional = usuariosRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);

        if(usuariosOptional.isEmpty()){

            throw new UsernameNotFoundException("Usuario no encontrado con el usuario o email proporcionado : " + usernameOrEmail);
        }

        return new User(usuariosOptional.get().getEmail(),usuariosOptional.get().getPassword(),mapearRoles(usuariosOptional.get().getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapearRoles(Set<Roles> roles){

        return roles.stream().map(rol -> new SimpleGrantedAuthority(rol.getNombre())).collect(Collectors.toList());
    }
}

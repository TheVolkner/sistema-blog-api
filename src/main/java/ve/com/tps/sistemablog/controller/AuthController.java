package ve.com.tps.sistemablog.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ve.com.tps.sistemablog.dto.LoginDTO;
import ve.com.tps.sistemablog.dto.RegistroDTO;
import ve.com.tps.sistemablog.entities.Roles;
import ve.com.tps.sistemablog.entities.Usuarios;
import ve.com.tps.sistemablog.repositories.RolesRepository;
import ve.com.tps.sistemablog.repositories.UsuariosRepository;
import ve.com.tps.sistemablog.security.JWTAuthResponseDTO;
import ve.com.tps.sistemablog.security.JwtTokenProvider;

import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("api/auth")
@Slf4j
public class AuthController {



    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/iniciarsesion")
    private ResponseEntity<JWTAuthResponseDTO> authenticateUser(@RequestBody LoginDTO loginDTO){

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsernameOrEmail(),loginDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.info("Generando token");

        String token = jwtTokenProvider.generarToken(authentication);

        log.info("Token generado exitosamente");

        return ResponseEntity.ok(new JWTAuthResponseDTO(token));
    }

    @PostMapping("/registrar")
    private ResponseEntity<String> registerUser(@RequestBody RegistroDTO registroDTO){

        if(usuariosRepository.existsByEmail(registroDTO.getEmail())){

            return new ResponseEntity<>("El email del usuario a registrar ya existe.", HttpStatus.BAD_REQUEST);
        }

        if(usuariosRepository.existsByUsername(registroDTO.getUsername())){

            return new ResponseEntity<>("El username del usuario a registrar ya existe.", HttpStatus.BAD_REQUEST);
        }

        Usuarios u = new Usuarios();
        u.setEmail(registroDTO.getEmail());
        u.setNombre(registroDTO.getNombre());
        u.setUsername(registroDTO.getUsername());
        u.setPassword(passwordEncoder.encode(registroDTO.getPassword()));

        Optional<Roles> rolesOptional = rolesRepository.findByNombre("ROLE_ADMIN");

        if(rolesOptional.isEmpty()){

            return new ResponseEntity<>("El rol a agregar no existe.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        u.setRoles(Collections.singleton(rolesOptional.get()));

        usuariosRepository.save(u);

        return new ResponseEntity<>("Usuario registrado exitosamente",HttpStatus.CREATED);
    }
}

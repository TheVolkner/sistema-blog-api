package ve.com.tps.sistemablog.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//ESTA CLASE SE ENCARGA DE VALIDAR QUE LOS DATOS DEL TOKEN JWT COINCIDAN CON LOS DEL CLIENTE LOGEADO
// EN SPRING SECURITY
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    //ACCEDEMOS A LOS DATOS DEL USUARIO LOGEADO
    @Autowired
    private CustomUserDetailsService userDetailsService;

    //ACCEDEMOS AL TOKEN CREADO
    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    @Override
    //ESTE METODO SE ENCARGA DE VALIDAR EL TOKEN Y COMPROBAR QUE EXISTE UN USUARIO ASOCIADO AL MISMO,
    // PARA AGREGAR LA SESION A SPRING SECURITY
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        //Obtenemos el token
        String token = getJWTFromRequest(request);

        //Validamos el token
        if(StringUtils.hasText(token) && jwtTokenProvider.validarToken(token)){

            //Obtener el username asociado al token (Subject)
            String username = jwtTokenProvider.obtenerUsername(token);

            //Cargamos el usuario asociado al token desde BBDD
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            //Creamos el token de sesion asociado a ese usuario
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            //Establecemos la seguridad
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request,response);
    }

    //Bearer Token de Acceso.
    private String getJWTFromRequest(HttpServletRequest request){

        String bearerToken = request.getHeader("Authorization");

        if(StringUtils.hasLength(bearerToken) && bearerToken.startsWith("Bearer")){

            return bearerToken.substring(7,bearerToken.length());
        }
        return null;
    }
}

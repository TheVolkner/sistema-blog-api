package ve.com.tps.sistemablog.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

//ESTA CLASE SE ENCARGARA DE MANEJAR LOS ERRORES ASOCIADOS A LOS TOKEN DE JWT.
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    //SOBREESCRIBIMOS ESTE METODO DE AuthenticationEntryPoint PARA INDICAR UN MENSAJE INDICANDO LA EXCEPCION.
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED,authException.getMessage());
    }
}

package br.insper.projeto.pf;

import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import br.insper.projeto.pf.UserResponse;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TokenFilter extends OncePerRequestFilter {

    private static final String VALIDATE_URL = "http://184.72.80.215/usuario/validate";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = request.getHeader("Authorization");

        if (token != null && !token.isEmpty()) {
            // Valida o token chamando o serviço externo
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            try {
                ResponseEntity<UserResponse> userResponse = restTemplate.exchange(
                        VALIDATE_URL,
                        HttpMethod.GET,
                        entity,
                        UserResponse.class);

                if (userResponse.getStatusCode() == HttpStatus.OK) {
                    UserResponse user = userResponse.getBody();

                    // Armazena as informações do usuário na requisição
                    request.setAttribute("user", user);
                } else {
                    // Token inválido ou expirado
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido ou expirado.");
                    return;
                }

            } catch (Exception e) {
                // Erro na chamada ao serviço externo
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Erro ao validar o token.");
                return;
            }
        } else {
            // Token não fornecido
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token não fornecido.");
            return;
        }

        // Continua a cadeia de filtros
        filterChain.doFilter(request, response);
    }
}

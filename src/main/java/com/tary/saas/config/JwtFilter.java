package com.tary.saas.config;

import com.tary.saas.entity.User;
import com.tary.saas.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        System.out.println("JwtFilter - Iniciou filtro para " + request.getRequestURI());

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            System.out.println("JwtFilter - Token recebido: " + token);

            if (jwtUtil.isTokenValid(token)) {
                System.out.println("JwtFilter - Token válido!");
                Claims claims = jwtUtil.extractClaims(token);
                String email = claims.getSubject();
                System.out.println("JwtFilter - Email extraído: " + email);

                User user = userRepository.findByEmail(email).orElse(null);
                if (user != null) {
                    System.out.println("JwtFilter - Usuário encontrado: " + user.getEmail());
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    request.setAttribute("companyId", user.getCompanyId());
                } else {
                    System.out.println("JwtFilter - Usuário não encontrado no banco!");
                }
            } else {
                System.out.println("JwtFilter - Token inválido!");
            }
        } else {
            System.out.println("JwtFilter - Header Authorization ausente ou inválido");
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        System.out.println("JwtFilter - Avaliando shouldNotFilter para: " + path);
        return path.startsWith("/api/auth")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-resources")
                || path.startsWith("/webjars")
                || path.equals("/swagger-ui.html");
    }

}

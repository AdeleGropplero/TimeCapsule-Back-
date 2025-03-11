package com.capstone.TimeCapsule.Security.JWT;

import com.capstone.TimeCapsule.Exception.UnauthorizedException;
import com.capstone.TimeCapsule.Model.Utente;
import com.capstone.TimeCapsule.Service.UtenteService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    JwtUtil jwtUtil;

    private final UtenteService utenteService;

    @Autowired
    public JwtAuthFilter(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, UnauthorizedException {
        String authToken = request.getHeader("Authorization");

        String path = request.getServletPath();

        // Ignora il controllo del token per le rotte pubbliche
        if (path.equals("/register") || path.equals("/")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (authToken == null || !authToken.startsWith("Bearer ")){
            throw new UnauthorizedException("Perfavore manda un token valido");
        }

        //Puliamo il token recuperato dalla auth.
        String token = authToken.substring(7);

        //verifichiamo la validità del token
        jwtUtil.verifyToken(token);

        // Recuperiamo l'id (subject) dal nostro token e recuperiamo l'utente associato
        String id = jwtUtil.decodeToken(token);
        Utente utente =utenteService.findById(UUID.fromString(id));

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(utente, null, utente.getAuthorities());
        System.out.println(auth); //da togliere successivamente 🛑
        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);

    }
}

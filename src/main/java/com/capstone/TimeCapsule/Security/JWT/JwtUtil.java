package com.capstone.TimeCapsule.Security.JWT;

import com.capstone.TimeCapsule.Exception.UnauthorizedException;
import com.capstone.TimeCapsule.Model.Utente;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    // La chiave segreta per firmare il JWT, letta dal file di configurazione
    @Value("${jwt.secret}")
    private String jwtSecret;

    // La durata di validità del token, letta dal file di configurazione
    @Value("${jwt.expiration}")
    private String jwtExpiration;

    /**
     * Metodo per generare un token JWT basato sull'utente fornito.
     * @param utente Oggetto Utente che contiene le informazioni dell'utente per creare il token.
     * @return Token JWT generato
     */
    public String generateToken(Utente utente) {
        // Converte jwtExpiration (che è una stringa) in long
        long expirationTimeInMillis = Long.parseLong(jwtExpiration);

        // Creazione del token JWT con le informazioni dell'utente
        String token = Jwts.builder()
                .setSubject(utente.getId().toString())  // Il subject del token è l'id dell'utente, non metto l'email per non esporla.
                .claim("role", utente.getRuolo().getNome().name()) // Aggiungiamo il ruolo dell'utente al token come claim
                .setIssuedAt(new Date())  // Impostiamo la data di emissione del token
                .setExpiration(new Date(System.currentTimeMillis() + expirationTimeInMillis))  // Impostiamo la data di scadenza del token
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()), SignatureAlgorithm.HS256)  // Firma del token con la chiave segreta e l'algoritmo HS256
                .compact();  // Genera il token finale
        return token;
    }

    /**
     * Metodo per verificare se un token è valido.
     * @param token Il token JWT da verificare
     * @throws UnauthorizedException Se il token non è valido o è scaduto
     */
    public void verifyToken(String token) throws UnauthorizedException {
        try {
            // Verifica del token. Se è valido, non succede nulla, altrimenti si genera un'eccezione
            Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes())).build().parse(token);
        } catch (Exception e) {
            // Se il token è scaduto o non valido, viene lanciata una UnauthorizedException
            throw new UnauthorizedException("Token non valido o scaduto!");
        }
    }

    /**
     * Metodo per decodificare il token JWT e ottenere il subject (ad esempio, l'email dell'utente).
     * @param token Il token JWT da decodificare
     * @return L'email dell'utente contenuta nel token
     */
    public String decodeToken(String token) {
        // Decodifica il token e restituisce il subject, che in questo caso è l'id dell'utente
        return Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes())).build().parseClaimsJws(token)
                .getBody().getSubject();
    }
}

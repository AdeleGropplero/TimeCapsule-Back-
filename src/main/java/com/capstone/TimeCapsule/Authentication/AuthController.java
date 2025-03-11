package com.capstone.TimeCapsule.Authentication;

import com.capstone.TimeCapsule.Authentication.Request.LoginRequest;
import com.capstone.TimeCapsule.Authentication.Request.RegistrationRequest;
import com.capstone.TimeCapsule.Authentication.Response.LoginResponse;
import com.capstone.TimeCapsule.Exception.EmailDuplicateException;
import com.capstone.TimeCapsule.Exception.PassworErrataException;
import com.capstone.TimeCapsule.Exception.UnauthorizedException;
import com.capstone.TimeCapsule.Exception.UtenteNonTrovatoException;
import com.capstone.TimeCapsule.Model.Utente;
import com.capstone.TimeCapsule.Security.JWT.JwtUtil;
import com.capstone.TimeCapsule.Service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("")
@CrossOrigin(origins = {"http://localhost:5173/"})
public class AuthController {

    @Autowired
    UtenteService utenteService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    PasswordEncoder bcrypt;

    @PostMapping("/")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest body) throws UtenteNonTrovatoException, UnauthorizedException {
        Utente found = this.utenteService.findByEmail(body.getEmail());

        if (found == null) {
            throw new UtenteNonTrovatoException(body.getEmail());
        }

        if (bcrypt.matches(body.getPassword(), found.getPassword())) {
            String token = jwtUtil.generateToken(found);
            UUID id = found.getId();
            String fullName = found.getFullName();
            LoginResponse loginResponse = new LoginResponse(token, id, fullName);

            // Restituisce una risposta con il corpo e il codice di stato OK (200)
            return ResponseEntity.ok(loginResponse);
        } else {
            // Se la password non è corretta, restituisce un errore 401 Unauthorized
            throw new UnauthorizedException("Password errata!");
        }
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> registrazioneUtente(@RequestBody @Validated RegistrationRequest nuovoUtente, BindingResult validation) {

        if (validation.hasErrors()) {
            StringBuilder messaggio = new StringBuilder("Problemi nella validazione dei dati: \n");

            for (ObjectError error : validation.getAllErrors()) {
                messaggio.append(error.getDefaultMessage()).append("\n");
            }
            return new ResponseEntity<>(messaggio.toString(), HttpStatus.BAD_REQUEST);
        }
        try {
            String salvaUtenteMessaggio = utenteService.salvaUtente(nuovoUtente);

            return new ResponseEntity<>(salvaUtenteMessaggio, HttpStatus.CREATED);

        } catch (EmailDuplicateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/token-validation") //@RequestHeader È un'annotazione di Spring che permette di accedere alle intestazioni (headers) di una richiesta HTTP
    public ResponseEntity<String> tokenValidation(@RequestHeader("Authorization") String token) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7); // Rimuove "Bearer " dal token
            }

            // Verifico la validità del token
            jwtUtil.verifyToken(token);

            return ResponseEntity.ok("Token valido");
        } catch (UnauthorizedException e) {
            // In caso di token non valido o scaduto
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token non valido o scaduto");
        }
    }

}

package com.capstone.TimeCapsule.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "utenti", uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Utente implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, name = "full_name")
    private String fullName;

    @Column(nullable = false, name = "data_registrazione")
    private LocalDate dataRegistrazione;

    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ruolo_id")
    private Ruolo ruolo;

    @ManyToMany(mappedBy = "utenti", cascade = CascadeType.ALL)
    private List<Capsula> capsule = new ArrayList<>();

    @Column(length = 500) // URL può essere lungo
    private String avatar;

    public Utente(String fullName, String email, String password) {
        this.fullName = fullName;
        this.dataRegistrazione = LocalDate.now();
        this.email = email;
        this.password = password;
    }

    /*    // Per la gestione sicura della password, usando BCrypt
    @PrePersist
    public void encryptPassword() {
        this.password = new BCryptPasswordEncoder().encode(password);
    }*/

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.ruolo.getNome().name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String toString() {
        return "Utente{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", dataRegistrazione=" + dataRegistrazione +
                ", ruolo=" + ruolo +
                '}';
    }
}

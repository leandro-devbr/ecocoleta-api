package br.com.ecocoleta.ecocoleta.service;

import br.com.ecocoleta.ecocoleta.dto.auth.AuthResponse;
import br.com.ecocoleta.ecocoleta.dto.auth.LoginRequest;
import br.com.ecocoleta.ecocoleta.dto.auth.RegisterRequest;
import br.com.ecocoleta.ecocoleta.dto.auth.UsuarioResponse;
import br.com.ecocoleta.ecocoleta.exception.BusinessConflictException;
import br.com.ecocoleta.ecocoleta.models.PerfilUsuario;
import br.com.ecocoleta.ecocoleta.models.Usuario;
import br.com.ecocoleta.ecocoleta.repository.UsuarioRepository;
import br.com.ecocoleta.ecocoleta.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Transactional
    public UsuarioResponse register(RegisterRequest request) {
        String email = normalizeEmail(request.email());
        if (usuarioRepository.existsByEmailIgnoreCase(email)) {
            throw new BusinessConflictException("Já existe uma conta cadastrada com este e-mail.");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(request.nome().trim());
        usuario.setEmail(email);
        usuario.setSenha(passwordEncoder.encode(request.senha()));
        usuario.setPerfil(PerfilUsuario.USER);
        return toResponse(usuarioRepository.save(usuario));
    }

    public AuthResponse login(LoginRequest request) {
        String email = normalizeEmail(request.email());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, request.senha())
        );
        if (!(authentication.getPrincipal() instanceof UserDetails userDetails)) {
            throw new IllegalStateException("O provedor de autenticação retornou um usuário inválido.");
        }
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new IllegalStateException("Usuário autenticado não encontrado."));
        return new AuthResponse(
                jwtService.generateToken(userDetails),
                "Bearer",
                jwtService.getTokenLifetimeSeconds(),
                toResponse(usuario)
        );
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioResponse(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getPerfil());
    }
}

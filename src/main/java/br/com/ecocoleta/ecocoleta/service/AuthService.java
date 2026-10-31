package br.com.ecocoleta.ecocoleta.service;

import br.com.ecocoleta.ecocoleta.dto.LoginRequestDTO;
import br.com.ecocoleta.ecocoleta.dto.LoginResponseDTO;
import br.com.ecocoleta.ecocoleta.dto.UsuarioRequestDTO;
import br.com.ecocoleta.ecocoleta.dto.UsuarioResponseDTO;
import br.com.ecocoleta.ecocoleta.exceptions.RegraNegocioException;
import br.com.ecocoleta.ecocoleta.models.PerfilUsuario;
import br.com.ecocoleta.ecocoleta.models.Usuario;
import br.com.ecocoleta.ecocoleta.repository.UsuarioRepository;
import br.com.ecocoleta.ecocoleta.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public UsuarioResponseDTO cadastrar(UsuarioRequestDTO dto) {
        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new RegraNegocioException("Já existe um usuário com este e-mail");
        }
        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setSenha(passwordEncoder.encode(dto.senha()));
        usuario.setPerfil(PerfilUsuario.USUARIO); // o perfil nunca vem do cliente
        Usuario salvo = usuarioRepository.save(usuario);
        return new UsuarioResponseDTO(salvo.getId(), salvo.getNome(), salvo.getEmail(), salvo.getPerfil());
    }

    public LoginResponseDTO login(LoginRequestDTO dto) {
        // lança BadCredentialsException se o e-mail ou a senha estiverem errados
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.email(), dto.senha()));
        return new LoginResponseDTO(jwtService.gerarToken(dto.email()), "Bearer");
    }
}
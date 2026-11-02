package br.com.ecocoleta.ecocoleta.controller;

import br.com.ecocoleta.ecocoleta.dto.DescarteRequestDTO;
import br.com.ecocoleta.ecocoleta.dto.DescarteResponseDTO;
import br.com.ecocoleta.ecocoleta.service.DescarteService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/descartes")
public class DescarteController {

    private final DescarteService service;

    public DescarteController(DescarteService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<DescarteResponseDTO> registrar(@RequestBody @Valid DescarteRequestDTO dto,
                                                         Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.registrar(dto, authentication.getName()));
    }

    @GetMapping("/ponto/{pontoId}")
    public ResponseEntity<List<DescarteResponseDTO>> historico(@PathVariable Long pontoId) {
        return ResponseEntity.ok(service.historico(pontoId));
    }
}
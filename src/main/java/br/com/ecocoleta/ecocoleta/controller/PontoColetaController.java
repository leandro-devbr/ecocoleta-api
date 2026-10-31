package br.com.ecocoleta.ecocoleta.controller;

import br.com.ecocoleta.ecocoleta.dto.PontoColetaRequestDTO;
import br.com.ecocoleta.ecocoleta.dto.PontoColetaResponseDTO;
import br.com.ecocoleta.ecocoleta.models.PontoStatus;
import br.com.ecocoleta.ecocoleta.models.TipoResiduo;
import br.com.ecocoleta.ecocoleta.service.PontoColetaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pontos-coleta")
public class PontoColetaController {

    private final PontoColetaService service;

    public PontoColetaController(PontoColetaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<PontoColetaResponseDTO>> listar(
            @RequestParam(required = false) TipoResiduo tipo,
            @RequestParam(required = false) PontoStatus status,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(service.listar(tipo, status, pageable));
    }

    @GetMapping("/alertas")
    public ResponseEntity<List<PontoColetaResponseDTO>> alertas() {
        return ResponseEntity.ok(service.alertas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PontoColetaResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscar(id));
    }

    @PostMapping
    public ResponseEntity<PontoColetaResponseDTO> criar(@RequestBody @Valid PontoColetaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PontoColetaResponseDTO> atualizar(@PathVariable Long id,
                                                            @RequestBody @Valid PontoColetaRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/coleta")
    public ResponseEntity<PontoColetaResponseDTO> registrarColeta(@PathVariable Long id) {
        return ResponseEntity.ok(service.registrarColeta(id));
    }
}
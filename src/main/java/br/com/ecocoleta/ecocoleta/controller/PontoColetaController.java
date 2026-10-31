package br.com.ecocoleta.ecocoleta.controller;

import br.com.ecocoleta.ecocoleta.dto.ponto.PontoRequest;
import br.com.ecocoleta.ecocoleta.dto.ponto.PontoResponse;
import br.com.ecocoleta.ecocoleta.models.PontoStatus;
import br.com.ecocoleta.ecocoleta.models.TipoResiduo;
import br.com.ecocoleta.ecocoleta.service.PontoColetaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/pontos-coleta")
public class PontoColetaController {

    private final PontoColetaService pontoService;

    public PontoColetaController(PontoColetaService pontoService) {
        this.pontoService = pontoService;
    }

    @GetMapping
    public PagedModel<PontoResponse> findAll(
            @RequestParam(required = false) TipoResiduo tipoResiduo,
            @RequestParam(required = false) PontoStatus status,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size
    ) {
        return pontoService.findAll(tipoResiduo, status, page, size);
    }

    @GetMapping("/{id}")
    public PontoResponse findById(@PathVariable @Min(1) Long id) {
        return pontoService.findById(id);
    }

    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<PontoResponse> create(@Valid @RequestBody PontoRequest request) {
        PontoResponse ponto = pontoService.create(request);
        return ResponseEntity.status(201).body(ponto);
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public PontoResponse update(@PathVariable @Min(1) Long id, @Valid @RequestBody PontoRequest request) {
        return pontoService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> delete(@PathVariable @Min(1) Long id) {
        pontoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/coleta")
    @SecurityRequirement(name = "bearerAuth")
    public PontoResponse completeCollection(@PathVariable @Min(1) Long id) {
        return pontoService.completeCollection(id);
    }

    @GetMapping("/alertas")
    @SecurityRequirement(name = "bearerAuth")
    public java.util.List<PontoResponse> alerts() {
        return pontoService.findFull();
    }
}

package br.com.ecocoleta.ecocoleta.controller;

import br.com.ecocoleta.ecocoleta.dto.descarte.DescarteRequest;
import br.com.ecocoleta.ecocoleta.dto.descarte.DescarteResponse;
import br.com.ecocoleta.ecocoleta.service.RegistroDescarteService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/descartes")
public class RegistroDescarteController {

    private final RegistroDescarteService descarteService;

    public RegistroDescarteController(RegistroDescarteService descarteService) {
        this.descarteService = descarteService;
    }

    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<DescarteResponse> create(
            @Valid @RequestBody DescarteRequest request,
            Authentication authentication
    ) {
        DescarteResponse descarte = descarteService.create(request, authentication.getName());
        return ResponseEntity.status(201).body(descarte);
    }

    @GetMapping("/ponto/{pontoId}")
    @SecurityRequirement(name = "bearerAuth")
    public PagedModel<DescarteResponse> findByPoint(
            @jakarta.validation.constraints.Min(1) @org.springframework.web.bind.annotation.PathVariable Long pontoId,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size
    ) {
        return descarteService.findByPoint(pontoId, page, size);
    }
}

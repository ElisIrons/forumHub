package com.forumhub.Forum.Hub.controller;

import com.forumhub.Forum.Hub.dto.CursoDTO;
import com.forumhub.Forum.Hub.dto.CursoIdDTO;
import com.forumhub.Forum.Hub.service.CursoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/curso")
@SecurityRequirement(name = "bearer-key")
public class CursoController {

    @Autowired
    private CursoService service;

    @PostMapping
    @Transactional
    public ResponseEntity<String> cadastrarCurso(@RequestBody @Valid CursoDTO cursoDTO,
                                                 UriComponentsBuilder uriComponentsBuilder) {

        Long cursoId = service.saveCurso(cursoDTO);
        var uri = uriComponentsBuilder.path("/curso/{id}")
                .buildAndExpand(cursoId)
                .toUri();
        return ResponseEntity.created(uri)
                .body("Curso registrado com sucesso");
    }

    @GetMapping
    public ResponseEntity<Page<CursoIdDTO>> listarCursos(Pageable pageable) {
        Page<CursoIdDTO> cursosPage = service.getAllCursos(pageable);
        return ResponseEntity.ok(cursosPage);
    }

    @PutMapping("/{cursoId}")
    public ResponseEntity<String> atualizarCurso(
            @PathVariable Long cursoId,
            @RequestBody CursoIdDTO cursoIdDTO) {

        service.updateCurso(cursoId, cursoIdDTO);
        return ResponseEntity.ok("Curso atualizado com sucesso.");

    }

}
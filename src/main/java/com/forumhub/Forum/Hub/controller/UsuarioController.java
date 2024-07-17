package com.forumhub.Forum.Hub.controller;

import com.forumhub.Forum.Hub.dto.UsuarioDTO;
import com.forumhub.Forum.Hub.dto.UsuarioDetalhamentoDTO;
import com.forumhub.Forum.Hub.dto.UsuarioIdDTO;
import com.forumhub.Forum.Hub.service.UsuarioService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@RestController
@RequestMapping("/usuario")
@SecurityRequirement(name = "bearer-key")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    @Transactional
    public ResponseEntity<String> cadastrarUsuario(@RequestBody @Valid UsuarioDTO usuarioDTO, UriComponentsBuilder uriComponentsBuilder) {
        Long userId = usuarioService.saveUser(usuarioDTO);
        var uri = uriComponentsBuilder.path("/usuario/{id}")
                .buildAndExpand(userId).toUri();
        return ResponseEntity.created(uri)
                .body("Usuário registrado com sucesso!" + userId);
    }

    @GetMapping("")
    public ResponseEntity<Page<UsuarioIdDTO>> listarUsuarios(Pageable pageable) {
        Page<UsuarioIdDTO> usersPage = usuarioService.getAllUsers(pageable);
        return ResponseEntity.ok(usersPage);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<String> atualizarUsuario(
            @PathVariable Long userId,
            @RequestBody UsuarioIdDTO usuarioInfo) {
        usuarioService.updateUser(userId, usuarioInfo);
        return ResponseEntity.ok("Usuário atualizado com sucesso!");
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUsuarios(@PathVariable Long userId) {
        usuarioService.deleteUser(userId);
        return ResponseEntity.noContent().build();

    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDetalhamentoDTO> detalharUsuario(@PathVariable Long id) {
        Optional<UsuarioDetalhamentoDTO> detalheOptional = usuarioService.detalharUsuario(id);
        return detalheOptional
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
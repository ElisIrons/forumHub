package com.forumhub.Forum.Hub.controller;

import com.forumhub.Forum.Hub.dto.*;
import com.forumhub.Forum.Hub.model.Usuario;
import com.forumhub.Forum.Hub.service.RespostaService;
import com.forumhub.Forum.Hub.service.TopicoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/topicos")
@SecurityRequirement(name = "bearer-key")
public class TopicoController {


    @Autowired
    private TopicoService service;

    @Autowired
    private RespostaService respostaService;

    @PostMapping
    public ResponseEntity<String> cadastrarTopico(
            @RequestBody @Valid TopicoDTO topicoDTO,
            UriComponentsBuilder uriComponentsBuilder,
            Authentication authentication) {

        String loggedUserEmail = authentication.getName();

        Long topicoId = service.saveTopico(topicoDTO, loggedUserEmail);

        var uri = uriComponentsBuilder.path("/topicos/{id}")
                .buildAndExpand(topicoId).toUri();

        return ResponseEntity.created(uri)
                .body("Tópico registrado com sucesso!");
    }

    @GetMapping("/lista")
    public ResponseEntity<Page<TopicosAtivosDTO>> listarTopicosAtivos(
            @RequestParam(required = false) String cursoNome,
            @RequestParam(required = false) Integer ano,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<TopicosAtivosDTO> topicosPage = service.getAllTopicosAtivos(pageable, cursoNome, ano);
        return ResponseEntity.ok(topicosPage);
    }

    @GetMapping("listaAdmin")
    public ResponseEntity<Page<TopicoListDTO>> listarTodosTopicos(
            @RequestParam(required = false) String cursoNome,
            @RequestParam(required = false) Integer ano,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<TopicoListDTO> topicosPage = service.getAllTopicosOrderByDataCriacao(pageable, cursoNome, ano);
        return ResponseEntity.ok(topicosPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity detalharTopico(@PathVariable Long id){
        Optional<TopicoDetalhamentoDTO> detalheOptional = service.detalharTopico(id);

        return detalheOptional
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{topicoId}")
    public ResponseEntity<String> atualizarTopico(
            @PathVariable Long topicoId,
            @RequestBody TopicoDetalhamentoDTO topicoInformacao) {

        service.updateUser(topicoId, topicoInformacao);
        return ResponseEntity.ok("Tópico atualizado com sucesso.");
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluirTopico(@PathVariable Long id){
        service.inactivateTopico(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/resposta/{topicId}")
    public ResponseEntity<?> salvarResposta(
            @PathVariable Long topicId,
            @RequestBody RespostaDTO respostaDTO,
            Principal principal) {
        Usuario autor = respostaService.findByEmail(principal.getName());
        LocalDateTime dataCriacao = LocalDateTime.now();
        respostaService.saveResposta(topicId, respostaDTO, autor, dataCriacao);
        return ResponseEntity.ok().build();
    }

}
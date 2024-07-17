package com.forumhub.Forum.Hub.service;

import com.forumhub.Forum.Hub.dto.*;
import com.forumhub.Forum.Hub.model.Curso;
import com.forumhub.Forum.Hub.model.Topico;
import com.forumhub.Forum.Hub.model.Usuario;
import com.forumhub.Forum.Hub.repository.TopicoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TopicoService {
    @Autowired
    private TopicoRepository repository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CursoService cursoService;

    @Transactional
    public Long saveTopico(TopicoDTO topicoDto, String loggedUserEmail) {
        if (topicoDto.titulo() == null || topicoDto.mensagem() == null) {
            throw new IllegalArgumentException("Título e mensagem são obrigatórios.");
        }

        Usuario loggedUser = usuarioService.findByEmail(loggedUserEmail);
        if (repository.existsByTituloAndMensagemAndCursoId(topicoDto.titulo(), topicoDto.mensagem(), topicoDto.curso().id())) {
            throw new IllegalArgumentException("A combinação de título, mensagem e curso já está em uso.");
        }
        Long cursoId = topicoDto.curso().id();
        if (cursoId == null || !cursoService.existsById(cursoId)) {
            throw new IllegalArgumentException("A ID do curso não é válida!");
        }

        Topico topico = new Topico();
        topico.setTitulo(topicoDto.titulo());
        topico.setMensagem(topicoDto.mensagem());
        topico.setAutor(loggedUser);
        topico.setCurso(new Curso(cursoId, topicoDto.curso().nome(), topicoDto.curso().categoria()));
        topico.setData_criacao(LocalDateTime.now());
        topico.setStatus(true);

        Topico savedTopico = repository.save(topico);
        return savedTopico.getId();
    }

    public Page<TopicoListDTO> getAllTopicosOrderByDataCriacao(Pageable pageable, String cursoNome, Integer ano) {
        Page<Topico> topicosPage;

        if (cursoNome != null && ano != null) {
            topicosPage = repository.findByCursoNomeAndAno(cursoNome, ano, pageable);
        } else {
            topicosPage = repository.findAllByOrderByDataCriacaoAsc(pageable);
        }

        return topicosPage.map(topico -> new TopicoListDTO(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensagem(),
                new UsuarioIdDTO(topico.getAutor().getId(), topico.getAutor().getNome(), topico.getAutor().getEmail()),
                new CursoIdDTO(topico.getCurso().getId(), topico.getCurso().getNome(), topico.getCurso().getCategoria()),
                topico.isStatus()
        ));
    }

    public void updateUser(Long topicoId, TopicoDetalhamentoDTO topicoInfo) {
        Optional<Topico> optionalTopico = repository.findById(topicoId);
        if (optionalTopico.isEmpty()) {
            throw new IllegalStateException("Tópico não encontrado!");
        }

        Topico existingTopico = optionalTopico.get();

        existingTopico.setTitulo(topicoInfo.titulo());
        existingTopico.setMensagem(topicoInfo.mensagem());

        try {
            repository.save(existingTopico);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Título ou mensagem não podem ser nulos", e);
        }
    }

    @Transactional
    public void inactivateTopico(Long id) {
        Topico topic = repository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Tópico não encontrado!"));
        topic.setStatus(false);
        repository.save(topic);
    }


    public Page<TopicosAtivosDTO> getAllTopicosAtivos(Pageable pageable, String cursoNome, Integer ano) {
        Page<Topico> topicosPage;

        topicosPage = repository.findByStatusTrue(pageable);

        return topicosPage.map(topico -> new TopicosAtivosDTO(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensagem(),
                new UsuarioIdDTO(topico.getAutor().getId(), topico.getAutor().getNome(), topico.getAutor().getEmail()),
                new CursoIdDTO(topico.getCurso().getId(), topico.getCurso().getNome(), topico.getCurso().getCategoria())
        ));
    }
    public Topico findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tópico não encontrado :("));
    }

    public Optional<TopicoDetalhamentoDTO> detalharTopico(Long id) {
        Optional<Topico> optionalTopico = repository.findByIdAndStatusTrue(id);
        return optionalTopico.map(this::mapToDetalhamentoDTO);
    }

    private TopicoDetalhamentoDTO mapToDetalhamentoDTO(Topico topico) {
        List<RespostaIdDTO> respostasDto = topico.getRespostas().stream()
                .map(resposta -> new RespostaIdDTO(
                        resposta.getId(),
                        resposta.getMensagem(),
                        resposta.getData_criacao(),
                        resposta.isSolucao(),
                        resposta.getAutor().getId(),
                        resposta.getTopico().getId()
                ))
                .collect(Collectors.toList());

        return new TopicoDetalhamentoDTO(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensagem(),
                new UsuarioIdDTO(topico.getAutor().getId(), topico.getAutor().getNome(), topico.getAutor().getEmail()),
                new CursoIdDTO(topico.getCurso().getId(), topico.getCurso().getNome(), topico.getCurso().getCategoria()),
                respostasDto,
                topico.isStatus()
        );
    }
}
package com.forumhub.Forum.Hub.service;

import com.forumhub.Forum.Hub.dto.RespostaDTO;
import com.forumhub.Forum.Hub.model.Resposta;
import com.forumhub.Forum.Hub.model.Topico;
import com.forumhub.Forum.Hub.model.Usuario;
import com.forumhub.Forum.Hub.repository.RespostaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class RespostaService {
    @Autowired
    private RespostaRepository respostaRepository;

    @Autowired
    private TopicoService topicoService;

    @Autowired
    private UsuarioService usuarioService;

    public Usuario findByEmail(String email) {
        return usuarioService.findByEmail(email);
    }

    @Transactional
    public void saveResposta(Long topicId, RespostaDTO respostaDto, Usuario autor, LocalDateTime dataCriacao) {
        Topico topico = topicoService.findById(topicId);

        if (topico.isStatus()) {
            Resposta resposta = respostaDto.toEntity(autor, topico, dataCriacao);
            respostaRepository.save(resposta);
        } else {
            throw new IllegalArgumentException("Tópico inativo :(");
        }
    }

}

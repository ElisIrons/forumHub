package com.forumhub.Forum.Hub.dto;

import java.util.List;

public record TopicoDetalhamentoDTO(Long id, String titulo, String mensagem, UsuarioIdDTO autor, CursoIdDTO curso, List<RespostaIdDTO> resposta, boolean status) {

    public TopicoDetalhamentoDTO(Long id, String titulo, String mensagem, UsuarioIdDTO autor, CursoIdDTO curso, List<RespostaIdDTO> resposta, boolean status) {
        this.id = id;
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.autor = autor;
        this.curso = curso;
        this.resposta = resposta;
        this.status = status;
    }
}

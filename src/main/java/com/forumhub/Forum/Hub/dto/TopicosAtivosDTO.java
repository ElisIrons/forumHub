package com.forumhub.Forum.Hub.dto;

public record TopicosAtivosDTO(Long id, String titulo, String mensagem, UsuarioIdDTO autor, CursoIdDTO curso) {

    public TopicosAtivosDTO(Long id, String titulo, String mensagem, UsuarioIdDTO autor, CursoIdDTO curso) {
        this.id = id;
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.autor = autor;
        this.curso = curso;
    }
}
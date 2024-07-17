package com.forumhub.Forum.Hub.dto;

public record TopicoListDTO(Long id, String titulo, String mensagem, UsuarioIdDTO autor, CursoIdDTO curso, boolean status)
{
    public TopicoListDTO(Long id, String titulo, String mensagem, UsuarioIdDTO autor, CursoIdDTO curso, boolean status) {
        this.id = id;
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.autor = autor;
        this.curso = curso;
        this.status = status;
    }
}

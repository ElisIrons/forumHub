package com.forumhub.Forum.Hub.dto;

public record UsuarioIdDTO(Long id, String nome, String email, boolean status){

    public UsuarioIdDTO(Long id, String nome, String email) {
        this(id, nome, email, false);
    }
}
package com.forumhub.Forum.Hub.dto;

import jakarta.validation.constraints.NotBlank;

public record TopicoDTO(@NotBlank String titulo, @NotBlank String mensagem, UsuarioDTO autor, CursoDTO curso) {
}

package com.forumhub.Forum.Hub.dto;

import com.forumhub.Forum.Hub.model.Resposta;
import com.forumhub.Forum.Hub.model.Topico;
import com.forumhub.Forum.Hub.model.Usuario;

import java.time.LocalDateTime;

public record RespostaDTO(String mensagem, boolean solucao, Long autorId, Long topicoId) {

    public Resposta toEntity(Usuario autor, Topico topico, LocalDateTime dataCriacao) {
        Resposta resposta = new Resposta();
        resposta.setMensagem(this.mensagem());
        resposta.setSolucao(this.solucao());
        resposta.setAutor(autor);
        resposta.setTopico(topico);
        resposta.setData_criacao(dataCriacao);
        return resposta;
    }
}

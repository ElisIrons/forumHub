package com.forumhub.Forum.Hub.dto;

import java.time.LocalDateTime;

public record RespostaIdDTO(Long id, String mensagem, LocalDateTime data_criacao, boolean solucao, Long autorId, Long topicId) {
}

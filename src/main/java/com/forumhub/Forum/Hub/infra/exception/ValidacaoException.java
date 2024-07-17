package com.forumhub.Forum.Hub.infra.exception;

public class ValidacaoException extends RuntimeException {
    public ValidacaoException(String mensagem) {
        super (mensagem);
    }
}
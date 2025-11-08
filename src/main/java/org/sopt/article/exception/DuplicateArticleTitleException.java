package org.sopt.article.exception;

public class DuplicateArticleTitleException extends IllegalArgumentException {
    public DuplicateArticleTitleException(String message) {
        super(message);
    }
}
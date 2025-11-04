package org.sopt.article.exception;

public class DuplicateArticleTitleException extends IllegalStateException {
    public DuplicateArticleTitleException(String message) {
        super(message);
    }
}

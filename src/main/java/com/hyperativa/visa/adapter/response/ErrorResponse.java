package com.hyperativa.visa.adapter.response;

import java.util.List;

public record ErrorResponse(
    String message,
    List<String> errors
) {
    public ErrorResponse(String message) {
        this(message, null);
    }

    public ErrorResponse(List<String> errors) {
        this(null, errors);
    }
}

package com.hyperativa.visa.common.util;

public final class Constants {
    private Constants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String API_VERSION = "v1";
    public static final String BASE_PATH = "/api/" + API_VERSION;
    
    // Security Constants
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    
    // Error Messages
    public static final String INTERNAL_SERVER_ERROR = "Ocorreu um erro inesperado. Tente novamente mais tarde.";
    public static final String INVALID_CREDENTIALS = "Usuário e/ou Senha estão incorretos.";
    
    // Validation Messages
    public static final String INVALID_CARD_NUMBER = "Número do cartão inválido";
    public static final String INVALID_CARD_EXPIRY = "Data de expiração inválida";
    public static final String INVALID_CARD_CVV = "CVV inválido";
} 
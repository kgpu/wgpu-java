package com.noahcharlton.wgpuj.jnrgen;

public class Token {

    public enum TokenType {
        COMMENT,
        IDENTIFIER,
        HASH,
        LEFT_PARENTHESIS,
        RIGHT_PARENTHESIS,
        OPEN_BRACKET,
        CLOSE_BRACKET,
        LESS_THAN,
        GREATER_THAN,
        COMMA,
        SEMICOLON,
        PERIOD,
        EQUAL,
        NEWLINE,
        EOF,
    }

    private final TokenType type;
    private final String text;

    public Token(TokenType type, String text) {
        this.type = type;
        this.text = text;
    }

    public Token(TokenType type) {
        this(type, "");
    }

    @Override
    public String toString() {
        String text = this.text;

        if(text.length() > 50)
            text = text.substring(0, 50) + "...";

        return String.format("%s(%s)", type, text);
    }
}

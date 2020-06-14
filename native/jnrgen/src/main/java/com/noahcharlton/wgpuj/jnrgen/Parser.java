package com.noahcharlton.wgpuj.jnrgen;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Parser {

    private final List<Item> items = new ArrayList<>();
    private final List<Token> tokens;

    private int index = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;

        parse();
    }

    private void parse() {
        Token token;

        while((token = poll()) != null){
            var item = createItem(token);

            if(item != null){
                items.add(item);
                System.out.println("Item: " + item);
            }
        }
    }

    private Item createItem(Token token) {
        if(Token.identifier("typedef").equals(token)){
            var next = poll();

            if(Token.identifier("enum").equals(next)){
                return createEnum();
            }
        }
        return null;
    }

    private Item createEnum() {
        List<EnumItem.EnumField> fields = new ArrayList<>();

        skipWhitespace();
        pollExpect(Token.TokenType.OPEN_BRACKET);
        skipWhitespace();

        Token token;
        while((token = poll()).getType() != Token.TokenType.CLOSE_BRACKET){
            Token identifier = Objects.requireNonNull(token);
            Token equalOrComma = Objects.requireNonNull(poll());

            if(equalOrComma.getType() == Token.TokenType.COMMA){
                fields.add(new EnumItem.EnumField(identifier.getText(), fields.size()));
            }else{
                Token valueToken = pollExpect(Token.TokenType.IDENTIFIER);
                int value = Integer.parseInt(valueToken.getText());

                fields.add(new EnumItem.EnumField(identifier.getText(), value));

                pollExpect(Token.TokenType.COMMA);
            }

            skipWhitespace();
        }

        Token identifier = pollExpect(Token.TokenType.IDENTIFIER);

        return new EnumItem(identifier.getText(), fields);
    }

    private void skipWhitespace(){
        while(peek().getType() == Token.TokenType.NEWLINE || peek().getType() == Token.TokenType.COMMENT){
            poll();
        }
    }

    private Token pollExpect(Token.TokenType type){
        var token = poll();

        if(token == null || token.getType() != type){
            throw new RuntimeException("Expected " + type + " but found " + token);
        }

        return token;
    }

    private Token poll(){
        if(index >= tokens.size())
            return null;

        return tokens.get(index++);
    }

    private Token peek(){
        if(index >= tokens.size())
            return null;

        return tokens.get(index);
    }

    public List<Item> getItems() {
        return items;
    }
}

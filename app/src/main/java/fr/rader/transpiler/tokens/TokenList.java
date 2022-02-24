package fr.rader.transpiler.tokens;

import java.util.ArrayList;
import java.util.List;

public class TokenList {

    private final List<Token> tokens;

    private int index = 0;

    public TokenList() {
        this.tokens = new ArrayList<>();
    }

    public void add(Token token) {
        tokens.add(token);
    }

    public Token peek() {
        if (index >= tokens.size()) {
            return null;
        }

        return tokens.get(index);
    }

    public Token get() {
        Token token = peek();

        index++;

        return token;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public void skip() {
        index++;
    }

    public void reset() {
        index = 0;
    }
}

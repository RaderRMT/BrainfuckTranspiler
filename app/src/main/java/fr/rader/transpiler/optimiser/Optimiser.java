package fr.rader.transpiler.optimiser;

import fr.rader.transpiler.tokens.Token;
import fr.rader.transpiler.tokens.TokenList;
import fr.rader.transpiler.tokens.TokenType;

import static fr.rader.transpiler.tokens.TokenType.*;

public class Optimiser {

    private final TokenList originalTokenList;

    private final TokenList optimisedTokenList;

    public Optimiser(TokenList originalTokenList) {
        this.originalTokenList = originalTokenList;
        this.optimisedTokenList = new TokenList();
    }

    public TokenList optimise() {
        for (Token token : originalTokenList.getTokens()) {
            if (token.getType().equals(POINTER_OFFSET)) {
                int value = (int) token.getValue();

                if (value < 0) {
                    addToken(LESS, -value);
                } else {
                    addToken(GREATER, value);
                }
            } else if (token.getType().equals(VALUE_OFFSET)) {
                int value = (int) token.getValue();

                if (value < 0) {
                    addToken(MINUS, -value);
                } else {
                    addToken(PLUS, value);
                }
            } else {
                optimisedTokenList.add(token);
            }
        }

        return optimisedTokenList;
    }

    private void addToken(TokenType type, Object value) {
        optimisedTokenList.add(new Token(
                type,
                value
        ));
    }
}

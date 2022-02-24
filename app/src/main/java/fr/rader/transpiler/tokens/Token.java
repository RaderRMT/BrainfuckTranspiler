package fr.rader.transpiler.tokens;

public class Token {

    /** The token type */
    private final TokenType type;

    /** The token value. it will either be a number, or null */
    private final Object value;

    public Token(TokenType type, Object value) {
        this.type = type;
        this.value = value;
    }

    public TokenType getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Token [type=" + type + ", value=" + value + "]";
    }
}

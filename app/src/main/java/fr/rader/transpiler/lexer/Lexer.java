package fr.rader.transpiler.lexer;

import java.io.File;
import java.io.IOException;

import fr.rader.transpiler.tokens.Token;
import fr.rader.transpiler.tokens.TokenList;
import fr.rader.transpiler.tokens.TokenType;
import fr.rader.transpiler.utils.io.SourceStream;

import static fr.rader.transpiler.tokens.TokenType.*;

public class Lexer {

    private static final int DATA_SIZE = 30_000;

    private final SourceStream source;

    private final TokenList tokens;

    private boolean hasError = false;

    private int bracketLevel = 0;
    private int pointerPosition = 0;

    public Lexer(File file) throws IOException {
        this.source = new SourceStream(file);
        this.tokens = new TokenList();
    }

    public TokenList scanTokens() throws IOException {
        while (!hasError && source.hasNext()) {
            scanToken();
        }

        if (hasError) {
            System.out.println("An error occured");
        }

        addToken(EOF);

        return tokens;
    }

    private void scanToken() throws IOException {
        char c = source.next();

        switch (c) {
            case '+':
            case '-':
                int countSigns = countValue((c == '+') ? 1 : -1);

                if (countSigns != 0) {
                    addToken(VALUE_OFFSET, countSigns);
                }
                break;

            case '[':
                addToken(OPEN_SQUARE_BRACKET);
                bracketLevel++;
                break;
            case ']':
                if (bracketLevel == 0) {
                    System.out.println("Invalid bracket level!");

                    hasError = true;
                    return;
                }

                addToken(CLOSE_SQUARE_BRACKET);
                break;

            case '.':
                addToken(DOT);
                break;
            case ',':
                addToken(COMMA);
                break;
                
            case '>':
                int countGreater = countPointer(1);
                if (countGreater != 0) {
                    if (pointerPosition + countGreater == DATA_SIZE) {
                        System.out.println("Pointer reached upper bound");

                        hasError = true;
                        return;
                    }
                
                    pointerPosition += countGreater;
                    addToken(POINTER_OFFSET, countGreater);
                }
                break;
            case '<':
                int countLess = countPointer(-1);
                if (countLess != 0) {
                    if (pointerPosition - countLess < 0) {
                        System.out.println("Pointer reached lower bound");

                        hasError = true;
                        return;
                    }

                    pointerPosition -= countLess;
                    addToken(POINTER_OFFSET, countLess);
                }
                break;

            default:
                break;
        }
    }

    private int countValue(int initialValue) throws IOException {
        int count = initialValue;

        while (source.peek(SourceStream.NEXT) == '+' ||
                source.peek(SourceStream.NEXT) == '-') {
            count += (source.next() == '+') ? 1 : -1;
        }

        return count;
    }

    private int countPointer(int initialValue) throws IOException {
        int count = initialValue;

        while (source.peek(SourceStream.NEXT) == '>' ||
                source.peek(SourceStream.NEXT) == '<') {
            count += (source.next() == '>') ? 1 : -1;
        }

        return count;
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object value) {
        tokens.add(new Token(
                type,
                value
        ));
    }
}

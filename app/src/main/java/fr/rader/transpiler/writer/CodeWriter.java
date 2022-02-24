package fr.rader.transpiler.writer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import fr.rader.transpiler.tokens.Token;
import fr.rader.transpiler.tokens.TokenList;

public class CodeWriter {

    private final FileWriter fileWriter;

    private int braceLevel = 1;

    public CodeWriter(File file) throws IOException {
        this.fileWriter = new FileWriter(file);

        initFile();
    }

    private void initFile() throws IOException {
        fileWriter.write(
                "#include <stdlib.h>\n" +
                "#include <stdio.h>\n\n" +
                "int main() {\n" +
                "    char* data = malloc(30000);\n" +
                "    int pointer = 0;\n" +
                "    for (int i = 0; i < 30000; i++) {\n" +
                "        data[i] = 0;\n" +
                "    }\n\n"
        );
    }

    private void closeFile() throws IOException {
        fileWriter.append(
                "\n    free(data);\n" +
                "}"
        );

        fileWriter.flush();
        fileWriter.close();
    }

    public void write(TokenList tokens) throws IOException {
        for (Token token : tokens.getTokens()) {
            switch (token.getType()) {
                case PLUS:
                    writeLine("data[pointer] += " + (int) token.getValue() + ';');
                    break;
                case MINUS:
                    writeLine("data[pointer] -= " + (int) token.getValue() + ';');
                    break;

                case OPEN_SQUARE_BRACKET:
                    writeLine("while (data[pointer] != 0) {");
                    braceLevel++;
                    break;
                case CLOSE_SQUARE_BRACKET:
                    braceLevel--;
                    writeLine("}\n");
                    break;

                case GREATER:
                    writeLine("pointer += " + (int) token.getValue() + ';');
                    break;
                case LESS:
                    writeLine("pointer -= " + (int) token.getValue() + ';');
                    break;

                case COMMA:
                    writeLine("scanf(\"%c\", data + pointer);");
                    break;
                case DOT:
                    writeLine("printf(\"%c\", data[pointer]);");
                    break;
                    
                case EOF:
                    closeFile();
                    break;

                default:
                    break;
            }
        }
    }

    private void writeLine(String line) throws IOException {
        for (int i = 0; i < braceLevel * 4; i++) {
            fileWriter.append(" ");
        }

        fileWriter.append(line + '\n');
    }
}

package fr.rader.transpiler;

import java.io.File;
import java.io.IOException;

import fr.rader.transpiler.lexer.Lexer;
import fr.rader.transpiler.optimiser.Optimiser;
import fr.rader.transpiler.writer.CodeWriter;

public class Transpiler {
    
    void start() throws IOException {
        File file = new File("/home/rader/BrainFuck/hello.bf");

        Lexer lexer = new Lexer(file);
        Optimiser optimiser = new Optimiser(lexer.scanTokens());

        CodeWriter writer = new CodeWriter(new File("/home/rader/BrainFuck/c.c"));
        writer.write(optimiser.optimise());
    }
}

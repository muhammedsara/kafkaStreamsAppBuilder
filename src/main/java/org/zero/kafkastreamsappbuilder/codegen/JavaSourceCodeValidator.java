package org.zero.kafkastreamsappbuilder.codegen;

import net.openhft.compiler.CompilerUtils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class JavaSourceCodeValidator {

    public static class CompilationException extends Exception {
        private String compilerMessage;

        public CompilationException(String compilerMessage) {
            this.compilerMessage = compilerMessage;
        }

        public CompilationException(String message, String compilerMessage) {
            super(message);
            this.compilerMessage = compilerMessage;
        }

        public String getCompilerMessage() {
            return compilerMessage;
        }
    }

    public static Class<?> compileAndLoad(String className, String code) throws CompilationException {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        Class ret = null;
        try {
            ret = CompilerUtils.CACHED_COMPILER.loadFromJava(ClassLoader.getSystemClassLoader(), className, code, pw);
        } catch (ClassNotFoundException e) {
            String exceptionAsString = sw.toString();
            throw new CompilationException(exceptionAsString);
        }
        return ret;
    }

}

package fr.jmini.jdt.gradle.example.app;

import fr.jmini.jdt.gradle.example.JdtFormatter;

public class App {
    public static void main(String[] args) {
        System.out.println("App using the java formatter");

        String result = getFormattedHelloWorld();
        System.out.println(result);
    }

    public static String getFormattedHelloWorld() {
        String javaCode = "public class MyClass{ "
                + "public static void main(String[] args) { "
                + "System.out.println(\"Hello World\");"
                + " }"
                + " }";
        return JdtFormatter.format(javaCode);
    }
}

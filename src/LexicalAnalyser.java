import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.List;
import java.io.PrintWriter;

public class LexicalAnalyser {
    public static String fileString = "";

    public LexicalAnalyser(String filePath) {
        fileString = readLineByLineJava8(filePath);
    }

    public void main() throws IOException {
        String withoutComments = fileString.replaceAll("(?s:/\\*.*?\\*/)|//.*", "");
        String withoutSpaces = withoutComments.replaceAll("(?s)\\s+|/\\*.*?\\*/|//[^\\r\\n]*", " ");
        String help = withoutSpaces
                .replaceAll("(?<=.)(\\,|\\\"|\\(|\\)|\\'|\\;|\\.|\\{|\\}|\\:|\\++|\\-|\\!|\\?|\\[|\\])(|[a-z])", " ");
        List<tokenBrabo> tokenList = new ArrayList<tokenBrabo>();

        StringTokenizer st = new StringTokenizer(help);

        tokenBrabo token;

        int cont = -1;
        while (st.hasMoreTokens()) {
            cont++;
            String x = st.nextToken();
            token = new tokenBrabo();
            token.id = cont;
            token.value = x;
            token.type = validateString(x);
            tokenList.add(token);
            System.out.println(token.getId() + " | " + token.getValue() + " | " + token.getType() + "\n");
        }

        try (PrintWriter out = new PrintWriter("saida.csv")) {
            for (int i = 0; i < tokenList.size(); i++) {
                token = new tokenBrabo();

                token = tokenList.get(i);

                String linha = token.getId() + " | " + token.getValue() + " | " + token.getType() + "\n";

                out.println(linha);
            }
        }
    }

    private static String readLineByLineJava8(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return contentBuilder.toString();
    }

    private static String validateString(String x) {
        String[] palavrasResevadas = { "window", "document", "use", "strict", "abstract", "arguments", "await",
                "boolean", "break", "byte", "case", "catch", "char", "class", "const", "continue", "debugger",
                "default", "delete", "do", "double", "else", "enum", "eval", "export", "extends", "false", "final",
                "finally", "float", "for", "function", "goto", "if", "implements", "import", "in", "instanceof", "int",
                "interface", "let", "long", "native", "new", "null", "package", "private", "protected", "public",
                "return", "short", "static", "super", "switch", "synchronized", "this", "throw", "throws", "transient",
                "true", "try", "typeof", "var", "void", "volatile", "while", "with", "yield" };
        String[] comparador = { "==", "===", "!==", "!=", ">", "=>", "=<", "<", "!", "&&", "||", "?" };
        String[] operador = { "+", "-", "++", "*", "/", "=" };

        for (String a : palavrasResevadas) {
            if (a.equals(x))
                return "palavra-reservada";
        }

        for (String a : comparador) {
            if (a.equals(x))
                return "comparador";
        }

        for (String a : operador) {
            if (a.equals(x))
                return "operador";
        }

        if (x.matches("[0-9]\\.[0-9]"))
            return "float";

        if (x.matches("[0-9]"))
            return "inteiro";

        if (x.matches("(true|false)"))
            return "boolean";

        if (x.matches(
                "(?i)string|int|array|date|list|JSON|float|boolean|var|null|undefined|function|bigint|char|symbol|math"))
            return "tipo-de-dado";

        if (x.matches(".$"))
            return "char";

        if (x.matches("[a-z][a-z]+[A-Z][a-z]+"))
            return "funcao";

        if (x.matches("[a-z][A-Z][a-z]+"))
            return "variavel";

        return "string";
    }
}
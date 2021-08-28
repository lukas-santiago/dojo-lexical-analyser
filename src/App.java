
public class App {
    public static void main(String[] args) throws Exception {
        // String filePath =
        // "C:\\Users\\lukas\\.dev\\dojo-lexical-analyser\\input-file\\jquery-3.6.0.js";
        String filePath = "jquery-3.6.0.js";
        var lexicalAnalyser = new LexicalAnalyser(filePath);
        lexicalAnalyser.main();
    }
}

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    private static final String PATH = "D:\\Конструирование компиляторов\\Labs\\-ompiler-labs\\Lab_1.3\\input.txt";
    public static void main(String[] args) throws IOException {
        System.out.println("----------------PROGRAM----------------");
        String program = new String(Files.readAllBytes(Paths.get(PATH)));
        System.out.println(program);
        Compiler compiler = new Compiler();
        Scanner scanner = compiler.getScanner(program);
        Token token;
        System.out.println("----------------TOKENS-----------------");
        do {
            token = scanner.nextToken();
            System.out.println(token +
                    (token.type == Type.IDENT ? ": "  + compiler.getName(((IdentToken) token).getPointer()) : "") +
                    (token.type == Type.NUMBER ? ": "  + ((NumberToken) token).getNumber() : "" ));
        } while (token.type != Type.END_OF_PROGRAM);
    }
}

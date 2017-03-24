import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by alexbelogurow on 21.03.17.
 *
 * Реализовать чтение входного потока и лексический анализ
 * Целочисленные константы: последовательности десятичных цифр, предваряемые символом #
 * Имена переменных: последовательности десятичных цифр, начинающиеся со знаков "." или ":"
 * Имена массивов: последовательности десятичных цифр, начинающиеся со знаков "," или ";"
 * Ключевые слова "PLEASE", "DO", "FORGET"
 *
 */
public class Main {
    public static void main(String[] args) throws IOException {
        Compiler compiler = new Compiler();
        String program = new String(Files.readAllBytes(Paths.get("src/input.txt")));
        Scanner scanner = new Scanner(program, compiler);
        System.out.println("TOKENS:");
        while (true) {
            Token token = scanner.nextToken();
            if (token.tag != DomainTag.END_OF_PROGRAM) {
                System.out.print(token.tag.name()+" "+token.coords.toString() + ": " );
                if (token instanceof KeywordToken) {
                    System.out.print(((KeywordToken) token).value);
                }
                else if (token instanceof VariableToken) {
                    System.out.print(((VariableToken) token).value);
                }
                else if (token instanceof ArrayToken) {
                    System.out.print(((ArrayToken) token).value);
                }
                else if (token instanceof ConstToken) {
                    System.out.print(((ConstToken) token).value);
                }
                System.out.println();
            } else {
                break;
            }
        }
        System.out.println("MESSAGES:");
        compiler.outputMessages();
    }
}

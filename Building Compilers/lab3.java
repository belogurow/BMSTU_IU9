import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by alexbelogurow on 03.03.17.
 *
 * Идентификаторы: последовательности десятичных цифр. Числовые литералы: римские цифры
 * и ключевое слово "NIXIL", не чувствительны к регистру.
 *
 */

public class Lab3 {
    public static void main(String[] args) {
        List<String> lines = null;

        String file = args[0];
        try {
            lines = Files.readAllLines(Paths.get(file), StandardCharsets.UTF_8);
            int numberOfLine = 1;
            for(String line: lines){
                test_match(line, numberOfLine);
                numberOfLine++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void test_match(String line, int numberOfline) {
        // Регулярные выражения
        String number = "[0-9]+";
        String nixil = "(?i)NIXIL";
        String validRoman = "(?i)(?=[MDCLXVI])M*(C[MD]|D?C{0,3})(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})";

        String pattern = "(" + number + ")|(" + nixil + ")|(" + validRoman + ")|(\\S)";

        // Компиляция регулярного выражения
        Pattern p = Pattern.compile(pattern);

        // Сопоставление текста с регулярным выражением
        Matcher m = p.matcher(line);
        ArrayList<String> allMatches = new ArrayList<>();
        while (m.find()) {
            allMatches.add(m.group());
        }

        //System.out.println(allMatches);

        int positionOfItem = 0;
        boolean error = false;
        for (String item : allMatches) {

            if (Pattern.compile(number).matcher(item).matches()) {
                positionOfItem += line.indexOf(item);
                System.out.format("IDENT (%d, %d): %s %n", numberOfline, positionOfItem + 1, item);
                line = line.substring(line.indexOf(item) + item.length());
                positionOfItem += item.length();
                error = false;
            }
            else if (Pattern.compile(nixil).matcher(item).matches() ||
                    (Pattern.compile(validRoman).matcher(item).matches())) {
                positionOfItem += line.indexOf(item);
                System.out.format("LITERAL (%d, %d): %s %n", numberOfline, positionOfItem + 1, item);
                line = line.substring(line.indexOf(item) + item.length());
                positionOfItem += item.length();
                error = false;
            }
            else {
                if (!error) {
                    positionOfItem += line.indexOf(item);
                    System.out.format("%nsyntax error (%d, %d) %n%n", numberOfline, positionOfItem + 1);
                    line = line.substring(line.indexOf(item) + item.length());
                    positionOfItem += item.length();
                    error = !error;
                }
            }
        }


    }
}

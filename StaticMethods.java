import java.util.*;

public class StaticMethods {
    private enum TokenType {
        None, Variable, Integer, Double, Character, String, Array
    };

    public static void print(String line, Map<String, Variable> variables) {
        // get beginning of string literal and trim line
        int beginIndex = line.indexOf(Keywords.STRING_LITERAL_KEYWORD);
        int endIndex = line.indexOf(Keywords.STRING_LITERAL_KEYWORD, beginIndex + 1);
        String remaining = line.substring(endIndex + Keywords.STRING_LITERAL_KEYWORD.length());
        //System.out.println(beginIndex + " " + endIndex + remaining);
        if (remaining.length() > 0) {
            throw new Error("Invalid Arguments to command: " + Keywords.PRINT_KEYWORD);
        }
        else {
            System.out.println(interpretExpression(line, variables));
        }
    }

    //xd #My name is # name # and my age is # 69
    private static Variable interpretExpression(String line, Map<String, Variable> variables) {
        Variable result = new Variable();
        List<String> tokens = new ArrayList<String>();
        String currentToken = "";
        TokenType tokenType = TokenType.None;
        for (char c : line.toCharArray()) {
            //check for string start/end
            if ()
        }


        return result;
    }
}

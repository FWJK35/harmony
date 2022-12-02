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

    // takes an List of Strings for each line of code and index of line where for loop begins
    public static void forLoop(List<String> code, int start) {
        int indent = 0;
        int temp = 0;
        Iterator<String> itr = code.listIterator(start);
        // checks indentation level of line containing forLoop call
        for (char c : itr.next().toCharArray()) {
            if (c == ' ') {
                temp++;
            }
            else {
                break;
            }
        }
        // checks that there is a next line with indentation greater than forLoop call
        if (itr.hasNext()) {
            for (char c : itr.next().toCharArray()) {
                if (c == ' ') {
                    indent++;
                }
                else {
                    break;
                }
            }
        }
        if (temp < indent) {
                while (itr.hasNext()) {
                    String line = itr.next();
                    // TODO: finish this lol
                }
            }
        else {
            // TODO: error logging for incorrect indentation
        }
    }

    //xd #My name is # name # and my age is # 69
    public static Variable interpretExpression(String line, Map<String, Variable> variables) {
        Variable result = new Variable();
        List<String> tokens = new ArrayList<String>();
        String currentToken = "";
        TokenType tokenType = TokenType.None;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            //previous char not escape character
            if (!(i > 0 && line.charAt(i - 1) == Keywords.ESCAPE_CHARACTER_KEYWORD)) {
                //begin or end string
                if (c == Keywords.STRING_LITERAL_KEYWORD) {
                    if (tokenType == TokenType.None) {
                        tokenType = TokenType.String;
                    }
                    else if (tokenType == TokenType.String) {
                        tokenType = TokenType.None;
                    }
                    else {
                        throw new Error("Error interpreting expression!");
                    }
                }
                else if (tokenType == TokenType.String) {
                    currentToken += c;
                }
                //previous char is operator or space
                else if (Keywords.OPERATOR_CHARACTERS.contains(Character.toString(line.charAt(i - 1)))) {
                    
                }
            }
            
        }


        return result;
    }
}

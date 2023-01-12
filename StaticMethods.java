import java.util.*;

public class StaticMethods {
    
    //types of expressions used in token types
    private enum TokenType {
        None, Number, String, Expression, Array, Identifier, Operator, Joiner
    }
    //types of expressions used in expression stack
    private enum ExpressionType {
        None, Expression, Array, String, Identifier
    }

    public static void print(String line, Environment env) {
        // get beginning of string literal and trim line
        int beginIndex = line.indexOf(Keywords.STRING_LITERAL_KEYWORD);
        int endIndex = line.indexOf(Keywords.STRING_LITERAL_KEYWORD, beginIndex + 1);
        String remaining = line.substring(endIndex + 1);
        //System.out.println(beginIndex + " " + endIndex + remaining);
        if (remaining.length() > 0) {
            throw new Error("Invalid Arguments to command: " + Keywords.PRINT_KEYWORD);
        }
        else {
            System.out.println(interpretExpression(line, env));
        }
    }
    
    public static int countIndent(String line) {
        int indent = 0;
        for (char c : line.toCharArray()) {
            if (c == ' ') {
                indent++;
            }
            else {
                break;
            }
        }
        return indent;
    }

    public static Variable getTokenVariable(String token) {
        try {
            return new Variable(Integer.parseInt(token));
        } catch (Exception e) {}
        try {
            return new Variable(Double.parseDouble(token));
        } catch (Exception e) {}
        return new Variable(token);
    }

    public static Variable eval(String line, Environment env) {

        Variable result = new Variable();
        List<TokenType> tokenTypes = new ArrayList<TokenType>();
        List<Variable> tokenVariables = new ArrayList<Variable>();
        Stack<ExpressionType> tokenStack = new Stack<ExpressionType>();
        String currentToken = "";

        for (int i = 0; i < line.length(); i++) {

            char c = line.charAt(i);
            boolean skip = false;
            ExpressionType currentType = ExpressionType.None;

            if (!tokenStack.isEmpty()) {
                currentType = tokenStack.peek();
            }

            if (c == Keywords.ESCAPE_CHARACTER_KEYWORD) {
                //skip next character
                skip = true;
                i++;
            }

            if (!skip) {
                //begin/end string expression
                if (c == Keywords.STRING_LITERAL_KEYWORD) {
                    //check for end string
                    if (!tokenStack.isEmpty() && tokenStack.peek() == ExpressionType.String) {
                        //end string
                        tokenStack.pop();
                        //don't add this character if it isn't inside any expression
                        if (tokenStack.isEmpty()) {
                            Variable strVar = new Variable(currentToken);
                            tokenVariables.add(strVar);
                            tokenTypes.add(TokenType.String);
                            currentToken = "";
                            skip = true;
                        }
                    }
                    //check for begin string
                    else {
                        //don't add this character if it isn't inside any expression
                        if (tokenStack.isEmpty()) {
                            skip = true;
                        }
                        //begin string
                        tokenStack.add(ExpressionType.String);
                    }
                }
                //if not currently in a string
                else if (currentType != ExpressionType.String) {
                    //check for begin expression
                    if (c == Keywords.OPEN_PAREN_KEYWORD) {
                        //don't add this character if it isn't inside any expression
                        if (tokenStack.isEmpty()) {
                            skip = true;
                        }
                        //begin expression
                        tokenStack.add(ExpressionType.Expression);
                    }

                    //check for end expression
                    else if (c == Keywords.CLOSE_PAREN_KEYWORD) {
                        //check that closing parenthesis is valid
                        if (!tokenStack.isEmpty() && tokenStack.peek() == ExpressionType.Expression) {
                            //end expression
                            tokenStack.pop();
                            if (tokenStack.isEmpty()) {
                                Variable expVar = new Variable(currentToken);
                                tokenVariables.add(expVar);
                                tokenTypes.add(TokenType.Expression);
                                currentToken = "";
                                skip = true;
                            }
                        }
                        //does not have matching parenthesis
                        else {
                            if (tokenStack.isEmpty()) {
                                throw new Error("No opening parenthesis");
                            }
                            else {
                                throw new Error("Must close previous expression");
                            }
                        }
                        //don't add this character if it isn't inside any expression
                        if (tokenStack.isEmpty()) {
                            skip = true;
                        }
                    }

                    //check for beginning of array
                    else if (c == Keywords.OPEN_ARRAY_KEYWORD) {
                        //don't add this character if it isn't inside any expression
                        if (tokenStack.isEmpty()) {
                            skip = true;
                        }
                        //begin array index or definition
                        tokenStack.add(ExpressionType.Array);
                    }

                    //check for end of array
                    else if (c == Keywords.CLOSE_ARRAY_KEYWORD) {
                        //check that closing parenthesis is valid
                        if (!tokenStack.isEmpty() && tokenStack.peek() == ExpressionType.Array) {
                            //end expression
                            tokenStack.pop();
                            if (tokenStack.isEmpty()) {
                                Variable arrVar = new Variable(currentToken);
                                tokenVariables.add(arrVar);
                                tokenTypes.add(TokenType.Array);
                                currentToken = "";
                                skip = true;
                            }
                        }
                        //does not have matching parenthesis
                        else {
                            if (tokenStack.isEmpty()) {
                                throw new Error("No opening square bracket");
                            }
                            else {
                                throw new Error("Must close previous expression");
                            }
                        }
                        //don't add this character if it isn't inside any expression
                        if (tokenStack.isEmpty()) {
                            skip = true;
                        }
                    }
                    
                    //check for beginning of identifier
                    else if (isAlpha(c)) {
                        //will need to be evaluated
                        if (tokenStack.isEmpty()) {
                            while (isIdentifierChar(c)) {
                                currentToken += c;
                                i++;
                                c = line.charAt(i);
                            }
                            //end by setting current character to last one of identifier
                            i--;
                            skip = true;
                            //currentToken is now the name of the identifier
                            tokenTypes.add(TokenType.Identifier);
                            tokenVariables.add(new Variable(currentToken));
                            currentToken = "";
                        }
                    }
                
                    //check for explicitly defined number
                    else if (isDigit(c)) {
                        //will need to be evaluated
                        if (tokenStack.isEmpty()) {
                            while (isDigit(c)) {
                                currentToken += c;
                                i++;
                                c = line.charAt(i);
                            }
                            Variable numVar;
                            if (c == '.') {
                                do {
                                    currentToken += c;
                                    i++;
                                    c = line.charAt(i);
                                } while (isIdentifierChar(c));
                                numVar = new Variable(Double.parseDouble(currentToken));
                            }
                            else {
                                numVar = new Variable(Integer.parseInt(currentToken));
                            }
                            //end by setting current character to last one of identifier
                            i--;
                            skip = true;
                            //currentToken is now the value of number
                            tokenTypes.add(TokenType.Number);
                            tokenVariables.add(numVar);
                            currentToken = "";
                        }
                    }

                    //check for operator character
                    else if (Keywords.OPERATOR_CHARACTERS.indexOf(c) > -1) {
                        if (tokenStack.isEmpty()) {
                            tokenTypes.add(TokenType.Operator);
                            tokenVariables.add(new Variable(c));
                            skip = true;
                        }
                    }
                
                    //check for joiner keyword
                    else if (c == ' ') {
                        if (tokenStack.isEmpty()) {
                            tokenTypes.add(TokenType.Joiner);
                            tokenVariables.add(new Variable());
                            skip = true;
                        }
                    }
                
                    //must be unexpected character, throw error
                    else {
                        throw new Error("Unexpected symbol at position: " + i);
                    }
                
                }
                //first-level expression has not begun or ended
                if (!skip) {
                    currentToken += c;
                }
            }
        }
        //process token combinations
        List<Variable> finalVariables = new ArrayList<Variable>();
        for (int t = 0; t < tokenTypes.size(); t++) {
            TokenType currentType = tokenTypes.get(t);
            Variable currentVariable = tokenVariables.get(t);
            System.out.println(tokenTypes.get(t) + ": " + tokenVariables.get(t));
            if (currentType == TokenType.Identifier) {
                //there is another token after it
                if (t + 1 < tokenTypes.size()) {
                    TokenType nextType = tokenTypes.get(t + 1);

                    //TODO figure out what identifier is

                    //identifies function
                    if (nextType == TokenType.Expression) {
                        t++;
                        List<Variable> arguments = new ArrayList<Variable>();
                        Variable nextVariable = tokenVariables.get(t);
                        //adds arguments to function parameters
                        for (String arg : separate((String) currentVariable.getData())) {
                            arguments.add(eval(arg, env));
                        }
                        Function func = env.getFunction(currentToken, arguments);
                        if (func != null) {
                            finalVariables.add(func.run(arguments));
                        }
                    }
                    
                    //identifies array index or slice
                    else if (nextType == TokenType.Array) {
                        //TODO do something
                    }

                    //variable OR operator
                    else if (nextType == TokenType.Joiner) {
                        while (nextType == TokenType.Joiner) {
                            t++;
                            nextType = tokenTypes.get(t);
                        }
                        //number expression
                        if (nextType == TokenType.Operator) {

                        }
                    }

                    else {
                        throw new Error("Incorrect token found!");
                    }
                }

                //just a variable
                else {
                    
                }
            }
        }

        return result;
    }

    public static List<String> separate(String line) {
        List<String> args = new ArrayList<String>();
        Stack<ExpressionType> tokenStack = new Stack<ExpressionType>();
        String currentArg = "";
        for (int i = 0; i < line.length(); i++)  {
            char c = line.charAt(i);
            boolean skip = false;
            if (c == Keywords.ESCAPE_CHARACTER_KEYWORD) {
                //skip next character
                skip = true;
                i++;
            }

            if (!skip) {
                //begin/end string expression
                if (c == Keywords.STRING_LITERAL_KEYWORD) {
                    //check for end string
                    if (!tokenStack.isEmpty() && tokenStack.peek() == ExpressionType.String) {
                        //end string
                        tokenStack.pop();
                    }
                    //check for begin string
                    else {
                        //don't add this character if it isn't inside any expression
                        //begin string
                        tokenStack.add(ExpressionType.String);
                    }
                }
                //if not currently in a string
                else if (tokenStack.isEmpty() || tokenStack.peek() != ExpressionType.String) {
                    //check for begin expression
                    if (c == Keywords.OPEN_PAREN_KEYWORD) {
                        //begin expression
                        tokenStack.add(ExpressionType.Expression);
                    }

                    //check for end expression
                    else if (c == Keywords.CLOSE_PAREN_KEYWORD) {
                        //check that closing parenthesis is valid
                        if (!tokenStack.isEmpty() && tokenStack.peek() == ExpressionType.Expression) {
                            //end expression
                            tokenStack.pop();
                        }
                        //does not have matching parenthesis
                        else {
                            if (tokenStack.isEmpty()) {
                                throw new Error("No opening parenthesis");
                            }
                            else {
                                throw new Error("Must close previous expression");
                            }
                        }
                    }

                    //check for beginning of array
                    else if (c == Keywords.OPEN_ARRAY_KEYWORD) {
                        //begin array index or definition
                        tokenStack.add(ExpressionType.Array);
                    }

                    //check for end of array
                    else if (c == Keywords.CLOSE_ARRAY_KEYWORD) {
                        //check that closing parenthesis is valid
                        if (!tokenStack.isEmpty() && tokenStack.peek() == ExpressionType.Array) {
                            //end expression
                            tokenStack.pop();
                        }
                        //does not have matching parenthesis
                        else {
                            if (tokenStack.isEmpty()) {
                                throw new Error("No opening square bracket");
                            }
                            else {
                                throw new Error("Must close previous expression");
                            }
                        }
                    }
                    
                }
            }

            if (c == Keywords.SEPARATOR_KEYWORD && tokenStack.isEmpty()) {
                args.add(currentArg);
                currentArg = "";
            }
            else {
                currentArg += c;
            }
        }
        args.add(currentArg);

        return args;
    }


    public static Variable interpretExpression(String line, Environment env) {
        return eval(line, env);
    }
    //ily calvin
    //calvin++

    //ily calvin 3000
    //           ||||                                                        number
    //env.getVariable("i").setData(env.getVariable("i").addTo(new Variable(          )).getData());
    //calvin += 3000

    public static void defineVariable(Environment env, String line) {
        String[] tokens = line.split(" ");
        if (line.length() >= 3) {
            boolean validName = true;
            if (!isAlpha(tokens[0].charAt(0))) {
                validName = false;
            }
            for (char c : tokens[0].toCharArray()) {
                if (!isIdentifierChar(c)) {
                    validName = false;
                }
            }
            if (!validName) {
                throw new Error("Invalid identifier name");
            }
            
        }
        else {
            throw new Error("Could not read variable definition");
        }
    }

    public static void increment(Environment env, String line) {
        String variableName = line.split(" ")[1];
        String afterVariable = line.substring(line.indexOf(variableName) + variableName.length()); 
        Variable incrementBy = interpretExpression(afterVariable, env);
        env.getVariable(variableName).setData(env.getVariable(variableName).addTo(incrementBy));
    } 
    public static void decrement(Environment env, String line) {
        String variableName = line.split(" ")[1];
        String afterVariable = line.substring(line.indexOf(variableName) + variableName.length()); 
        Variable incrementBy = interpretExpression(afterVariable, env);
        //get data of variable from interpret expression chec kif integer cast to integer and make the integer negative then add to and then
        if (incrementBy.getData() instanceof Integer) {
            int decrementValue = (int) incrementBy.getData();
            env.getVariable(variableName).setData(env.getVariable(variableName).addTo(new Variable(-decrementValue)));
        } else {
            throw new Error("type has to be double or integer");
        }
        if (incrementBy.getData() instanceof Double) {
            double decrementValue = (double) incrementBy.getData();
            env.getVariable(variableName).setData(env.getVariable(variableName).addTo(new Variable(-decrementValue)));
        } else {
            throw new Error("type has to be double or integer");
        }
    }

    /* TODO read this
     * interpretNumberExpression
     * should take a string containing numbers, variables, and functions
     * containing NO SPACES and use order of operations
     * you can put any expression within parenthesis back into interpretExpression
     * same with really anything between operators
     * for example:
     * 3.5*sqrt(9)+6/(3/2)
     *     |||||||   |||||
     * you can put these sections into interpretExpression
     * have fun :)
     * 
     */

    //you can use interpretExpression on each segment of the expression
    //for example
    //1+2*3-(sqrt(9)/2)
    //| | | ||||||||||| <- this 
    

    public double interpretNumberExpression(String line, Environment env) {
        if (line.contains(" ")) {
            // TODO: throw some error here
        }
        
        String expression = line;
        
        while (expression.contains("(") || expression.contains("")) {
            int open = expression.indexOf("(");
            int close = expression.indexOf(")");
            if (open < close && open != -1) {
                expression = expression.substring(0, open) + interpretNumberExpression(expression.substring(open + 1, close), env) + expression.substring(close);
            }
            else {
                // TODO: throw some error here
            }
        }
        

        // int left = 0;
        // int right = 0;
        // for (char i : line.toCharArray()) {
        //     right++;
        //     if(special.contains(i + "")) {
        //         if (i=='+') {
        //             return Integer.parseInt(line.substring(left,right)) + interpretNumberExpression(line.substring(right), variables, functions);
        //         }
        //         else if (i=='-') {
        //             return Integer.parseInt(line.substring(left,right)) - interpretNumberExpression(line.substring(right), variables, functions);
        //         }
        //         else if (i=='*') {
        //             return Integer.parseInt(line.substring(left,right)) * interpretNumberExpression(line.substring(right), variables, functions);
        //         }
        //         else if (i=='/') {
        //             return Integer.parseInt(line.substring(left,right)) / interpretNumberExpression(line.substring(right), variables, functions);
        //         }
        //         else if (i=='-') {
        //             return Math.pow(Integer.parseInt(line.substring(left,right)), interpretNumberExpression(line.substring(right), variables, functions));
        //         }
        //         else if (i=='(') {
        //             left = right;
        //             right = line.indexOf(")");
                    
        //         }
        //     }
        // }
        return 0.0;
    }

    public static boolean isAlpha(char c) {
        String alpha = "abcdefghijklmnopqrstuvwxyz";
        return (alpha.contains(c + "") || alpha.toUpperCase().contains(c + ""));
    }
    public static boolean isDigit(char c) {
        if ("0123456789".contains(c + "")) {
            return true;
        }
        return false;
    }
    public static boolean isIdentifierChar(char c) {
        return (isAlpha(c) || isDigit(c) || c == '_');
    }
}

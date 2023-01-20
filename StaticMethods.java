import java.util.*;

public class StaticMethods {
    
    //types of expressions used in token types
    private enum TokenType {
        None, Number, String, Expression, Array, Identifier, Operator, Joiner
    }
    //types of expressions used in final list
    private enum FinalType {
        Evaluated, Number, Array, Operator, Negative
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
                return indent;
            }
        }
        //gets to end of line without any characters, could be ambiguous.
        return -1;
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
        line = line.strip();
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
                            while (isIdentifierChar(c) && i + 1 < line.length()) {
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
                    else if (isDigit(c) || c == '.') {
                        //will need to be evaluated
                        if (tokenStack.isEmpty()) {
                            while (isDigit(c) && i < line.length()) {
                                currentToken += c;
                                i++;
                                if (i < line.length()) {
                                    c = line.charAt(i);
                                }
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

        //remove double joiners
        for (int t = tokenTypes.size() - 2; t > 0; t--) {
            if (tokenTypes.get(t) == tokenTypes.get(t + 1) && tokenTypes.get(t) == TokenType.Joiner) {
                tokenTypes.remove(t);
                tokenVariables.remove(t);
            }
        }
        
        //process token combinations
        List<Variable> finalVariables = new ArrayList<Variable>();
        List<FinalType> finalTypes = new ArrayList<FinalType>();
        for (int t = 0; t < tokenTypes.size(); t++) {
            TokenType currentType = tokenTypes.get(t);
            Variable currentVariable = tokenVariables.get(t);
            System.out.println(tokenTypes.get(t) + ": " + tokenVariables.get(t));
            if (currentType == TokenType.Expression) {
                finalVariables.add(eval(currentVariable.toString(), env));
                finalTypes.add(FinalType.Evaluated);
            }

            //identifier (function, variable, etc)
            else if (currentType == TokenType.Identifier) {
                //there is another token after it
                if (t + 1 < tokenTypes.size()) {
                    TokenType nextType = tokenTypes.get(t + 1);

                    //identifies function
                    if (nextType == TokenType.Expression) {
                        t++;
                        List<Variable> arguments = new ArrayList<Variable>();
                        Variable nextVariable = tokenVariables.get(t);
                        //adds arguments to function parameters
                        for (String arg : separate(nextVariable.toString())) {
                            arguments.add(eval(arg, env));
                        }
                        Function func = env.getFunction(currentVariable.toString(), arguments);
                        if (func != null) {
                            finalVariables.add(func.run(arguments));
                            finalTypes.add(FinalType.Evaluated);
                        }
                        else {
                            throw new Error("Function not found");
                        }
                    }
                    
                    //identifies array index or slice
                    else if (nextType == TokenType.Array) {
                        //TODO do something
                    }

                    //just a variable
                    else if (nextType == TokenType.Joiner || nextType == TokenType.Operator) {
                        //add the value of that variable
                        finalVariables.add(env.getVariable(currentVariable.toString()));
                        finalTypes.add(FinalType.Evaluated);
                        //do nothing, just figure out at next step
                    }

                    else {
                        throw new Error("Incorrect token found!");
                    }
                }

                //just a variable
                else {
                    finalVariables.add(env.getVariable(currentVariable.toString()));
                    finalTypes.add(FinalType.Evaluated);
                }
            }

            //string
            else if (currentType == TokenType.String) {
                if (t + 1 < tokenTypes.size()) {
                    TokenType nextType = tokenTypes.get(t + 1);
                    if (nextType == TokenType.Array) {
                        //TODO slice the string
                    }
                    //regular string
                    else if (nextType == TokenType.Joiner) {
                        finalVariables.add(currentVariable);
                        finalTypes.add(FinalType.Evaluated);
                    }
                    else {
                        throw new Error("Incorrect token found!");
                    }
                }
                //regular string
                else {
                    finalVariables.add(currentVariable);
                    finalTypes.add(FinalType.Evaluated);
                }
                
            }

            //regular number
            else if (currentType == TokenType.Number) {
                finalVariables.add(currentVariable);
                finalTypes.add(FinalType.Evaluated);
            }

            //possibly new array
            else if (currentType == TokenType.Array) {

            }

            //operator
            else if (currentType == TokenType.Operator) {
                finalVariables.add(currentVariable);
                finalTypes.add(FinalType.Operator);
            }
        }
        if (finalTypes.get(finalTypes.size() - 1) == FinalType.Operator) {
            throw new Error("Must have expression after operator");
        }
        //parse for number expressions
        List<Variable> vars = new ArrayList<Variable>();
        List<Character> ops = new ArrayList<Character>();
        boolean isNumberExpression = false;
        int expStart = 0;
        for (int f = 0; f < finalTypes.size(); f++) {
            //beginning of number expression or just regular expression
            if (!isNumberExpression && finalTypes.get(f) == FinalType.Evaluated) {
                isNumberExpression = true;
                expStart = f;
            }
            //same parity as beginning index and variable
            if (isNumberExpression && f % 2 == expStart % 2 && finalTypes.get(f) == FinalType.Evaluated) {
                vars.add(finalVariables.get(f));
            }
            //different parity as beginning index and operator
            else if (isNumberExpression && f % 2 == expStart % 2 && finalTypes.get(f) == FinalType.Operator) {
                ops.add((char) finalVariables.get(f).getData());
            }
            //end number expression 
            else {
                //just join them
                if (finalTypes.get(f) == FinalType.Evaluated) {
                    //single variable, leave as is
                    if (vars.size() == 1) {
                        
                    }
                    //longer, number expression
                    else {
                        System.out.println(vars);
                        System.out.println(ops);
                        Variable evaluatedNumber = interpretNumberExpression(vars, ops);
                        while (f != expStart) {
                            finalTypes.remove(f);
                            finalVariables.remove(f);
                            f--;
                        }
                        finalVariables.set(f, evaluatedNumber);
                        vars.clear();
                        ops.clear();
                        isNumberExpression = false;
                    }
                }
                //TODO throw error
            }
        }
        if (isNumberExpression) {
            if (vars.size() > ops.size()) {
                System.out.println(vars);
                System.out.println(ops);
                Variable evaluatedNumber = interpretNumberExpression(vars, ops);
                int f = finalTypes.size() - 1;
                while (f != expStart) {
                    finalTypes.remove(f);
                    finalVariables.remove(f);
                    f--;
                }
                finalVariables.set(f, evaluatedNumber);
                vars.clear();
                ops.clear();
                isNumberExpression = false;
            }
            else {
                throw new Error("Must have expression after operator");
            }
        }
        if (finalVariables.size() == 0) {
            return new Variable();
        }
        if (finalVariables.size() == 1) {
            return finalVariables.get(0);
        }
        for (Variable fv : finalVariables) {
            result = result.addTo(new Variable(fv.toString()));
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
    
    public static Variable defineVariable(Environment env, String line) {
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
            String expression = line.substring(line.indexOf(Keywords.DEFINE_VARIABLE_KEYWORD) + Keywords.DEFINE_VARIABLE_KEYWORD.length());
            Variable evaluated = eval(expression, env);
            env.putVariable(tokens[0], evaluated);
            return evaluated;
        }
        else {
            throw new Error("Could not read variable definition");
        }
    }

    // wdym TYPE NAME type1 name1 type2 name2 :)
    public static Function defineFunction(Environment env, String defLine) {
        String[] tokens = defLine.split(" ");
        String returnType = tokens[TokenIndex.DEFINE_FUNCTION_TYPE_TOKEN];
        String funcName = tokens[TokenIndex.DEFINE_FUNCTION_NAME_TOKEN];
        List<String> paramTypes = new ArrayList<String>();
        List<String> paramNames = new ArrayList<String>();

        for (int t = TokenIndex.MIN_DEFINE_LEN - 1; t < tokens.length - 1; t += 2) {
            paramTypes.add(tokens[t]);
            paramTypes.add(tokens[t + 1]);
        }

        Function func = new Function("", paramNames, paramTypes, returnType, funcName);
        env.putFunction(func);
        return func;
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

    
    //TODO Esther do this method
    public static Variable interpretNumberExpression(List<Variable> variables, List<Character> operators) {
        List<Variable> vars = new ArrayList<Variable>(variables);
        List<Character> ops = new ArrayList<Character>(operators);

        char pow = Keywords.OPERATOR_CHARACTERS.charAt(5),
            mod = Keywords.OPERATOR_CHARACTERS.charAt(4),
            divide = Keywords.OPERATOR_CHARACTERS.charAt(3),
            times = Keywords.OPERATOR_CHARACTERS.charAt(2),
            subtract = Keywords.OPERATOR_CHARACTERS.charAt(1),
            add = Keywords.OPERATOR_CHARACTERS.charAt(0);

        while (ops.contains(pow)) {
            int index = ops.indexOf(pow);
            vars.set(index, new Variable(Math.pow((double) vars.get(index).getData(), (double) vars.get(index + 1).getData())));
            vars.remove(index + 1);
            ops.remove(index);
        }
        //TODO ESTHERRRRRRRRR
        while (ops.contains(times)) {
            int index = 0;
            vars.set(index, new Variable((double) vars.get(index).getData() % (double) vars.get(index + 1).getData()));
            vars.remove(index + 1);
            ops.remove(index);
        }
        while (ops.contains(subtract)) {
            int index = ops.indexOf(pow);
            vars.set(index, new Variable((double) vars.get(index).getData() - (double) vars.get(index + 1).getData()));
            vars.remove(index + 1);
            ops.remove(index);
        }
        while (ops.contains(add)) {
            int index = ops.indexOf(pow);
            vars.set(index, new Variable(vars.get(index).addTo(vars.get(index + 1))));
            vars.remove(index + 1);
            ops.remove(index);
        }
        
        if (vars.size() != 1 || ops.size() != 0) {
            System.out.println("this is bad");
        }
        return vars.get(0);
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
    
    //method that removes all spaces from the start and end of the string
    public static String stripSpaces(String input) {
        String output = input;
        while (output.charAt(0) == ' ') {
            output = output.substring(1);
        }
        while (output.charAt(output.length() - 1) == ' ') {
            output = output.substring(0, output.length() - 1);
        }
        return output;
    }
}

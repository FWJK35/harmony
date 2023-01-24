/*
 * StaticMethods contains methods that are used across many
 * other classes, such as code to evaluate arguments in functions
 * EVAL TOOK SO LONG
 * Also contains helper methods that modify/check arguments,
 * such as isBlank and isAlpha. Also contains helper methods for 
 * other functions in this class
 */

import java.util.*;
import javax.swing.JOptionPane;

public class StaticMethods {
    //types of expressions used in token types
    private enum TokenType {
        None, Number, String, Expression, Array, Identifier, Operator, BoolOperator, Joiner
    }
    //types of expressions used in final list
    private enum FinalType {
        Evaluated, Operator, BoolOperator
    }
    //types of expressions used in expression stack
    private enum ExpressionType {
        None, Expression, Array, String, Identifier
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
        line = stripSpaces(line);
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
                                if (i == line.length()) {
                                    break;
                                }
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
                                    if (i < line.length()) {
                                        c = line.charAt(i);
                                    }
                                } while (isIdentifierChar(c) && i < line.length());
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
            if (currentType == TokenType.Expression) {
                finalVariables.add(eval(currentVariable.toString(), env));
                finalTypes.add(FinalType.Evaluated);
            }

            //identifier (function, variable, etc)
            else if (currentType == TokenType.Identifier) {
                String idName = currentVariable.toString();
                //boolean keyword
                boolean boolKeyword = false;
                for (String word : Keywords.BOOLEAN_KEYWORDS) {
                    if (idName.equals(word)) {
                        boolKeyword = true;
                        break;
                    }
                }
                if (boolKeyword) {
                    finalVariables.add(currentVariable);
                    finalTypes.add(FinalType.BoolOperator);
                }
                else if (Keywords.isIllegalEvalIdentifier(idName)) {
                    throw new Error("Illegal identifier name: " + idName);
                }
                //there is another token after it and it isnt an eval keyword
                else if (t + 1 < tokenTypes.size() && 
                        !(Keywords.isIllegalIdentifier(idName) && !Keywords.isIllegalEvalIdentifier(idName))) {
                    TokenType nextType = tokenTypes.get(t + 1);

                    //identifies function
                    if (nextType == TokenType.Expression) {
                        t++;
                        List<Variable> arguments = new ArrayList<Variable>();
                        Variable nextVariable = tokenVariables.get(t);
                        //adds arguments to function parameters
                        for (String arg : separate(nextVariable.toString(), Keywords.SEPARATOR_KEYWORD)) {
                            arguments.add(eval(arg, env));
                        }
                        Function func = env.getFunction(idName, arguments);
                        if (func != null) {
                            finalVariables.add(func.run(arguments));
                            finalTypes.add(FinalType.Evaluated);
                        }
                        else {
                            throw new Error("Function " + idName + " not found");
                        }
                    }

                    //just a variable
                    else if (nextType == TokenType.Joiner || nextType == TokenType.Operator || 
                            nextType == TokenType.BoolOperator || nextType == TokenType.Array) {
                        //add the value of that variable
                        if (!env.containsVariable(idName)) {
                            throw new Error("Variable name not found: " + idName);
                        }
                        finalVariables.add(env.getVariable(idName));
                        finalTypes.add(FinalType.Evaluated);
                        //do nothing, just figure out at next step
                    }

                    else {
                        throw new Error("Incorrect token found for expression: " + line);
                    }
                }

                //just a variable or eval keyword
                else {
                    //eval keyword
                    if (Keywords.isIllegalIdentifier(idName) && !Keywords.isIllegalEvalIdentifier(idName)) {
                        if (idName.equals(Keywords.INPUT_KEYWORD)) {
                            finalVariables.add(new Variable(input()));
                            finalTypes.add(FinalType.Evaluated);
                        }
                        else if (idName.equals(Keywords.TRUE_KEYWORD)) {
                            finalVariables.add(new Variable(true));
                            finalTypes.add(FinalType.Evaluated);
                        }
                        else if (idName.equals(Keywords.FALSE_KEYWORD)) {
                            finalVariables.add(new Variable(false));
                            finalTypes.add(FinalType.Evaluated);
                        }

                    }
                    else {
                        if (!env.containsVariable(idName)) {
                            throw new Error("Variable name not found: " + idName);
                        }
                        finalVariables.add(env.getVariable(idName));
                        finalTypes.add(FinalType.Evaluated);
                    }
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

            //possibly new array or slice
            //arrtest aka [1, 2, 3]
            //xd arrtest[0]
            //
            else if (currentType == TokenType.Array) {
                
            }

            //operator
            else if (currentType == TokenType.Operator) {
                finalVariables.add(currentVariable);
                finalTypes.add(FinalType.Operator);
            }

            else if (currentType == TokenType.BoolOperator) {
                finalVariables.add(currentVariable);
                finalTypes.add(FinalType.BoolOperator);
            }
        }
        if (finalTypes.size() > 0 && finalTypes.get(finalTypes.size() - 1) == FinalType.Operator) {
            throw new Error("Must have expression after operator");
        }
        //parse for number expressions
        List<Variable> vars = new ArrayList<Variable>();
        List<Character> ops = new ArrayList<Character>();
        boolean isNumberExpression = false;
        int expStart = 0;
        //collapse number expressions
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
            else if (isNumberExpression && (f + 1) % 2 == expStart % 2 && finalTypes.get(f) == FinalType.Operator) {
                ops.add((char) finalVariables.get(f).getData());
            }
            //end number expression 
            else {
                //just join them
                if (finalTypes.get(f) == FinalType.Evaluated || finalTypes.get(f) == FinalType.BoolOperator) {
                    //single variable, leave as is
                    if (vars.size() <= 1) {
                        isNumberExpression = false;
                        vars.clear();
                        ops.clear();
                        //f -= vars.size();
                    }
                    //longer, number expression
                    else {
                        Variable evaluatedNumber = interpretNumberExpression(vars, ops);
                        f--;
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
                else if (finalTypes.get(f) == FinalType.Operator) {
                    throw new Error("Cannot have double operator");
                }
            }
        }
        if (isNumberExpression) {
            if (vars.size() > ops.size()) {
                //single variable, leave as is
                if (vars.size() == 1) {
                    isNumberExpression = false;
                    vars.clear();
                    ops.clear();
                }
                //longer, number expression
                else {
                    Variable evaluatedNumber = interpretNumberExpression(vars, ops);
                    int f = finalTypes.size() - 1;
                    while (f != expStart) {
                        finalTypes.remove(f);
                        finalVariables.remove(f);
                        f--;
                    }
                    finalVariables.set(f, evaluatedNumber);
                }
            }
            else {
                throw new Error("Must have expression after operator");
            }
        }
        vars.clear();
        
        if (finalVariables.size() == 0) {
            return new Variable();
        }
        if (finalVariables.size() == 1) {
            return finalVariables.get(0);
        }
        
        //combine things before boolean evaluation
        List<String> boolOps = new ArrayList<String>();
        for (int f = 0; f < finalTypes.size(); f++) {
            //current and next one are both evaluated
            if (finalTypes.get(f) == FinalType.Evaluated) {
                if (f + 1 < finalTypes.size() && finalTypes.get(f + 1) == FinalType.Evaluated) {
                    finalVariables.set(f, Variable.combine(
                        new Variable(
                            finalVariables.get(f)
                        .toString()),
                        new Variable(finalVariables.get(f + 1).toString())));
                    finalTypes.remove(f + 1);
                    finalVariables.remove(f + 1);
                    f--;
                }
            }
            else if (finalTypes.get(f) == FinalType.BoolOperator) {
                if (f + 1 >= finalTypes.size() || finalTypes.get(f + 1) == FinalType.BoolOperator || f == 0) {
                    throw new Error("Error with boolean operators");
                }
                boolOps.add(finalVariables.get(f).toString());
                finalTypes.remove(f);
                finalVariables.remove(f);
            }
            else {
                throw new Error("something is stupid and I dont know what");
            }
        }
        if (finalVariables.size() == 1) {
            return finalVariables.get(0);
        }
        
        return new Variable(interpretBooleanExpression(finalVariables, boolOps));
    }

    public static List<String> separate(String line, char sep) {
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

            if (c == sep && tokenStack.isEmpty()) {
                args.add(currentArg);
                currentArg = "";
            }
            else {
                currentArg += c;
            }
        }
        if (!isBlank(currentArg)) {
            args.add(currentArg);
        }
        
        for (int arg = 0; arg < args.size(); arg++) {
            args.set(arg, stripSpaces(args.get(arg)));
        }
        return args;
    }

    public static Variable interpretExpression(String line, Environment env) {
        return eval(line, env);
    }
    
    public static Variable defineVariable(Environment env, String line) {
        line = StaticMethods.stripSpaces(line);
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
            String expression = line.substring(line.indexOf(" " + Keywords.DEFINE_VARIABLE_KEYWORD + " ") + Keywords.DEFINE_VARIABLE_KEYWORD.length() + 2);
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
        String funcName = tokens[TokenIndex.DEFINE_FUNCTION_NAME_TOKEN];
        List<String> paramTypes = new ArrayList<String>();
        List<String> paramNames = new ArrayList<String>();

        for (int t = TokenIndex.MIN_DEFINE_LEN - 1; t < tokens.length - 1; t += 2) {
            int typeIndex = -1;
            for (int type = 0; type < Keywords.HARMONY_TYPES.length; type++) {
                if (tokens[t].equals(Keywords.HARMONY_TYPES[type])) {
                    typeIndex = type;
                }
            }
            if (typeIndex < 0) {
                throw new Error("Invalid type name " + tokens[t]);
            }
            paramTypes.add(Keywords.JAVA_TYPES[typeIndex]);
            paramNames.add(tokens[t + 1]);
        }

        Function func = new Function("", env, paramNames, paramTypes, funcName);
        env.putFunction(func);
        return func;
    }
    public static SystemFunction defineSystemFunction(Environment env, String defLine) {
        String[] tokens = defLine.split(" ");
        String funcName = tokens[TokenIndex.DEFINE_FUNCTION_NAME_TOKEN];
        List<String> paramTypes = new ArrayList<String>();
        List<String> paramNames = new ArrayList<String>();

        for (int t = TokenIndex.MIN_DEFINE_LEN - 1; t < tokens.length - 1; t += 2) {
            int typeIndex = -1;
            for (int type = 0; type < Keywords.HARMONY_TYPES.length; type++) {
                if (tokens[t].equals(Keywords.HARMONY_TYPES[type])) {
                    typeIndex = type;
                }
            }
            if (typeIndex < 0) {
                throw new Error("Invalid type name " + tokens[t]);
            }
            paramTypes.add(Keywords.JAVA_TYPES[typeIndex]);
            paramNames.add(tokens[t + 1]);
        }

        SystemFunction func = new SystemFunction(env, paramNames, paramTypes, funcName);
        env.putFunction(func);
        return func;
    }

    public static void increment(Environment env, String line) {
        String variableName = line.split(" ")[1];
        String afterKeyword = line.substring(Keywords.INCREMENT_KEYWORD.length());
        String afterVariable = afterKeyword.substring(afterKeyword.indexOf(variableName) + variableName.length()); 
        Variable incrementBy = interpretExpression(afterVariable, env);
        if (!env.containsVariable(variableName)) {
            throw new Error("Variable name not found: " + variableName);
        }
        if (incrementBy.getData() == null) {
            if (env.getVariable(variableName).getData() instanceof Integer || env.getVariable(variableName).getData() instanceof Double) {
                env.getVariable(variableName).setData(Variable.combine(env.getVariable(variableName), new Variable(1)).getData());
            }
        }
        else {
            env.getVariable(variableName).setData(Variable.combine(env.getVariable(variableName), incrementBy).getData());
        }
    } 

    public static void decrement(Environment env, String line) {
        String variableName = line.split(" ")[1];
        String afterKeyword = line.substring(Keywords.INCREMENT_KEYWORD.length());
        String afterVariable = afterKeyword.substring(afterKeyword.indexOf(variableName) + variableName.length()); 
        Variable incrementBy = interpretExpression(afterVariable, env);
        //get data of variable from interpret expression chec kif integer cast to integer and make the integer negative then add to and then
        if (incrementBy.getData() == null) {
            if (env.getVariable(variableName).getData() instanceof Integer || env.getVariable(variableName).getData() instanceof Double) {
                env.getVariable(variableName).setData(Variable.combine(env.getVariable(variableName), new Variable(1)).getData());
                return;
            }
        }
        if (incrementBy.getData() instanceof Integer) {
            int decrementValue = -(int) incrementBy.getData();
            env.getVariable(variableName).setData(Variable.combine(env.getVariable(variableName), new Variable(decrementValue)).getData());
        }
        else if (incrementBy.getData() instanceof Double) {
            double decrementValue = -(double) incrementBy.getData();
            if (!env.containsVariable(variableName)) {
                throw new Error("Variable name not found: " + variableName);
            }
            env.getVariable(variableName).setData(Variable.combine(env.getVariable(variableName), new Variable(decrementValue)).getData());
        } 
        else {
            throw new Error("Type has to be double or integer");
        }
    }

    public static String input() {
        return JOptionPane.showInputDialog("");
    }

    public static List<Variable> arrayInitialize(String input, Environment env) {
        List<Variable> output = new ArrayList<Variable>();
        for (String term : separate(input, Keywords.ARRAY_SEPARATOR_KEYWORD)) {
            output.add(eval(term, env));
        }
        return output;
    }

    public static List<Variable> arraySplit(List<Variable> array, int start, int end, int increment) {
        List<Variable> output = new ArrayList<Variable>();
        if (start < 0 || end > array.size()) {
            throw new Error("Index out of bounds, start: " + start + ", end: " + end + ", size: " + array.size());
        }
        for (int i = start; i < end; i += increment) {
            output.add(array.get(i));
        }
        return output;
    }

    // evaluates a boolean expression, returns a boolean
    // takes an expression separated into a Variable list and String list of boolean operators and (in)equalities (<, >, ==)
    public static boolean interpretBooleanExpression(List<Variable> variables, List<String> operators) {
        List<Variable> vars = new ArrayList<Variable>(variables);
        List<String> ops = new ArrayList<String>(operators);
        if (variables.size() != operators.size() + 1) {
            throw new Error("Mismatched variables and operators: " + variables.size() + " Variables, " + operators.size() + " Operators");
        }

        // processes references to boolean values (true/false)
        for (Variable var : vars) {
            if (var.getData().equals(Keywords.TRUE_KEYWORD)) {
                var.setData(true);
            }
            else if (var.getData().equals(Keywords.FALSE_KEYWORD)) {
                var.setData(false);
            }
        }

        // checks for equality comparisons (==)
        while (ops.contains(Keywords.EQUALS_KEYWORD)) {
            int index = ops.indexOf(Keywords.EQUALS_KEYWORD);
            vars.set(index, new Variable(vars.get(index).equals(vars.get(index + 1))));
            vars.remove(index + 1);
            ops.remove(index);
        }

        // inequality comparisons (<, >)
        while (ops.contains(Keywords.GREATER_KEYWORD)) {
            int index = ops.indexOf(Keywords.GREATER_KEYWORD);
            vars.set(index, new Variable(vars.get(index).toDouble() > vars.get(index + 1).toDouble()));
            vars.remove(index + 1);
            ops.remove(index);
        }
        while (ops.contains(Keywords.LESSER_KEYWORD)) {
            int index = ops.indexOf(Keywords.LESSER_KEYWORD);
            vars.set(index, new Variable(vars.get(index).toDouble() < vars.get(index + 1).toDouble()));
            vars.remove(index + 1);
            ops.remove(index);
        }

        // checks for boolean operators (or, and)
        while (ops.contains(Keywords.OR_KEYWORD)) {
            int index = ops.indexOf(Keywords.OR_KEYWORD);
            vars.set(index, new Variable((boolean) vars.get(index).getData() || (boolean) vars.get(index + 1).getData()));
            vars.remove(index + 1);
            ops.remove(index);
        }
        while (ops.contains(Keywords.AND_KEYWORD)) {
            int index = ops.indexOf(Keywords.AND_KEYWORD);
            vars.set(index, new Variable((boolean) vars.get(index).getData() && (boolean) vars.get(index + 1).getData()));;
            vars.remove(index + 1);
            ops.remove(index);
        }

        // checks that all variables and operators have been processed
        if (vars.size() != 1 || ops.size() != 0) {
            throw new Error("Remaining Variables in List: " + vars.size() + ", Remaining Operators in List: " + ops.size());
        }

        return vars.get(0).toBoolean();
    }

    // evaluates a numerical expression using PEMDAS (no parenthesis, modulus included) and returns a Variable
    // takes an expression separated into a Variables list of length n and String list of mathematical operators of length n-1
    public static Variable interpretNumberExpression(List<Variable> variables, List<Character> operators) {
        List<Variable> vars = new ArrayList<Variable>(variables);
        List<Character> ops = new ArrayList<Character>(operators);

        if (variables.size() != operators.size() + 1) {
            throw new Error("Mismatched variables and operators: " + variables.size() + " Variables, " + operators.size() + " Operators");
        }

        char pow = Keywords.OPERATOR_CHARACTERS.charAt(5),
            mod = Keywords.OPERATOR_CHARACTERS.charAt(4),
            divide = Keywords.OPERATOR_CHARACTERS.charAt(3),
            times = Keywords.OPERATOR_CHARACTERS.charAt(2),
            subtract = Keywords.OPERATOR_CHARACTERS.charAt(1),
            add = Keywords.OPERATOR_CHARACTERS.charAt(0);

        // exponential operator processing
        while (ops.contains(pow)) {
            int index = ops.indexOf(pow);
            vars.set(index, new Variable(Math.pow(vars.get(index).toDouble(), vars.get(index + 1).toDouble())));
            vars.remove(index + 1);
            ops.remove(index);
        }
        // multiplication, division, modulus operator processing (left to right)
        while (ops.contains(times) || ops.contains(divide) || ops.contains(mod)) {
            int index = posMin(ops.indexOf(times), ops.indexOf(divide), ops.indexOf(mod));
            char op = ops.get(index);
            double num;
            if (op == times) {
                num = vars.get(index).toDouble() * vars.get(index + 1).toDouble();
            }
            else if (op == divide) {
                num = vars.get(index).toDouble() / vars.get(index + 1).toDouble();
            }
            else {
                num = vars.get(index).toDouble() % vars.get(index + 1).toDouble();
            }

            if ((int) num == num) {
                vars.set(index, new Variable((int) num));
            }
            else {
                vars.set(index, new Variable(num));
            }
            vars.remove(index + 1);
            ops.remove(index);
        }
        // subtraction processing
        while (ops.contains(subtract)) {
            int index = ops.indexOf(subtract);
            vars.set(index, new Variable(vars.get(index).toDouble() - vars.get(index + 1).toDouble()));
            vars.remove(index + 1);
            ops.remove(index);
        }
        // addition processing
        while (ops.contains(add)) {
            int index = ops.indexOf(add);
            vars.set(index, Variable.combine(vars.get(index), vars.get(index + 1)));
            vars.remove(index + 1);
            ops.remove(index);
        }
        // checks that all variables and operators have been processed
        if (vars.size() != 1 || ops.size() != 0) {
            throw new Error("Remaining Variables in List: " + vars.size() + ", Remaining Operators in List: " + ops.size());
        }
        return vars.get(0);
    }

    // finds the positive minimum value among three integers
    private static int posMin(int a, int b, int c) {
        if ((b < 0 || a < b) && a >= 0) {
            if (c < a && c >= 0) {
                return c;
            }
            return a;
        }
        if ((c < 0 || b < c) && 0 <= b) {
            return b;
        }
        return c;
    } 

    public static void print(Variable var) {
        print(var.toString());
    }
    public static void print(String line) {
        // temporary print replacement
        System.out.println(line);
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
        String output = new String(input);
        if (isBlank(input)) {
            return "";
        }
        while (output.charAt(0) == ' ') {
            output = output.substring(1);
        }
        while (output.charAt(output.length() - 1) == ' ') {
            output = output.substring(0, output.length() - 1);
        }
        return output;
    }

    public static String stripTrailingSpaces(String input) {
        String output = new String(input);
        if (isBlank(input)) {
            return "";
        }
        while (output.charAt(output.length() - 1) == ' ') {
            output = output.substring(0, output.length() - 1);
        }
        return output;
    }

    // similar to Java 11's 
    public static boolean isBlank(String s) {
        for (char c : s.toCharArray()) {
            if (c != ' ') {
                return false;
            }
        }
        return true;
    }
}

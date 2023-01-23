import java.util.*;

public class Function {
    String code;
    Environment env;
    List<String> paramNames;
    List<String> paramTypes;
    String returnType;
    String name;
    Variable hasReturned;
    Variable returnValue;
    boolean isMainCall;
    int startLine;
    
    public Function(String code, Environment env, List<String> paramNames, List<String> paramTypes, String name) {
        this.name = name;
        this.code = code;
        this.env = env;
        this.paramNames = paramNames;
        this.paramTypes = paramTypes;
        this.hasReturned = new Variable(false);
        this.returnValue = new Variable();
        this.isMainCall = true;
    }

    public Function() {
        this.name = "";
        this.code = "";
        this.paramNames = new ArrayList<String>();
        this.paramTypes = new ArrayList<String>();
        this.returnType = "";
    }

    public List<String> getParamTypes() {
        return paramTypes;
    }

    public String getReturnType() {
        return returnType;
    }

    public String getName() {
        return name;
    }

    public void setCode(String newCode) {
        this.code = newCode;
    }

    public void setStartLine(int startLine) {
        this.startLine = startLine;
    }

    public Variable run(List<Variable> args){//, Object returnMain) {
        return run(args, new Variable(false), new Variable());
    }

    public Variable run(List<Variable> args, Variable hasReturned, Variable returnValue) {
        //add arguments to enviroment
        Environment env = this.env.copyEnvironment();
        for (int v = 0; v < paramNames.size(); v++) {
            env.putVariable(paramNames.get(v), args.get(v));
        }

        String[] lines = code.split("\n");
        int baseIndent = 0;
        boolean endOfFile = false;
        String line = "";
        
        for (int l = 0; l < lines.length; l++) {
            try {
            line = StaticMethods.stripTrailingSpaces(lines[l]);
            //System.out.println("line " + l + ": " + line);
            String doubleSpacesRemoved = StaticMethods.stripSpaces(line);
            while (doubleSpacesRemoved.contains("  ")) {
                doubleSpacesRemoved = doubleSpacesRemoved.replaceFirst("  ", " ");
            }
            String[] tokens = doubleSpacesRemoved.split(" ");
            //VERY first, check comment or blank line
            if (StaticMethods.isBlank(line) || doubleSpacesRemoved.substring(0, 
                    Keywords.COMMENT_KEYWORD.length()).equals(Keywords.COMMENT_KEYWORD)) {
                //ignore line
            }
            //fisrtly, check if should be multi line statement
            else if (line.charAt(line.length() - 1) == Keywords.LINE_JOINER_KEYWORD) {
                while (line.charAt(line.length()) == Keywords.LINE_JOINER_KEYWORD) {
                    if (l + 1 >= lines.length) {
                        throw new Error("Must have line after line joiner");
                    }
                    l++;
                    //remove joiner character
                    line = line.substring(0, line.length() - 1);
                    line += " " + StaticMethods.stripSpaces(lines[l]);
                }
            }

            //check for...
            
            //base indentation and indentation errors
            if (baseIndent == 0 && !StaticMethods.isBlank(line)) {
                baseIndent = StaticMethods.countIndent(line);
            }
            if (baseIndent > 0 && !StaticMethods.isBlank(line)) {
                if (StaticMethods.countIndent(line) != baseIndent) {
                    throw new Error("Incorrect indentation on line " + l);
                }
            }

            //return
            if (tokens.length > TokenIndex.RETURN_TOKEN && tokens[TokenIndex.RETURN_TOKEN].equals(Keywords.RETURN_KEYWORD)) {
                String toReturn = line.substring(Keywords.RETURN_KEYWORD.length() + 1);
                hasReturned.setData(true);
                returnValue.setData(StaticMethods.eval(toReturn, env).getData());
                return returnValue;
            }
            
            //variable declaration
            if (tokens.length > TokenIndex.DEFINE_VARIABLE_TOKEN && tokens[TokenIndex.DEFINE_VARIABLE_TOKEN].equals(Keywords.DEFINE_VARIABLE_KEYWORD)) {
                StaticMethods.defineVariable(env, line);
            }

            //variable modification
            if (tokens.length > TokenIndex.MODIFY_VARIABLE_TOKEN && tokens[TokenIndex.MODIFY_VARIABLE_TOKEN].equals(Keywords.INCREMENT_KEYWORD)) {
                StaticMethods.increment(env, line);
            } 
            if (tokens.length > TokenIndex.MODIFY_VARIABLE_TOKEN && tokens[TokenIndex.MODIFY_VARIABLE_TOKEN].equals(Keywords.DECREMENT_KEYWORD)) {
                StaticMethods.decrement(env, line);
            }

            //print line
            if (tokens.length > TokenIndex.PRINT_TOKEN && tokens[TokenIndex.PRINT_TOKEN].equals(Keywords.PRINT_KEYWORD)) {
                //TODO print to notepad, placeholder for now
                System.out.println(StaticMethods.eval(line.substring(line.indexOf(Keywords.PRINT_KEYWORD) + Keywords.PRINT_KEYWORD.length() + 1), env));
            }

            //TODO if
            if (tokens.length > TokenIndex.IF_STATEMENT_TOKEN && tokens[TokenIndex.IF_STATEMENT_TOKEN].equals(Keywords.IF_KEYWORD)) {
                //get all code inside the if statement
                //get condition statement
                String ifCondition = line.substring(line.indexOf(Keywords.IF_KEYWORD) + Keywords.IF_KEYWORD.length(), 
                    line.length() - Keywords.COLON_KEYWORD.length());
                l++;
                int startIf = l;
                //check line after
                if (l >= lines.length) {
                    endOfFile = true;
                    throw new Error("No next line found");
                }
                String nextLine = StaticMethods.stripTrailingSpaces(lines[l]);
                int blockIndent = StaticMethods.countIndent(nextLine);
                //check next indentation fine
                if (blockIndent <= baseIndent) {
                    throw new Error("Incorrect indentation on line " + l);
                }
                //get code inside if statement
                String nestedIfCode = "";
                while (StaticMethods.countIndent(nextLine) >= blockIndent || StaticMethods.countIndent(nextLine) == -1) {
                    nestedIfCode += nextLine + "\n";
                    l++;
                    if (l >= lines.length) {
                        endOfFile = true;
                        break;
                    }
                    nextLine = lines[l];
                }
                //go back to last line of the if block
                l--;

                //else statement exists
                int startElse = l + 1;
                String nestedElseCode = "";
                String lineAfter = StaticMethods.stripSpaces(nextLine);
                String wholeLineAfter = nextLine;
                //System.out.println("line after if block: " + wholeLineAfter);
                while (lineAfter.contains("  ")) {
                    lineAfter = lineAfter.replaceFirst("  ", " ");
                }
                //next line is else or elif
                if (!endOfFile && lineAfter.split(" ")[TokenIndex.ELSE_STATEMENT_TOKEN].equals(Keywords.ELSE_KEYWORD)) {
                    l += 2;
                    //check line after
                    if (l >= lines.length) {
                        endOfFile = true;
                        throw new Error("No next line found");
                    }
                    nextLine = lines[l];
                    blockIndent = StaticMethods.countIndent(nextLine);
                    //check indentation fine
                    if (blockIndent <= baseIndent) {
                        throw new Error("Incorrect indentation on line " + l);
                    }
                    while (StaticMethods.countIndent(nextLine) >= blockIndent || StaticMethods.countIndent(nextLine) == -1) {
                        nestedElseCode += nextLine + "\n";
                        l++;
                        if (l >= lines.length) {
                            endOfFile = true;
                            break;
                        }
                        nextLine = lines[l];
                    }
                    l--;
                }
                
                
                Function ifStatement = new Function(nestedIfCode, env.copyEnvironment(), new ArrayList<String>(), 
                        new ArrayList<String>(), Keywords.IF_KEYWORD);
                ifStatement.setStartLine(startLine + startIf);
                //should run if statement
                //System.out.println(ifCondition);
                if (StaticMethods.eval(ifCondition, env).toBoolean()) {
                    ifStatement.run(new ArrayList<Variable>(), hasReturned, returnValue);
                    if (hasReturned.toBoolean()) {
                        return returnValue;
                    }
                }
                else {
                    //check for elif
                    if (lineAfter.split(" ").length > TokenIndex.ELIF_STATEMENT_TOKEN && 
                            lineAfter.split(" ")[TokenIndex.ELIF_STATEMENT_TOKEN].equals(Keywords.IF_KEYWORD)) {
                        //just set next line to an if statement and go back to it
                        //System.out.println("found elif");
                        l = startElse;
                        lines[l] = wholeLineAfter.substring(0, wholeLineAfter.indexOf(Keywords.ELSE_KEYWORD)) + 
                                wholeLineAfter.substring(wholeLineAfter.indexOf(Keywords.IF_KEYWORD));
                        l--;
                    }
                    else if (!StaticMethods.isBlank(nestedElseCode)) {
                        //System.out.println("found else");
                        Function elseStatement = new Function(nestedElseCode, env.copyEnvironment(), new ArrayList<String>(), 
                            new ArrayList<String>(), Keywords.ELSE_KEYWORD);
                        elseStatement.setStartLine(startLine + startElse);
                        elseStatement.run(new ArrayList<Variable>(), hasReturned, returnValue);
                        if (hasReturned.toBoolean()) {
                            return returnValue;
                        }
                    }
                }
            }
            //finds else statement
            if (tokens.length > TokenIndex.ELSE_STATEMENT_TOKEN && tokens[TokenIndex.ELSE_STATEMENT_TOKEN].equals(Keywords.ELSE_KEYWORD)) {
                //should have already run or is elif, skip over it
                String nextLine = lines[l + 1];
                int blockIndent = StaticMethods.countIndent(line);
                while (StaticMethods.countIndent(nextLine) >= blockIndent || StaticMethods.countIndent(nextLine) == -1) {
                    l++;
                    if (l >= lines.length) {
                        endOfFile = true;
                        break;
                    }
                    nextLine = lines[l];
                }
                l--;
            }
            //TODO for
            //TODO while


            } catch (Error e) {
                //e.printStackTrace();
                for (int c = 0; c < lines.length; c++) {
                    if (lines[c].contains(line)) {
                        System.out.println("Possible error on line " + (c + 1 + startLine) + ": " + e.getMessage());
                    }
                }
                break;
            } 
        }
        return new Variable();
    }


    public Variable runHelper(List<Variable> args) {
        return new Variable();
    }

    public static Function getFunction(List<Function> functions, String name, List<Variable> inputs) {
        for (Function possible : functions) {
            if (name.equals(possible.getName()) && inputs.size() == possible.getParamTypes().size()) {
                boolean match = true;
                for (int i = 0; i < inputs.size(); i++) {
                    if (!(inputs.get(i).getType().equals(possible.getParamTypes().get(i)))) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    return possible;
                }
            }
        }
        return null;
    }

    public static List<Function> getFunction(List<Function> functions, String name) {
        List<Function> result = new ArrayList<Function>();
        for (Function possible : functions) {
            if (name.equals(possible.getName())) {
                result.add(possible);
            }
        }
        return result;
    }
    public String toString() {
        String out = "";
        out += "wdym " + returnType + " " + name + " ";
        for (int p = 0; p < paramTypes.size(); p++) {
            out += paramTypes.get(p) + " " + paramNames + " ";
        }
        out += ":)\n";
        out += code;
        return out;
    }
}

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
    
    public Function(String code, Environment env, List<String> paramNames, List<String> paramTypes, String returnType, String name) {
        this.name = name;
        this.code = code;
        this.env = env;
        this.paramNames = paramNames;
        this.paramTypes = paramTypes;
        this.returnType = returnType;
        this.hasReturned = new Variable(false);
        this.returnValue = new Variable();
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

    public Variable run(List<Variable> args){//, Object returnMain) {
        return run(args, new Variable(false), new Variable());
    }

    public Variable run(List<Variable> args, Variable hasReturned, Variable returnValue) {
        String[] lines = code.split("\n");
        for (int l = 0; l < lines.length; l++) {
            String line = lines[l];
            String[] tokens = line.split(" ");
            //fisrtly, check if should be multi line statement
            if (line.charAt(line.length()) == Keywords.LINE_JOINER_KEYWORD) {
                while (line.charAt(line.length()) == Keywords.LINE_JOINER_KEYWORD) {
                    if (l + 1 >= lines.length) {
                        throw new Error("Must have line after line joiner");
                    }
                    line += " " + StaticMethods.stripSpaces(codeScanner.nextLine());
                }
            }

            //check for...

            //return
            if (tokens[TokenIndex.RETURN_TOKEN].equals(Keywords.RETURN_KEYWORD)) {
                String toReturn = line.substring(Keywords.RETURN_KEYWORD.length() + 1);
                hasReturned.setData(true);
                returnValue.setData(StaticMethods.eval(toReturn, env).getData());
                return returnValue;
            }
            
            //variable declaration
            if (tokens[TokenIndex.DEFINE_VARIABLE_TOKEN].equals(Keywords.DEFINE_VARIABLE_KEYWORD)) {
                StaticMethods.defineVariable(env, line);
            }

            //variable modification
            if (tokens[TokenIndex.MODIFY_VARIABLE_TOKEN].equals(Keywords.INCREMENT_KEYWORD)) {
                StaticMethods.increment(env, line);
            } 
            if (tokens[TokenIndex.MODIFY_VARIABLE_TOKEN].equals(Keywords.DECREMENT_KEYWORD)) {
                StaticMethods.decrement(env, line);
            }

            //print line
            if (tokens[TokenIndex.PRINT_TOKEN].equals(Keywords.PRINT_KEYWORD)) {
                //TODO print to notepad, placeholder for now
                System.out.println(StaticMethods.eval(line.substring(Keywords.PRINT_KEYWORD.length() + 1), env));
            }

            //TODO if
            if (tokens[TokenIndex.IF_STATEMENT_TOKEN].equals(Keywords.PRINT_KEYWORD)) {
                
            }
            //TODO else
            //TODO for
            //TODO while
            
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

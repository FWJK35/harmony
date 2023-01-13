import java.util.*;

public class Function {
    String code;
    List<String> paramNames;
    List<String> paramTypes;
    String returnType;
    String name;
    
    public Function(String code, List<String> paramNames, List<String> paramTypes, String returnType, String name) {
        this.name = name;
        this.code = code;
        this.paramNames = paramNames;
        this.paramTypes = paramTypes;
        this.returnType = returnType;
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

    //TODO IMPORT COMPILER CODE HERE
    public Variable runFunction(String[] localParam) {
        return new Variable();
    }

    public Variable run(List<Variable> args) {
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
}

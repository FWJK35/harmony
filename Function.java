import java.util.*;

public class Function {
    String code;
    Map<String, String> params;
    List<String> paramTypes;
    String returnType;
    public Function(String code, Map<String, String> params, List<String> paramTypes, String returnType) {
        this.code = code;
        this.params = params;
        this.paramTypes = paramTypes;
        this.returnType = returnType;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public List<String> getParamTypes() {
        return paramTypes;
    }

    public String getReturnType() {
        return returnType;
    }

    public Variable run(Map<String, Variable> params) {
        Variable result = new Variable();
        
        return result;
    }

    public Variable runFunction(String[] localParam) {
        return Compiler.runCode(new Scanner(code));
    }

    public static Function getFunction(List<Function> functions, String name, List<Variable> inputs) {
        for (Function possible : functions) {
            boolean inputsMatch = true;
            if (possible.getParamTypes().size() == inputs.size()) {

            }
            else {
                
            }
        }
        return null;
    }
}

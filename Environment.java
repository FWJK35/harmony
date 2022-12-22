import java.util.*;

public class Environment {
    private Map<String, Variable> variables;
    private List<Function> functions;

    // constructors
    public Environment() {
        this.variables = new HashMap<String, Variable>();
        this.functions = new ArrayList<Function>();
    }
    public Environment(Map<String, Variable> variables) {
        this.variables = variables;
        this.functions = new ArrayList<Function>();
    }
    public Environment(List<Function> functions) {
        this.variables = new HashMap<String, Variable>();
        this.functions = functions;
    }
    public Environment(Map<String, Variable> variables, List<Function> functions) {
        this.variables = variables;
        this.functions = functions;
    }

    // get methods for private variables
    public Map<String, Variable> getVariables() {
        return variables;
    }
    public List<Function> getFunctions() {
        return functions;
    }

    public Variable getVariable(String name) {
        return variables.get(name);
    }
    public List<Function> getFunction(String name) {
        return Function.getFunction(functions, name);
    }
    public Function getFunction(String name, List<Variable> param) {
        return Function.getFunction(functions, name, param);
    }

    // put methods for private variables
    public void putVariable(String name, Variable value) {
        variables.put(name, value);
    }
    public void putFunction(Function function) {
        functions.add(function);
    }

    // returns Environment with new variables and new 
    public Environment copyEnvironment() {
        return new Environment(new HashMap<String, Variable>(variables), new ArrayList<Function>(functions));
    }
}

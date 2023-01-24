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

    // merges updates from child to parent environment
    public void updateVariable(Environment child) {
        for (String key : variables.keySet()) {
            if (child.containsVariable(key)) {
                variables.put(key, child.getVariable(key));
            }
            else {
                throw new Error("Missing variable");
            }
        }
    }

    // returns true if environment has a variable/function corresponding to name
    public boolean containsVariable(String name) {
        return variables.containsKey(name);
    }
    
    public boolean containsFunction(String name) {
        for (Function function : functions) {
            if (function.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        String out = "";
        for (String var : variables.keySet()) {
            out += var + " = " + variables.get(var).toString() + "\n"; 
        }
        for (Function func : functions) {
            out += func.toString() + "\n";
        }
        return out;
    }
}

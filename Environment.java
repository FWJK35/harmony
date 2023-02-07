/**
 * Environment holds a map of String names to Variable variables
 * and a list of Function objects. Basically contains all data
 * of the current runtime
 */

import java.util.*;
import javax.swing.JTextArea;

public class Environment {
    private Map<String, Variable> variables;
    private List<Function> functions;
    private JTextArea terminal;

    // constructors
    public Environment(JTextArea terminal) {
        this.variables = new HashMap<String, Variable>();
        this.functions = new ArrayList<Function>();
        this.terminal = terminal;
    }
    public Environment(Map<String, Variable> variables) {
        this.variables = variables;
        this.functions = new ArrayList<Function>();
    }
    public Environment(List<Function> functions) {
        this.variables = new HashMap<String, Variable>();
        this.functions = functions;
    }
    public Environment(Map<String, Variable> variables, List<Function> functions, JTextArea terminal) {
        this.variables = variables;
        this.functions = functions;
        this.terminal = terminal;
    }

    // accessors
    public Map<String, Variable> getVariables() {
        return variables;
    }
    public List<Function> getFunctions() {
        return functions;
    }
    public JTextArea getTerminal() {
        return terminal;
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

    // mutators
    public void putVariable(String name, Variable value) {
        variables.put(name, value);
    }
    public void putFunction(Function function) {
        functions.add(function);
    }

    // returns a separate, duplicate instance of Environment
    public Environment copyEnvironment() {
        return new Environment(new HashMap<String, Variable>(variables), new ArrayList<Function>(functions), terminal);
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

    public void print(String line) {
        terminal.append(line + "\n");
    }

    @Override
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

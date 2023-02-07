/**
 * Compiler class holds a private Environment and File object
 * so that each Environment is unique to a File; ie. variables
 * would not be globally shared across files. It can compile
 * (initalize variables and functions), and run .hrm code. 
 */

import java.io.*;
import java.util.*;
import javax.swing.*;

public class Compiler {
    private Environment env;
    private File file;
    private JTextArea terminal;
    
    // constructor
    public Compiler(File file, JTextArea terminal) {
        this.file = file;
        this.terminal = terminal;
        this.env = new Environment(terminal);
    }

    // accessors
    public Environment getEnvironment() {
        return env;
    }

    //adds global variables and all functions
    public void compile() {
        env = new Environment(terminal);
        for (String sysfunc : SystemFunction.systemFunctionHeaders) {
            StaticMethods.defineSystemFunction(env, sysfunc);
        }
        try (Scanner fileScanner = new Scanner(file);) {

            boolean defining = false;
            String code = "";
            Function currentFunc = new Function();
            int lineNum = 0;
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] tokens = line.split(" ");


                //least indentation
                if (StaticMethods.countIndent(line) == 0) {
                    //check for end of function definition
                    if (defining) {
                        defining = false;
                        currentFunc.setCode(code);
                        code = "";
                    }
                    //define function token
                    if (tokens.length >= TokenIndex.DEFINE_FUNCTION_TOKEN && 
                            tokens[TokenIndex.DEFINE_FUNCTION_TOKEN].equals(Keywords.DEFINE_FUNCTION_KEYWORD)) {
                        //at least has min number of tokens in definition
                        if (tokens.length - TokenIndex.MIN_DEFINE_LEN >= 0 && (tokens.length - TokenIndex.MIN_DEFINE_LEN) % 2 == 0) {
                            currentFunc = StaticMethods.defineFunction(env, line);
                            currentFunc.setStartLine(lineNum + 1);
                            defining = true;
                        }
                    }
                    //define variable token
                    else if (tokens.length >= TokenIndex.DEFINE_VARIABLE_TOKEN + 1 && 
                            tokens[TokenIndex.DEFINE_VARIABLE_TOKEN].equals(Keywords.DEFINE_VARIABLE_KEYWORD)) {
                        StaticMethods.defineVariable(env, line);
                    }
                    
                }
                else {
                    if (defining) {
                        code += line + "\n";
                    }
                    else if (StaticMethods.countIndent(line) > 0) {
                        throw new Error("Incorrect indentation for line: " + line);
                    }
                }
                lineNum++;
            }
            //get to end, finish defining
            if (defining) {
                currentFunc.setCode(code);
            }
            fileScanner.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Variable runCode() {
        Function mainFunction = env.getFunction("main", new ArrayList<Variable>());
        if (mainFunction == null) {
            throw new Error("Main function not found");
        }
        return mainFunction.run(new ArrayList<Variable>());
    }
}
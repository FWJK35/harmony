import java.io.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

/**
 * Compiler
 */

public class Compiler {
    private Environment env;
    private File file;
    // public static List<Function> functions = env.getFunctions();
    
    // constructor
    public Compiler(File file) {
        this.file = file;
        this.env = new Environment();
    }

    // accessors
    public Environment getEnvironment() {
        return env;
    }

    //adds global variables and all functions
    public void compile() {
        env = new Environment();
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

    public static void writeFile(File file) {
        new Notepad(file.getName());
    }

    public Variable runCode() {
        Function mainFunction = env.getFunction("main", new ArrayList<Variable>());
        if (mainFunction == null) {
            throw new Error("Main function not found");
        }
        return mainFunction.run(new ArrayList<Variable>());
    }
}
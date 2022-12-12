import java.io.*;
import java.util.*;
import java.util.List;
import java.awt.*;
import javax.swing.*;

/**
 * Compiler
 */
public class Compiler {
    public static Map<String, Variable> variables = new HashMap<String, Variable>();
    public static List<Function> functions = new ArrayList<Function>();
    public static void main(String[] args) {
        test();
    }


    public static Variable runCode(Scanner file) {
        return runCode(file, variables);
    }
    public static Variable runCode(Scanner file, Map<String, Variable> tempVariables) {
        // initialize variables and stuff here
        Map<String, Variable> vari = new HashMap<String, Variable>();
        vari.putAll(variables);
        vari.putAll(tempVariables);

        List<String> lines = new ArrayList<String>();
        while (file.hasNextLine()) {
            lines.add(file.nextLine());
        }
        int currentLine = 0;
        while (currentLine < lines.size()) {
            String thisLine = lines.get(currentLine);
            
            // get all tokens on the current line
            Scanner lineScanner = new Scanner(thisLine);
            List<String> tokens = new ArrayList<String>();
            while (lineScanner.hasNext()) {
                tokens.add(lineScanner.next());
            }
            lineScanner.close();

            // check if line is PRINT statment
            if (tokens.size() > TokenIndex.PRINT_TOKEN) {
                if (tokens.get(TokenIndex.PRINT_TOKEN).equals(Keywords.PRINT_KEYWORD)) {
                    // print the current line command
                    StaticMethods.print(thisLine, vari);
                }
            }
            
            // check if line is DEFINE_VARIABLE statement
            else if (tokens.size() > TokenIndex.DEFINE_VARIABLE_TOKEN) {
                if (tokens.get(TokenIndex.DEFINE_VARIABLE_TOKEN).equals(Keywords.DEFINE_VARIABLE_KEYWORD)) {
                    // put new variable in map
                    //TODO get both literal and expressions
                    variables.put(tokens.get(TokenIndex.VARIABLE_NAME_TOKEN), null); 
                    vari.put(tokens.get(TokenIndex.VARIABLE_NAME_TOKEN), null); 
                }
            }

            //check if line is IF_KEYWORD statement
            else if (tokens.size() > TokenIndex.IF_STATEMENT_TOKEN) {
                if (tokens.get(TokenIndex.IF_STATEMENT_TOKEN).equals(Keywords.IF_KEYWORD) && tokens.get(tokens.size() - 1).equals(Keywords.COLON_KEYWORD)) {
                    String codeToRun = "";
                    int currentIndentation = 0;
                    while (lines.get(currentLine).charAt(currentIndentation) == ' ') {
                        currentIndentation += 1;
                    }
                    for(int i = 1; currentIndentation < StaticMethods.countIndent(lines.get(currentLine + 1)); i++) {
                        currentLine++;
                        codeToRun += lines.get(currentLine) + "\n";
                    }
                    String statement = thisLine.substring(thisLine.indexOf(tokens.get(TokenIndex.IF_STATEMENT_TOKEN + 1), thisLine.length() - Keywords.COLON_KEYWORD.length()));
                    if (StaticMethods.interpretExpression(statement, vari).toBoolean()) {
                        runCode(new Scanner(codeToRun));
                    }
                    //ends code on the next line
                }
            }

            else if (tokens.size() > TokenIndex.WHILE_STATEMENT_TOKEN) {
                if (tokens.get(TokenIndex.WHILE_STATEMENT_TOKEN).equals(Keywords.WHILE_KEYWORD) && tokens.get(tokens.size() - 1).equals(Keywords.COLON_KEYWORD)) {
                    String condition = lines.get(currentLine);
                    int currentIndentation = StaticMethods.countIndent(condition);
                    condition = condition.substring(condition.indexOf(Keywords.WHILE_KEYWORD) + Keywords.WHILE_KEYWORD.length());
                    String codeToRun = "";
                    for (int i = 1; currentIndentation < StaticMethods.countIndent(lines.get(currentLine + 1)); i++) {
                        currentLine++;
                        codeToRun += lines.get(currentLine) + "\n";
                    }

                    while (StaticMethods.interpretExpression(condition, vari).toBoolean()) {
                        runCode(new Scanner(codeToRun));
                    }
                    
                }
            }

            else if (tokens.size() > TokenIndex.FOR_STATEMENT_TOKEN) {
                if (tokens.get(TokenIndex.FOR_STATEMENT_TOKEN).equals(Keywords.FOR_KEYWORD) && tokens.get(tokens.size() - 1).equals(Keywords.COLON_KEYWORD)) {
                    // takes an List of Strings for each line of code and index of line where for loop begins
                    Iterator<String> itr = lines.listIterator(currentLine);
                    String forLine = itr.next();
                    currentLine++;
                    int indent = StaticMethods.countIndent(forLine);
                    boolean isLoop = true;
                    
                    // checks that there is a next line with indentation greater than forLoop call
                    while (itr.hasNext() && isLoop) {
                        String line = itr.next();
                        currentLine++;
                        if (indent < StaticMethods.countIndent(line)) {
                            runCode(new Scanner(line));
                        }
                        else {
                            isLoop = false;
                        }
                    }
                }
            }

            else {
                currentLine++;
            }
        }
        return null;
    }

    public static void getFile() {
        // prompt file name using JOptionPane 
        Scanner input = new Scanner(System.in);
        try {
            String file = JOptionPane.showInputDialog("Input File Name: ");
            input = new Scanner(new File(file));
        } catch (FileNotFoundException e) {
            e.getStackTrace();
            System.out.println("Error: Invalid File Type");
        } catch (Exception e) {
            e.getStackTrace();
        }
        
        runCode(input);
    }
    
    
    // testing things here so it doesn't clog up the main class
    public static void test() {
        Variable intTest = new Variable(4);
        Variable doubleTest = new Variable(6.9);
        Variable boolTest = new Variable(true);
        Variable stringTest = new Variable("0123456789abcdefg");
        
        int a = 3;
        int b = 4;
        System.out.println(a + b);
        
    }
}
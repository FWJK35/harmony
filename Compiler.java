import java.io.*;
import java.util.*;
import java.util.List;
import java.awt.*;
import javax.swing.*;

/**
 * Compiler
 */
public class Compiler {

    public static void main(String[] args) {
        test();
        
        

    }

    public static void runCode(Scanner file) {
        // initialize variables and stuff here
        Map<String, Variable> variables = new HashMap<String, Variable>();


        while (file.hasNextLine()) {
            String thisLine = file.nextLine();
            
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
                    StaticMethods.print(thisLine, variables);
                }
            }

            // check if line is DEFINE_VARIABLE statement
            else if (tokens.size() > TokenIndex.DEFINE_VARIABLE_TOKEN) {
                if (tokens.get(TokenIndex.DEFINE_VARIABLE_TOKEN).equals(Keywords.DEFINE_VARIABLE_KEYWORD)) {
                    // put new variable in map
                    //TODO get both literal and expressions
                    variables.put(tokens.get(TokenIndex.VARIABLE_NAME_TOKEN), null); 
                }
            }

        }
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
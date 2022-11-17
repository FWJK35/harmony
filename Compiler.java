import java.io.*;
import java.security.Key;
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
        
        // prompt file name using JOptionPane 
        Scanner input = new Scanner(System.in);
        try {
            String file = JOptionPane.showInputDialog("Input File Name: ");
            input = new Scanner(new File(file));
            runCode(input);
            input.close();
        } catch (FileNotFoundException e) {
            e.getStackTrace();
            System.out.println("Error: Invalid File Type");
        } catch (Exception e) {
            e.getStackTrace();
        }
        
        runCode(input);
    }

    public static void runCode(Scanner file) {
        // initialize variables and stuff here
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
            if (tokens.size() >= TokenIndex.PRINT_TOKEN + 1) {
                if (tokens.get(TokenIndex.PRINT_TOKEN).equals(Keywords.PRINT_KEYWORD)) {
                    // interpret everything else after
                    // get beginning of string literal and trim line
                    int beginIndex = thisLine.indexOf(Keywords.STRING_LITERAL_KEYWORD);
                    int endIndex = thisLine.indexOf(Keywords.STRING_LITERAL_KEYWORD, beginIndex + 1);
                    String remaining = thisLine.substring(endIndex + Keywords.STRING_LITERAL_KEYWORD.length());
                    if (remaining.length() > 0) {
                        throw new Error("Invalid Arguments to command: " + Keywords.PRINT_KEYWORD);
                    }
                    else {
                        System.out.println(thisLine.substring(beginIndex, endIndex));
                    }
                }
            }

        }
    }
    
    
    // testing things here so it doesn't clog up the main class
    public static void test() {
        Variable intTest = new Variable(4);
        Variable doubleTest = new Variable(6.9);
        Variable boolTest = new Variable(true);
        Variable stringTest = new Variable("0123456789abcdefg");
        
        ArrayList<Variable> arrTest = new ArrayList<Variable>();
        arrTest.add(intTest);
        arrTest.add(doubleTest);
        arrTest.add(boolTest);
        arrTest.add(stringTest);

        // System.out.println(intTest);
        // System.out.println(doubleTest);
        // System.out.println(boolTest);
        // System.out.println(stringTest);
        // System.out.println(arrTest);
    
        System.out.println("SLICE TEST: " + stringTest.slice(6, 0, -1));
        System.out.println(arrTest);
    }
}
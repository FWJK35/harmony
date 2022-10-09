import java.io.*;
import java.util.*;
/**
 * Compiler
 */
public class Compiler {

    public static void main(String[] args) {
        test();

        try {
            Scanner file = new Scanner(new File("code.hrm"));
            file.close();
        } catch (Exception e) {}
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
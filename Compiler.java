import java.io.*;
import java.util.*;
/**
 * Compiler
 */
public class Compiler {

    public static void main(String[] args) {
        
        // Variable intTest = new Variable(4);
        // Variable doubleTest = new Variable(6.9);
        // Variable boolTest = new Variable(true);

        // ArrayList<Variable> arrTest = new ArrayList<Variable>();
        // arrTest.add(intTest);
        // arrTest.add(doubleTest);
        // arrTest.add(boolTest);
        // arrTest.add(stringTest);

        // System.out.println(intTest);
        // System.out.println(doubleTest);
        // System.out.println(boolTest);
        // System.out.println(stringTest);
        // System.out.println(arrTest);

        Variable stringTest = new Variable("lmfaoxdbozo hehe hehaw @$$ 8008135");
        System.out.println("SLICE TEST: " + stringTest.slice(6, 0, -1));

        try {
            Scanner file = new Scanner(new File("code.hrm"));
            file.close();
        } catch (Exception e) {}
    }

    // count function, generally to check number of nested arrays
    // private static int count(String str, String regex) {
    //     return str.split(regex).length;
    // }
}
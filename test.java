import java.io.File;

// import java.io.*;

public class test {
    public static void main(String[] args) {
        tester();
        
        Compiler comp = new Compiler(new File("tests/unitconverter.hrm"));
        comp.compile();
        System.out.println(comp.getEnvironment());
        System.out.println("--------------------------------------------");
        comp.runCode();
    }

    public static void tester() {
        System.out.println(Double.parseDouble("1"));
    }
}

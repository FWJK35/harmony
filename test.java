import java.io.File;

// import java.io.*;

public class test {
    public static void main(String[] args) {
        Compiler comp = new Compiler(new File("tests/unitconverter.hrm"));
        //Compiler comp = new Compiler(new File("test.hrm"));
        //Compiler comp = new Compiler(new File("test.hrm"));
        comp.compile();
        System.out.println(comp.getEnvironment());
        System.out.println("--------------------------------------------");
        comp.runCode();
    }
}

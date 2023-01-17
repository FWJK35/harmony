import java.io.File;

// import java.io.*;

public class test {
    public static void main(String[] args) {
        Compiler comp = new Compiler(new File("test.hrm"));
        comp.compile();
        System.out.println(comp.getEnvironment());
        //env.putVariable("name", new Variable("claving"));
        //System.out.println(StaticMethods.interpretExpression("#test: # (##name# # #R#)", env));
        //System.out.println(StaticMethods.separate("mult(10 ? 4) ? calvin"));
        
        // try {
        //     PrintStream test = new PrintStream(new File("lmafoa.xd"));
        //     test.println("test1");
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }

    }
}

// import java.io.*;

public class test {
    public static void main(String[] args) {
        Environment env = new Environment();
        env.putVariable("name", new Variable("claving"));
        System.out.println(StaticMethods.interpretExpression("#test: # (##name# # #R#)", env));

        
        // try {
        //     PrintStream test = new PrintStream(new File("lmafoa.xd"));
        //     test.println("test1");
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }

    }
}

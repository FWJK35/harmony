
public class test {
    public static void main(String[] args) {
        Environment env = new Environment();
        env.putVariable("name", new Variable("claving"));
        System.out.println('a'+'a');
        System.out.println(StaticMethods.interpretExpression("#test: # (##name# # #R#)", env));

        
        
    }
}

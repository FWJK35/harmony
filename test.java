import java.util.*;

public class test {
    public static void main(String[] args) {
        Map<String, Variable> vars = new HashMap<String, Variable>();
        vars.put("name", new Variable("claving"));
        System.out.println(StaticMethods.interpretExpression("#test: # (# #name# # #R#)", null));
    }
}

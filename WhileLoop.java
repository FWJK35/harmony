public class WhileLoop {
    private Environment env;
    private String condition;
    private Function code;

    public WhileLoop(Environment env, String condition, Function code) {
        this.env = env;
        this.condition = condition;
        this.code = code;
    }

    public void runCode() {
        while (StaticMethods.interpretExpression(condition, env).toBoolean()) {
            code.runFunction(null);
            
        }
    }
}

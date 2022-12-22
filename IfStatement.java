public class IfStatement {
    private Environment env;
    private String ifCondition;
    private Function ifCode;
    private Function elseCode;

    // if-else statement constructor
    public IfStatement(Environment env, String ifCondition, Function ifCode, Function elseCode) {
        this.env = env;
        this.ifCondition = ifCondition;
        this.ifCode = ifCode;
        this.elseCode = elseCode;
    }
    // if statement constructors
    public IfStatement(Environment env, String condition, Function ifCode) {
        this(env, condition, ifCode, new Function("", null, null, null, null));
    }

    public void runCode() {
        if (StaticMethods.interpretExpression(ifCondition, env).toBoolean()) {
            ifCode.runFunction(null);
        }
        else {
            elseCode.runFunction(null);
        }
    }
}

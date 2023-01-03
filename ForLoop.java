public class ForLoop {
    private Environment env;
    private String[] condition;
    private Function code;

    public ForLoop(Environment env, String condition, Function code) {
        this.env = env;
        this.condition = condition.split(";");
        this.code = code;
    }

    // TODO: write this
    public void runCode() {

    } 
}

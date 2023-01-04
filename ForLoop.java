public class ForLoop {
    private Environment env;
    private String[] condition;
    private Function code;


    //["i aka 0", "i < 5", "ily i"]
    public ForLoop(Environment env, String condition, Function code) {
        this.env = env;
        this.condition = condition.split(Keywords.SEPARATOR_KEYWORD);
        this.code = code;
    }

    // TODO: write this
    public void runCode() {
        
    } 
}

'
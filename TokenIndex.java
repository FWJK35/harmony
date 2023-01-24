/*
 * TokenIndex file contains indexes of tokens on a line
 * For example, if the first token is PRINT_KEYWORD, all
 * proceeding tokens are printed out. 
 */

public class TokenIndex {
    public static final int PRINT_TOKEN = 0;

    public static final int IF_STATEMENT_TOKEN = 0;
    public static final int ELSE_STATEMENT_TOKEN = 0;
    public static final int ELIF_STATEMENT_TOKEN = 1;
    public static final int WHILE_STATEMENT_TOKEN = 0;
    public static final int FOR_STATEMENT_TOKEN = 0;
    public static final int RETURN_TOKEN = 0;

    public static final int DEFINE_VARIABLE_TOKEN = 1;
    public static final int VARIABLE_NAME_TOKEN = 0;
    public static final int MODIFY_VARIABLE_TOKEN = 0;
    
    public static final int DEFINE_FUNCTION_TOKEN = 0;
    public static final int DEFINE_FUNCTION_NAME_TOKEN = 1;
    public static final int MIN_DEFINE_LEN = 3;
}

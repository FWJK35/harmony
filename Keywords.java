public class Keywords {
    public static final String FOR_KEYWORD = "lmfao";
    public static final String IF_KEYWORD = "lmao";
    public static final String ELSE_KEYWORD = "afk";
    public static final String WHILE_KEYWORD = "cope";
    public static final String DEFINE_VARIABLE_KEYWORD = "aka";
    public static final String DEFINE_FUNCTION_KEYWORD = "wdym";
    public static final String DECREMENT_KEYWORD = "kys";
    public static final String INCREMENT_KEYWORD = "ily";
    public static final String PRINT_KEYWORD = "xd";
    public static final String INPUT_KEYWORD = "hmu";
    public static final String RETURN_KEYWORD = "ttyl";
    public static final String EQUALS_KEYWORD = "is";
    public static final char STRING_LITERAL_KEYWORD = '#';
    public static final char ESCAPE_CHARACTER_KEYWORD = '\\';
    public static final char OPEN_PAREN_KEYWORD = '(';
    public static final char CLOSE_PAREN_KEYWORD = ')';
    public static final char OPEN_ARRAY_KEYWORD = '[';
    public static final char CLOSE_ARRAY_KEYWORD = ']';
    public static final String COLON_KEYWORD = ":)";
    public static final char SEPARATOR_KEYWORD = '?';
    public static final String OPERATOR_CHARACTERS = "+-*/%^";

    //TYPES
    public static final String NULL_TYPE = "non";
    public static final String INTEGER_TYPE = "int";
    public static final String DOUBLE_TYPE = "dbl";
    public static final String STRING_TYPE = "str";
    public static final String ARRAY_TYPE = "arr";

    //ILLEGAL IDENTIFIERS
    public static final String[] ILLEGAL_IDENTIFIERS = {
        FOR_KEYWORD, IF_KEYWORD, ELSE_KEYWORD, WHILE_KEYWORD, DEFINE_VARIABLE_KEYWORD, DEFINE_FUNCTION_KEYWORD, 
        DECREMENT_KEYWORD, INCREMENT_KEYWORD, PRINT_KEYWORD, INPUT_KEYWORD, RETURN_KEYWORD, EQUALS_KEYWORD, 
        STRING_LITERAL_KEYWORD, ESCAPE_CHARACTER_KEYWORD, OPEN_PAREN_KEYWORD, CLOSE_PAREN_KEYWORD, OPEN_ARRAY_KEYWORD,
        CLOSE_ARRAY_KEYWORD, COLON_KEYWORD, SEPARATOR_KEYWORD, OPERATOR_CHARACTERS, NULL_TYPE, INTEGER_TYPE,
        DOUBLE_TYPE, STRING_TYPE, ARRAY_TYPE
    };
    public static boolean isIllegalIdentifier(String s) {
        for (String id : ILLEGAL_IDENTIFIERS) {
            if (s.equals(id)) {
                return true;
            }
        }
        return false;
    }
}

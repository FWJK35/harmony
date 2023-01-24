/**
 * Keywords defines Harmony keywords and illegal identifiers
 */

public class Keywords {
    // if-else and conditional loops 
    public static final String 
        IF_KEYWORD = "lmao",
        ELSE_KEYWORD = "afk",
        FOR_KEYWORD = "lmfao",
        WHILE_KEYWORD = "cope";

    // boolean keywords
    public static final String 
        EQUALS_KEYWORD = "is",
        GREATER_KEYWORD = "more",
        LESSER_KEYWORD = "less",
        OR_KEYWORD = "or",
        AND_KEYWORD = "and",
        TRUE_KEYWORD = "good",
        FALSE_KEYWORD = "bad";

    public static final String DEFINE_VARIABLE_KEYWORD = "aka";
    public static final String DEFINE_FUNCTION_KEYWORD = "wdym";
    public static final String DECREMENT_KEYWORD = "kys";
    public static final String INCREMENT_KEYWORD = "ily";
    public static final String PRINT_KEYWORD = "xd";
    public static final String INPUT_KEYWORD = "hmu";
    public static final String RETURN_KEYWORD = "ttyl";
    public static final String COMMENT_KEYWORD = "//";
    
    public static final String[] BOOLEAN_KEYWORDS = {
        EQUALS_KEYWORD, GREATER_KEYWORD, LESSER_KEYWORD,
        OR_KEYWORD, AND_KEYWORD
    };

    public static final String[] EVAL_KEYWORDS = {
        TRUE_KEYWORD, FALSE_KEYWORD, INPUT_KEYWORD,
    };

    public static final char ARRAY_SEPARATOR_KEYWORD = ',';
    public static final char STRING_LITERAL_KEYWORD = '#';
    public static final char ESCAPE_CHARACTER_KEYWORD = '\\';
    public static final char OPEN_PAREN_KEYWORD = '(';
    public static final char CLOSE_PAREN_KEYWORD = ')';
    public static final char OPEN_ARRAY_KEYWORD = '[';
    public static final char CLOSE_ARRAY_KEYWORD = ']';
    public static final char SEPARATOR_KEYWORD = '?';
    public static final char LINE_JOINER_KEYWORD = '|';
    public static final String COLON_KEYWORD = ":)";
    public static final String OPERATOR_CHARACTERS = "+-*/%^";

    //TYPES
    public static String[] JAVA_TYPES = {"Integer", "Double", "String", "Boolean", "ArrayList"};
    public static String[] HARMONY_TYPES = {"int", "dbl", "str", "boo", "arr"};

    //ILLEGAL IDENTIFIERS
    public static final String[] ILLEGAL_IDENTIFIERS = {
        FOR_KEYWORD, IF_KEYWORD, ELSE_KEYWORD, WHILE_KEYWORD, DEFINE_VARIABLE_KEYWORD, DEFINE_FUNCTION_KEYWORD, 
        DECREMENT_KEYWORD, INCREMENT_KEYWORD, PRINT_KEYWORD, INPUT_KEYWORD, RETURN_KEYWORD, EQUALS_KEYWORD, 
        COLON_KEYWORD, OPERATOR_CHARACTERS, TRUE_KEYWORD, FALSE_KEYWORD
    };

    public static boolean isIllegalIdentifier(String s) {
        for (String id : ILLEGAL_IDENTIFIERS) {
            if (s.equals(id)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isIllegalEvalIdentifier(String s) {
        if (isIllegalIdentifier(s)) {
            for (String id : EVAL_KEYWORDS) {
                if (s.equals(id)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}

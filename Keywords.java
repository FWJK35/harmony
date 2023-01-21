import java.util.Map;

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
    public static Map<String, String> typeMap = Map.of(
        "null", "non",
        "int", "int",
        "double", "dbl",
        "String", "str",
        "ArrayList", "arr"
        );

    //ILLEGAL IDENTIFIERS
    public static final String[] ILLEGAL_IDENTIFIERS = {
        FOR_KEYWORD, IF_KEYWORD, ELSE_KEYWORD, WHILE_KEYWORD, DEFINE_VARIABLE_KEYWORD, DEFINE_FUNCTION_KEYWORD, 
        DECREMENT_KEYWORD, INCREMENT_KEYWORD, PRINT_KEYWORD, INPUT_KEYWORD, RETURN_KEYWORD, EQUALS_KEYWORD, 
        COLON_KEYWORD, OPERATOR_CHARACTERS, /*typeMap.get("null"), typeMap.get("int"), typeMap.get("double")
        , typeMap.get("String"), typeMap.get("ArrayList")*/
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

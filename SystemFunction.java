import java.util.List;

public class SystemFunction extends Function {
    //TODO these functions as well
    //TODO add to environment
    public static String[] systemFunctionHeaders = {
        "wdym len str toFind :)",
        "wdym len arr toFind :)",
        "wdym split str original str splitChar :)",
        "wdym toInt str data :)",
        "wdym toDouble str data :)",
        "wdym toString str data :)",
    };
    //xd len(#calvin#)
    //xd split(#03:05:06:11# ? #:#)
    public static Variable getSystemFunction() {
        return new Variable();
    }

    @Override
    public Variable run(List<Variable> args, Variable hasReturned, Variable returnValue) {
        if (this.getName().equals("len")) {
            return new Variable(args.get(0).len());
        } 
        else if (this.getName().equals("split")) {
            return new Variable(((String) args.get(0).getData()).split((String) args.get(1).getData()));
        }
        else if (this.getName().equals("toInt")) {
            Object data = args.get(0).getData();
            if (data instanceof String) {
                data = Double.parseDouble((String) data);
            }
            if (data instanceof Double) {
                return new Variable((int) (double) data);
            }
            return new Variable((int) data);
        }
        else if (this.getName().equals("toDouble")) {
            return new Variable(args.get(0).toDouble());
        }
        else if (this.getName().equals("toString")) {
            return new Variable(args.get(0).toString());
        }
        return new Variable();
    }
}

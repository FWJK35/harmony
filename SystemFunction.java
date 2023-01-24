/**
 * SystemFunction is a child class of Function
 * They are globally implemented Functions, including
 * finding length, casting, and random double from 0 to 1 
 */

import java.util.List;

public class SystemFunction extends Function {
    public static String[] systemFunctionHeaders = {
        "wdym len str toFind :)",
        "wdym len arr toFind :)",
        "wdym split str original str splitChar :)",
        "wdym toInt str data :)",
        "wdym toInt dbl data :)",
        "wdym toInt boo data :)",
        "wdym toDouble str data :)",
        "wdym toDouble int data :)",
        "wdym toString int data :)",
        "wdym toString dbl data :)",
        "wdym toString boo data :)",
        "wdym random :)"
    };
    public SystemFunction(Environment env, List<String> paramNames, List<String> paramTypes, String funcName) {
        super("", env, paramNames, paramTypes, funcName);
	}

	//xd len(#calvin#)
    //xd split(#03:05:06:11# ? #:#)
    public static Variable getSystemFuction() {
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
            return new Variable(args.get(0).toInteger());
        }
        else if (this.getName().equals("toDouble")) {
            return new Variable(args.get(0).toDouble());
        }
        else if (this.getName().equals("toString")) {
            return new Variable(args.get(0).toString());
        }
        else if (this.getName().equals("random")) {
            return new Variable(Math.random());
        }
        return new Variable();
    }
}

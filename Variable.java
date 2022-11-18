import java.util.ArrayList;

 @SuppressWarnings("unchecked")
public class Variable {
    private Object data;
    
    // constructors
    public Variable(Object data) {
        this.data = data;
    }

    public Variable copy() {
        return new Variable(data);
    }


    //TODO add the following functionality
    /*
     * int + int = int
     * double + double = double
     * string + string = string
     * char + char = string
     * array + array = arrays appended to each other
     * 
     * THE FOLLOWING OPERATIONS SHOULD WORK IN BOTH DIRECTIONS
     * 
     * double + int = double
     * int + string = string
     * double + string = string
     * char + int = char
     * string + char = string
     * double + bool = double
     * int + bool = int
     * string + bool = string
     * 
     * array + any = array with other element appended
     * 
     */
    public Variable addTo(Variable a) {
        Variable out = new Variable(0);
        if (this.data instanceof Integer) {
            if (a.getType().equals("Integer")) {
                data = (Integer)data + Double.parseDouble(a.toString());
            }
        }
        return out;
    }

    public Variable slice(int start, int end, int step) {
        ArrayList<Variable> data = new ArrayList<Variable>();
        
        // converts Object String data into an ArrayList of Variable type char
        if (this.data instanceof String) {
            for (char c : this.data.toString().toCharArray()) {
                data.add(new Variable(c));
            }
        }

        else if (this.data instanceof ArrayList) {
            for (Variable v : (ArrayList<Variable>) this.data) {
                data.add(v.copy());
            }
        }
        else {
            throw new IllegalStateException("Type must be Array or String");
        }

        ArrayList<Variable> newData = new ArrayList<Variable>();
        /*
         * Example:
         *   0  1  2  3  4  5  
         * -6 -5 -4 -3 -2 -1 
         * start < 6. start >= -6
         * end <= 6. end >= -7
        */
        
        // throw errors
        
        // checks if start and end points are out of range
        if (start >= data.size() || start < -data.size() || 
            end > data.size() || end < -data.size() - 1) {
            throw new IndexOutOfBoundsException("Index must be within size of string length");
        }
        
        // checks if step is 0
        if (step == 0) {
            throw new IllegalArgumentException("Step cannot be 0!");
        }

        // reassigns negative indexes
        if(start < 0) {
            start = data.size() + start;
        }
        if(end < 0) {
            end = data.size() + end - 1;
        }
        
        // if step is positive loop forward
        if (step > 0) {
            for(int i = start; i < end; i += step) {
                newData.add(data.get(i));
            }
        }

        // if step is negative loop backward
        else {
            for(int i = start; i > end; i += step) {
                newData.add(data.get(i));
            }
        }

        Variable result = new Variable(newData);

        // if original data was String, convert back to String
        if (this.data instanceof String) {
            String stringResult = "";
            for (Variable v : newData) {
                stringResult += v.toString();
            }
            result = new Variable(stringResult);
        }

        return new Variable(result);
    }

    public Variable slice(int start, int end) {
        return slice(start, end, 1);
    }

    // count function, generally to check number of nested arrays
    // private static int count(Object data, String regex) {
    //     String str = "";
    //     if(data instanceof ArrayList) {
    //         for(Object datum : (ArrayList<Object>) data) {
    //             str += datum;
    //         }
    //     }
    //     else {
    //         str += data;
    //     }
    //     return str.split(regex).length;
    // }
   
    public int len() {
        if (this.data instanceof String) {
            return ((String) data).length();
        }
        if (this.data instanceof ArrayList) {
            return ((ArrayList<Variable>) data).size();
        }
        else {
            throw new IllegalStateException("Type must be Array or String");
        }
    }

    @Override
    public String toString() {
        if (this.data instanceof ArrayList) {
            String result = "[";
            for (Variable v : (ArrayList<Variable>) data) {
                result += v.toString() + ", ";
            }
            return (new Variable(result)).slice(0, -2).toString() + "]";
        }
        return data.toString();
    }

    // accessor methods
    public String getType() {
        return data.getClass().getSimpleName();
    }
}
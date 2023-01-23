/*
 * Variable class is a universal variable
 * Can by any of following types
 * Int - Integer
 * Dbl - Double
 * Chr - Character
 * Str - String
 * Boo - Boolean
 * Arr - Array
 */

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class Variable {
    private Object data;
    
    // constructors
    public Variable(Object data) {
        this.data = data;
    }
    public Variable() {
        
    }

    public Variable copy() {
        return new Variable(data);
    }

    public void setData(Object data) {
        this.data = data;
    }

    // accessor methods
    public Object getData() {
        return data;
    }

    public String getType() {
        return data.getClass().getSimpleName();
    }

    //TODO add the following functionality
    /*
     * int + int = int
     * double + double = double
     * string + string = string
     * char + char = int
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
     //b + a
    public static Variable combine(Variable a, Variable b) {
        if (a.getData() == null) {
            return b;
        }
        if (b.getData() == null) {
            return a;
        }
        if (a.getData() instanceof ArrayList) {
            if (b.getData() instanceof ArrayList) {
                Variable out = a.copy();
                ((ArrayList<Variable>) out.getData()).addAll((ArrayList<Variable>) b.getData());
            }
            else {
                Variable out = a.copy();
                out.add(b);
                return out;
            }
        }
        if (b.getData() instanceof ArrayList) {
            Variable out = b.copy();
            out.add(0, a);
            return out;
        }
        if (a.getData() instanceof Integer) {
            if (b.getData() instanceof Integer) {
                return new Variable((int) a.getData() + (int) b.getData());
            }
            if (b.getData() instanceof Double) {
                return new Variable((int) a.getData() + (double) b.getData());
            }
            if (b.getData() instanceof Character) {
                return new Variable((int) a.getData() + (char) b.getData());
            }
            if (b.getData() instanceof String) {
                return new Variable((int) a.getData() + (String) b.getData());
            }
        }
        if (a.getData() instanceof Double) {
            if (b.getData() instanceof Integer) {
                return new Variable((double) a.getData() + (int) b.getData());
            }
            if (b.getData() instanceof Double) {
                return new Variable((double) a.getData() + (double) b.getData());
            }
            if (b.getData() instanceof Character) {
                return new Variable((double) a.getData() + (char) b.getData());
            }
            if (b.getData() instanceof String) {
                return new Variable((double) a.getData() + (String) b.getData());
            }
        }
        if (a.getData() instanceof Character) {
            if (b.getData() instanceof Integer) {
                return new Variable((char) a.getData() + (int) b.getData());
            }
            if (b.getData() instanceof Double) {
                return new Variable((char) a.getData() + (double) b.getData());
            }
            if (b.getData() instanceof Character) {
                return new Variable((char) a.getData() + (char) b.getData());
            }
            if (b.getData() instanceof String) {
                return new Variable((char) a.getData() + (String) b.getData());
            }
        }
        if (a.getData() instanceof String) {
            if (b.getData() instanceof Integer) {
                return new Variable((String) a.getData() + (int) b.getData());
            }
            if (b.getData() instanceof Double) {
                return new Variable((String) a.getData() + (double) b.getData());
            }
            if (b.getData() instanceof Character) {
                return new Variable((String) a.getData() + (char) b.getData());
            }
            if (b.getData() instanceof String) {
                return new Variable((String) a.getData() + (String) b.getData());
            }
        }
        throw new Error("Failed to combine variables: " + a.toString() + " and " + b.toString());
    }

    public Variable slice(int start, int end, int step) {
        ArrayList<Variable> data = new ArrayList<Variable>();
        
        // converts Object String data into an ArrayList of Variable type char
        if (this.data instanceof String) {
            for (char c : data.toString().toCharArray()) {
                data.add(new Variable(c));
            }
        }
        else if (this.data instanceof ArrayList) {
            for (Variable v : (ArrayList<Variable>) data) {
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

    //count function, generally to check number of nested arrays
    /*private static int count(Object data, String regex) {
        String str = "";
        if(data instanceof ArrayList) {
            for(Object datum : (ArrayList<Object>) data) {
                str += datum;
            }
        }
        else {
            str += data;
        }
        return str.split(regex).length;
    }*/

    // add to ArrayList
    public void add(int index, Variable value) {
        if (data instanceof ArrayList) {
            ((ArrayList<Variable>) data).add(index,value);
        }
        else {
            throw new IllegalStateException();
        }
    }

    //if is ArrayList
    public void add(Variable value) {
        add(len(), value);
    }

    // remove from ArrayList
    public Variable remove(int index) {
        if (data instanceof ArrayList<?>) {
            return ((ArrayList<Variable>) data).remove(index);
        }
        else {
            throw new IllegalStateException("Type must be Array");
        }
    }
    
    // gets length of data 
    public int len() {
        if (data instanceof String) {
            return ((String) data).length();
        }
        if (data instanceof ArrayList) {
            return ((ArrayList<Variable>) data).size();
        }
        else {
            throw new IllegalStateException("Type must be Array or String");
        }
    }

    @Override
    public String toString() {
        if (data instanceof ArrayList) {
            String result = "[";
            for (Variable v : (ArrayList<Variable>) data) {
                result += v.toString() + ", ";
            }
            return (new Variable(result)).slice(0, -2).toString() + "]";
        }
        if (data == null) {
            return "null";
        }
        return data.toString();
    }

    public boolean toBoolean() {
        if (data == null) {
            return false;
        }
        if (data instanceof Integer) {
            if ((int) data == 0) {
                return false;
            }
            return true;
        }
        if (data instanceof Double) {
            if (((int) data) == 0) {
                return false;
            }
            return true;
        }
        if (data instanceof String) {
            if (((String) data).equals("")) {
                return false;
            } 
            return true;
        }
        if (data instanceof Character) {
            if (((char) data) == ' ') {
                return false;
            }
            return true;
        }
        return (boolean) data;
    }

    public int toInteger() {
        Object data = this.data;
        if (data instanceof String) {
            return (int) Double.parseDouble((String) data);
        }
        if (data instanceof Character) {
            return (int) (char) data;
        }
        if (data instanceof Double) {
            return (int) (double) data;
        }
        return (int) data;
    }

    public double toDouble() {
        Object data = this.data;
        if (data instanceof String) {
            return Double.parseDouble((String) data);
        }
        if (data instanceof Integer) {
            return (double) (int) data;
        }
        return (double) data;
    }

    //method that sees if stuff equal
    public boolean equals(Variable v) {
        if (getType().equals(v.getType())) {
            if (data instanceof String) {
                if (((String) data).equals((String) v.getData())) {
                    return true;
                }
                return false;
            }
            if (data instanceof Integer) {
                if ((int) data == (int) v.getData()) {
                    return true;
                }
                return false;
            }
            if (data instanceof Double) {
                if ((double) data == (double) v.getData()) {
                    return true;
                }
                return false;
            }
            if (data instanceof Boolean) {
                if ((boolean) data == (boolean) v.getData()) {
                    return true;
                }
                return false;
            }
            if (data instanceof Character) {
                if (((Character) data).equals((Character) v.getData())) {
                    return true;
                }
                return false;
            }
            if (data instanceof List<?>) {
                // ((ArrayList<Variable>) data)
                if (((ArrayList<Variable>) data).size() == ((ArrayList<Variable>) v.getData()).size()) {
                    for (int i = 0; i < ((ArrayList<Variable>) data).size(); i++) {
                        if (!((ArrayList<Variable>) data).get(i).equals(((ArrayList<Variable>) v.getData()).get(i))) {
                            return false;
                        }
                    }
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }
}
import java.util.ArrayList;

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

    public Object getData() {
        return data;
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
    public Variable addTo(Variable a) {
        Variable out = new Variable(0);
        if (data instanceof Integer) {
            if (a.getData() instanceof Integer) {
                out.setData((int) data + (int) a.getData());
            }
            if (a.getData() instanceof Double) {
                data = (int) data + (double) a.getData();
            }
            if (a.getData() instanceof Character) {
                data = (int) data + (char) a.getData();
            }
            if (a.getData() instanceof String) {
                data = (int) data + (String) a.getData();
            }
        }
         if (data instanceof Double) {
            if (a.getData() instanceof Integer) {
                out.setData((int) data + (int) a.getData());
            }
            if (a.getData() instanceof Double) {
                data = (int) data + (double) a.getData();
            }
            if (a.getData() instanceof Character) {
                data = (int) data + (char) a.getData();
            }
            if (a.getData() instanceof String) {
                data = (int) data + (String) a.getData();
            }
        }
        if (data instanceof Character) {
            if (a.getData() instanceof Integer) {
                out.setData((int) data + (int) a.getData());
            }
            if (a.getData() instanceof Double) {
                data = (int) data + (double) a.getData();
            }
            if (a.getData() instanceof Character) {
                data = (int) data + (char) a.getData();
            }
            if (a.getData() instanceof String) {
                data = (int) data + (String) a.getData();
            }
        }
        if (data instanceof String) {
            if (a.getData() instanceof Integer) {
                out.setData((int) data + (int) a.getData());
            }
            if (a.getData() instanceof Double) {
                data = (int) data + (double) a.getData();
            }
            if (a.getData() instanceof Character) {
                data = (int) data + (char) a.getData();
            }
            if (a.getData() instanceof String) {
                data = (int) data + (String) a.getData();
            }
        }
        return out;
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
        return data.toString();
    }

    // accessor methods
    public String getType() {
        return data.getClass().getSimpleName();
    }

    public boolean toBoolean() {
        if (data instanceof Integer) {
            if ((int) data == 0) {
                return false;
            }
            else {
                return true;
            }
        }
        else if (data instanceof String) {
            if (((String) data).equals("")) {
                return false;
            } else {
                return true;
            }
        } 
        else if (data instanceof Char)
    }
}
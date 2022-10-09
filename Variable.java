public class Variable {
    private String stringVal;
    private String type;
    
    // constructors
    public Variable(Object i) {
        type = i.getClass().getSimpleName();
        System.out.println("type=" + type);
        stringVal = i + "";
    }

    public Variable(String data, String type) {
        this.stringVal = data;
        this.type = type;
    }

    public Variable copy() {
        return new Variable(stringVal, type);
    }

    //TODO finish this method
    public Variable slice(int start, int end, int step) {
        if (this.getType().equals("String")) {
            /*
               0  1  2  3  4  5  
            -6 -5 -4 -3 -2 -1 
            start < 6. start >= -6
            end <= 6. end >= -7
            */
            
            // checks if start and end points are out of range
            if (start >= stringVal.length() || start < -stringVal.length() || 
                end > stringVal.length() || end < -stringVal.length() - 1) {
                throw new IndexOutOfBoundsException("Index must be within size of string length");
            }
            if(start < 0) {
                start = stringVal.length() - start;
            }
            if(end < 0) {
                end = stringVal.length() - end - 1;
            }
            
            String newVar = "";//"[";
            if (step == 0) {
                throw new IllegalArgumentException("Step cannot be 0!");
            }
            if (step > 0) {
                for(int i = start; i < end; i += step) {
                    newVar += stringVal.charAt(i); //+ ", ";
                }
            }
            else {
                for(int i = start; i > end; i += step) {
                    newVar += stringVal.charAt(i); //+ ", ";
                }
            }
            //newVar += "]";
            return new Variable(newVar);
        }
        else if (this.getType().equals("ArrayList")) {
        }
        throw new IllegalStateException("Type must be Array or String");
    }


    public Variable slice(int start, int end) {
        return slice(start, end, 1);
    }

    public int len() {
        if (this.getType().equals("String")) {
            return stringVal.length();
        }
        if (this.getType().equals("ArrayList")) {
            //TODO count elements in array if it is on
            return 0;
        }
        else {
            throw new IllegalStateException("Type must be Array or String");
        }
    }

    public String toString() {
        return stringVal;
    }

    //accessor methods
    public String getType() {
        return type;
    }   
}
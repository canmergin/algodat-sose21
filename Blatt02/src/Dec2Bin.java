import java.util.Stack;

/**
 * A class for constructing a Decimal-to-Binary Number- Converter; * contains a main method for demonstration.
 */
public class Dec2Bin {

    public Stack<Integer> binStack;  // We make it public to modify it in our tests.
    private int N;

    /**
     * Constructor of an empty object. Use method {@code convert()} to convert a number.
     */
    public Dec2Bin() {
        binStack = new Stack<>();
    }

    /**
     * Returns the number that is converted as {@code int}.
     *
     * @return the converted number
     */
    public int getN() {
        return N;
    }

    /**
     * Converts the given number into binary format, with each digit being represented in a
     * stack of {@code int}.
     *
     * @param N the number that is to be converted.
     */
    public void convert(int N) {
        this.N = N;
        for (; N != 0; N /= 2) {
            binStack.push(N % 2);
        }
    }

    /**
     * Returns the digits that are stored in {@code binStack} as a string. To is the binary format of the
     * converted number.
     * For testing purpose, we require that the function works also, if the variable {@code binStack} is
     * modified externally.
     *
     * @return a string representation of the number in binary format.
     */
    @Override
    public String toString() {
        StringBuilder binString = new StringBuilder();
        for (int i = binStack.size() - 1; i >= 0; i--) binString.append(binStack.get(i));
        return binString.toString();
    }

    public static void main(String[] args) {
        Dec2Bin dec2bin = new Dec2Bin();
        dec2bin.convert(50);
        System.out.println("Die Zahl " + dec2bin.getN() + " in Binärdarstellung: " + dec2bin.toString());
        // Do it another time to demonstrate that toString does not erase the binStack.
        System.out.println("Die Zahl " + dec2bin.getN() + " in Binärdarstellung: " + dec2bin.toString());
    }
}


import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Matrix {
    public int[][] matrix;
    int rows;
    int cols;

    public Matrix(int n, int m) {
        matrix = new int[n][m];
        this.rows = n;
        this.cols = m;
    }

    @Override
    public String toString() {
        // return (Arrays.deepToString(matrix).replace("], ", "]\n").replace("[[",
        // "[").replace("]]", "]"));

        // String format = format();
        String result = "";
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result += String.format(format(j), matrix[i][j]);
            }
            result += "\n";
        }
        return result;
    }

    public void set(int n, int m, int value) {
        matrix[n][m] = value;
    }

    public void set(String input) {
        input = input.replaceAll(" ", "");
        String[] rows = input.split(";");
        for (int i = 0; i < this.rows; i++) {
            String row = rows[i];
            // System.out.println(row);
            String[] values = row.split(",");
            for (int j = 0; j < this.cols; j++) {
                // System.out.println(values[j]);
                matrix[i][j] = Integer.parseInt(values[j]);
            }
        }
    }

    public Matrix develop(){
        Matrix matrix = new Matrix(rows-1,cols-1);
        for (int i = 1; i < rows; i++) {
            for (int j = 1; j < cols; j++) {
                matrix.set(i-1, j-1, this.matrix[i][j]);
            }
        }
        return matrix;
    }

    public void print() {
        System.out.println(toString());
        // System.out.println();
    }

    public void swapRows(int to, int from) {
        to--;
        from--;
        int[] tmp = matrix[to];
        matrix[to] = matrix[from];
        matrix[from] = tmp;
    }

    public void swapCols(int to, int from) {
        to--;
        from--;
        int[] tmp = new int[this.rows];
        for (int i = 0; i < tmp.length; i++) {
            tmp[i] = matrix[i][to];
            // System.out.println(tmp[i]);
            matrix[i][to] = matrix[i][from];
            matrix[i][from] = tmp[i];
        }
    }

    public void addRows(int to, int from, int multiple) {
        to--;
        from--;
        for (int i = 0; i < this.rows; i++) {
            matrix[to][i] += matrix[from][i] * multiple;
        }
    }

    public void addRows(int to, int from) {
        addRows(to, from, 1);
    }

    public void addCols(int to, int from, int multiple) {
        to--;
        from--;

        for (int i = 0; i < this.rows; i++) {
            matrix[i][to] += matrix[i][from] * multiple;
        }
    }

    public void addCols(int to, int from) {
        addCols(to, from, 1);
    }

    public void multRow(int to, int multiple) {
        to--;
        for (int i = 0; i < matrix.length; i++) {
            matrix[to][i] *= multiple;
        }
    }

    public void multCol(int to, int multiple) {
        to--;
        for (int i = 0; i < matrix.length; i++) {
            matrix[i][to] *= multiple;
        }
    }

    public String format(int j) {
        int max = 0;
        for (int i = 0; i < cols; i++) {
            if ((matrix[i][j] + "").length() > max)
                max = (matrix[i][j] + "").length();
        }

        max++;
        return "%-" + max + "d";
    }

    public int width() {
        return 3;
    }

    public void eval(String in) {
        String[] tmp = lex(in);
        tmp = toPostfix(tmp);
        // System.out.println(Arrays.toString(tmp));
        calc(tmp);
    }

    public void calc(String[] expression) {
        if (expression[0].contains("r")) {
            if (expression.length == 1)
                multRow(split(expression[0])[0], split(expression[0])[1]);
            else {
                switch (expression[2]) {
                    case "+":
                        addRows(split(expression[0])[0], split(expression[1])[0], split(expression[1])[1]);
                        break;
                    case "-":
                        addRows(split(expression[0])[0], split(expression[1])[0], -split(expression[1])[1]);
                        break;
                    case "|":
                        swapRows(split(expression[0])[0], split(expression[1])[0]);
                }
            }
        } else {
            if (expression.length == 1)
                multCol(split(expression[0])[0], split(expression[0])[1]);
            else {
                switch (expression[2]) {
                    case "+":
                        addCols(split(expression[0])[0], split(expression[1])[0], split(expression[1])[1]);
                        break;
                    case "-":
                        addCols(split(expression[0])[0], split(expression[1])[0], -split(expression[1])[1]);
                        break;
                    case "|":
                        swapCols(split(expression[0])[0], split(expression[1])[0]);
                }
            }
        }
    }

    public static int[] split(String part) {
        part.replaceAll(" ", "");
        String[] tmp = part.split("k|r");
        int multiple = 0;
        int number = 0;
        // System.out.println(Arrays.toString(tmp));
        if (tmp[0].isEmpty()) {
            multiple = 1;
            number = Integer.parseInt(tmp[1]);
        } else {
            multiple = Integer.parseInt(tmp[0]);
            number = Integer.parseInt(tmp[1]);
        }

        int[] result = { number, multiple };
        return result;
    }

    public static String[] expressionToArray(String expression) {
        expression = expression.replaceAll(" ", "");
        String tmp = "";
        Queue<String> result = new LinkedList<>();

        for (char i : expression.toCharArray()) {
            if (isOperator(i + "")) {

                if (!tmp.isEmpty()) {
                    result.add(tmp);
                    tmp = "";
                }

                result.add(i + "");
            }

            else if (i == 'r' || i == 'k') {
                tmp += i;
            } else
                tmp += i;

        }
        if (!tmp.isEmpty()) {
            result.add(tmp);
            tmp = "";
        }

        return (String[]) result.toArray(new String[result.size()]);
    }

    public static String[] toPostfix(String[] expression) {
        Stack<String> stack = new Stack<String>();
        Queue<String> output = new LinkedList<String>();
        for (String current : expression) {
            // Operands always go straigt through
            if (isOperand(current))
                output.add(current);
            // If the stack is empty or the operator has higher priority
            else if (stack.empty() || (getOperandPririty(current) > getOperandPririty(stack.peek())))
                stack.push(current);
            else if (!isParenthasis(current)) {
                // If not, empty the stack until lower priority is found
                while (!stack.empty() && (getOperandPririty(current) <= getOperandPririty(stack.peek()))) {
                    String pop = stack.pop();
                    if (!isParenthasis(pop))
                        output.add(pop);
                }
                stack.add(current);
            } else if (current.equals("("))
                stack.push(current);
            else if (current.equals(")")) {
                while (!stack.empty() && !stack.peek().equals("("))
                    output.add(stack.pop());

                if (stack.peek().equals("("))
                    stack.pop();
            }

            // while (!stack.empty() && isParenthasis(stack.peek())) {
            // stack.pop();
            // }
        }

        // After loop is done, empty stack.
        while (!stack.empty()) {
            String pop = stack.pop();
            if (!isParenthasis(pop))
                output.add(pop);
        }

        String[] out = output.toArray(new String[output.size()]);
        return out;
    }

    public static String[] lex(String expr) {
        // Some formatting
        expr = expr.replace(",", ".");
        expr = expr.replace(" ", "");

        // Setup
        String tmpNumber = "";
        Queue<String> output = new LinkedList<String>();
        Boolean previousWasOperator = true;
        Boolean unaryNegative;

        // System.out.println("My code is running");

        // Loop through all characters
        for (char current : expr.toCharArray()) {

            // If the previous char was an operator then this one must be a unary minus.
            unaryNegative = (current == '-' && previousWasOperator);

            if (isOperand(current) || unaryNegative) {
                tmpNumber += current; // Since we do char by char it will take 3 iterations to register a 2 digit
                                      // number
                                      // We don't know when an operand ends until we detect an operator.
                previousWasOperator = false; // Used to calculate unarynegative.

            } else {
                // Check for unary minus in front of parenthasis
                if (current == '(' && tmpNumber.equals("-")) {
                    // a-(b-c) => a*-1*(b-c)
                    output.add("-1");
                    output.add("*");
                } else if (!tmpNumber.isEmpty()) { // Only add it if it's not empty.
                    output.add(tmpNumber); // Once we've found an operator we add the number.

                    // If the current is open bracket and the previous was an operand, it implies
                    // multiplication
                    if (current == '(' && !previousWasOperator)
                        // a(b+c) => a*(b+c)
                        output.add("*");
                }
                tmpNumber = ""; // Clearing it for next round.
                output.add(current + ""); // Add the current operator
                previousWasOperator = true; // Used to calculate unarynegative.
            }
        }

        // Infix should always end with a number, therefore tmpNumber should always not
        // be empty after a round. However we still make sure it's not empty.
        if (!tmpNumber.isEmpty())
            output.add(tmpNumber);

        // Converts from queue to a String[].
        String[] out = output.toArray(new String[output.size()]);
        return out;
    }

    // public static boolean isOperand(char input) {
    // String notoperands = "+-*/()";
    // return !compareChar(input, notoperands);
    // }

    public static boolean isOperand(char input) {
        return !isOperator(input + "");
    }

    public static boolean isOperand(String input) {
        return !isOperator(input);
    }

    public static boolean isParenthasis(char input) {
        String compare = "()";
        return compareChar(input, compare);
    }

    public static boolean isParenthasis(String input) {
        return input.equals("(") || input.equals(")");
    }

    public static boolean compareChar(char input, String compare) {
        for (char i : compare.toCharArray()) {
            if (i == input)
                return true;
        }
        return false;
    }

    public static int getOperandPririty(String input) {
        switch (input) {
            case "+":
            case "-":
                return 1;
            case "/":
            case "*":
                return 2;
        }
        return 0;
    }

    public static boolean isOperator(String input) {
        switch (input) {
            case "+":
            case "-":
            case "/":
            case "*":
            case "(":
            case ")":
            case "|":
            case ":":
                return true;
        }
        return false;
    }
}

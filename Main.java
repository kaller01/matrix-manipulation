import java.util.Scanner;

public class Main {
    public static Matrix matrix;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String[] size = input.nextLine().split(" ");
        if (size.length == 1)
            matrix = new Matrix(Integer.parseInt(size[0]), Integer.parseInt(size[0]));
        else
            matrix = new Matrix(Integer.parseInt(size[0]), Integer.parseInt(size[1]));
        matrix.set(input.nextLine());
        matrix.print();

        String in;
        while (true) {
            in = input.nextLine();
            if (in.equals("exit"))
                break;
            if (in.equals("d"))
                matrix = matrix.develop();
            else
                matrix.eval(in);
            matrix.print();
        }
    }



}
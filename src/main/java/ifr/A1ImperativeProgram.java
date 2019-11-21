package ifr;

import java.util.Arrays;
import java.util.List;

public class A1ImperativeProgram {

    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        //find the double of first number that is > 3 and even
        int result = 0;
        for (int n : numbers) {
            if (n > 3 && n % 2 == 0) {
                result = n * 2;
                break;
            }
        }
        System.out.println(result);

        // Questions?
        // what if the collection is empty? Result is 0.
        //so we will fix the code - ImperativeProgram2
    }
}

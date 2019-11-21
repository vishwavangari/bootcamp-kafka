package ifr;

import java.util.Arrays;
import java.util.List;

public class A2ImperativeProgram {

    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        //find the double of first number that is > 3 and even
        Integer result = null;
        for (int n : numbers) {
            if (n > 3 && n % 2 == 0) {
                result = n * 2;
                break;
            }
        }
        if (result != null) {
            System.out.println(result);
        } else {
            System.out.println("Value not found");
        }
        // Not great- because null is smell. we will have to check for all the Null pointers, before performing any operation.
    }
}

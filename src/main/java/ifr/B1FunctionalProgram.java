package ifr;

import java.util.Arrays;
import java.util.List;

public class B1FunctionalProgram {

    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        //find the double of first number that is > 3 and even
        System.out.println(numbers.stream()
                                    .filter(n -> n > 3)
                                    .filter(n -> n % 2 == 0)
                                    .map(n -> n * 2)
                                    .findFirst()
                                    .map(n -> n.toString())
                                    .orElse("No Value"));
        // streams are lazy....
        // all the functions are not invoked until terminal function is called here it is findFirst()
        // Stream contains two functions intermediate operations and terminal operations.
        // Intermediate functions return type is Stream
        // terminal functions return type either object or Optional<Object>
    }
}

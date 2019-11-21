package ifr;

import java.util.stream.IntStream;

public class D2LazyUsage {

    static D1Lazy<Integer> integerLazy = new D1Lazy<>(() -> compute(10, 20));

    public static void main(String[] args) {
        IntStream.range(0, 10)
                 .forEach((n) -> System.out.println(integerLazy.get()));
    }

    static int compute(int a, int b) {
        System.out.println("Called Lazy...");
        return a + b;
    }
}

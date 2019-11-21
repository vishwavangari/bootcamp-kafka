package ifr;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import java.util.stream.IntStream;

public class D4SupplierMemoize {

    static Supplier<Integer> integerLazy = Suppliers.memoize(() -> compute(10, 20));

    public static void main(String[] args) {
        IntStream.range(0, 10)
                .forEach(n -> System.out.println(integerLazy.get()));
    }

    static int compute(int a, int b) {
        System.out.println("Called Lazy...");
        return a + b;
    }
}

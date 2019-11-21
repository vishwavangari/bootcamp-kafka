package ifr;

import java.util.function.Supplier;

public class D1Lazy<T> {

    private T instance;
    private Supplier<T> supplier;

    /**
     *
     * @param supplier
     */
    public D1Lazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    /**
     *
     * @return
     */
    public T get() {
        synchronized (this) {
            if (instance == null) {
                instance = supplier.get();
                supplier = null;
            }
        }
        return instance;
    }
}

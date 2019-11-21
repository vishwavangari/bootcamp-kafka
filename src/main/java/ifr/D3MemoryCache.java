package ifr;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Function;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.stream.IntStream;


public class D3MemoryCache {

   private static Function<Integer, Integer> tse = ab -> compute(ab);

   private static LoadingCache<Integer, Integer> cache = CacheBuilder.newBuilder()
            .refreshAfterWrite(15, TimeUnit.MINUTES)
            .maximumSize(100)
            .build(CacheLoader.from(tse));

    private static Integer compute(int a) {
        System.out.println("Called Compute...");
        return a + a;
    }

    public static void computeCalculate(int a) {
        System.out.println(cache.getUnchecked(a));
    }

    public static void main(String[] args) {
        IntStream.range(0, 10)
                 .forEach(n -> computeCalculate(10));
    }
}

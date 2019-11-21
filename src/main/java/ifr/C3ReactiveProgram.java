package ifr;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.schedulers.Schedulers;

public class C3ReactiveProgram {

    //doOnError
    public static void main(String[] args) {
        Flowable.<Integer>create(emitter -> emit(emitter), BackpressureStrategy.BUFFER)
                .observeOn(Schedulers.io(), true, 2)
                .map(data -> data * 1)
                .doOnError(error -> System.err.println("Action after some error: " + error.getMessage()))
                .subscribe(C3ReactiveProgram::process,
                        err -> System.out.println("Catching Error: " + err));
        sleep(10000);
    }

    private static void emit(FlowableEmitter<Integer> emitter) {
        int count = 0;

        while (count < 10) {
            count++;
            if(count == 5)
                throw new RuntimeException("Exception");
            System.out.println("Emitting..." + count);
            emitter.onNext(count);
            sleep(500);
        }
        emitter.onComplete();
    }

    private static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void process(Integer integer) {
        System.out.println("Processing..." + integer);
        sleep(1000);
    }

    // Error Handling Operators: https://github.com/ReactiveX/RxJava/wiki/Error-Handling-Operators
    // onErrorNext
    // onErrorResumeNext
    // onExceptionResumeNext
    // onException
    // retry
    // retryUntil

}

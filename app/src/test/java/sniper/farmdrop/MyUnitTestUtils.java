package sniper.farmdrop;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/**
 * Created by sniper on 17-Jan-2017.
 */

public class MyUnitTestUtils {

    /**
     * This method derive from RXJava 2 (Predicate and Consumer objects). It will give us better way to test values inside Object
     * thanks to small hack and AssertJ library.
     * It transform a block of code that can throw an exception to a Predicate that returns a boolean.
     * If the code throws an exception the test fails
     * AssertJ error message usually is very accurate
     * @param consumer
     * @param <T>
     * @return
     */
    public static <T> Predicate<T> check(Consumer<T> consumer) {
        return t -> {
            consumer.accept(t);
            return true;
        };
    }
}

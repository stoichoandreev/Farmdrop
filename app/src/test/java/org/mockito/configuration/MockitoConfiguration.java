package org.mockito.configuration;

import android.support.annotation.NonNull;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import rx.Observable;
import rx.Single;

/**
 * Created by sniper on 25-Jan-2017.
 * Changing default Mockito return values
 * Using Mockito we can change default value returned in a method invocation,
 * we need to define a class MockitoConfiguration in package org.mockito.configuration
 *
 * In this class we define that, when a method returns an Observable (or a Single),
 * the value returned is an Observable (or a Single) that emits an exception.
 * In this way executing a test without defined mock behaviour we will get a proper Exception not just a NullPointerException!.
 */

public class MockitoConfiguration extends DefaultMockitoConfiguration {
    public Answer<Object> getDefaultAnswer() {
        return new ReturnsEmptyValues() {
            @Override
            public Object answer(InvocationOnMock inv) {
                Class<?> type = inv.getMethod().getReturnType();
                if (type.isAssignableFrom(rx.Observable.class)) {
                    return rx.Observable.error(createException(inv, "Observable"));
                } else if (type.isAssignableFrom(rx.Single.class)) {
                    return rx.Single.error(createException(inv, "Single"));
                } else if (type.isAssignableFrom(Observable.class)) {
                    return Observable.error(createException(inv, "Observable"));
                } else if (type.isAssignableFrom(Single.class)) {
                    return Single.error(createException(inv, "Single"));
                } else {
                    return super.answer(inv);
                }
            }
        };
    }

    @NonNull
    private RuntimeException createException(InvocationOnMock invocation, String className) {
        String s = invocation.toString();
        return new RuntimeException("No mock defined for invocation " + s +
                "\nwhen(" + s.substring(0, s.length() - 1) +
                ").thenReturn(" + className + ".just());");
    }
}

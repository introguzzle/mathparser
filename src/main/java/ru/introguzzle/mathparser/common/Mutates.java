package ru.introguzzle.mathparser.common;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface Mutates {
    String value() default "";
}

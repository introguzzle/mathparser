package ru.introguzzle.mathparser.common;

import java.lang.annotation.*;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.PARAMETER})
public @interface Mutates {
    String value() default "";
}

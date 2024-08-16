package ru.introguzzle.mathparser.parse;

import ru.introguzzle.mathparser.symbol.LambdaArgument;

public record EvaluationContext<T extends Number>(LambdaArgument<T> argument,
                                                  T from,
                                                  T to,
                                                  T delta) {
}

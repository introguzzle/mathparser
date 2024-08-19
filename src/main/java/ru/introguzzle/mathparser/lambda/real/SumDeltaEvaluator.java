package ru.introguzzle.mathparser.lambda.real;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.parse.AbstractParser;

import java.util.List;

public class SumDeltaEvaluator extends AbstractSumEvaluator {
    public SumDeltaEvaluator(AbstractParser<Double> parser) {
        super(parser, 3);
    }

    @Override
    public Double getDelta(List<Double> arguments) {
        return arguments.get(2);
    }

    @Override
    public @NotNull String getName() {
        return "sum_delta";
    }
}

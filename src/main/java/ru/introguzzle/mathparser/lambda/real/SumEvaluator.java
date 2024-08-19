package ru.introguzzle.mathparser.lambda.real;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.parse.AbstractParser;

import java.util.List;

public class SumEvaluator extends AbstractSumEvaluator {
    public SumEvaluator(AbstractParser<Double> parser) {
        super(parser, 2);
    }

    public Double getDelta(List<Double> arguments) {
        return getParser().getConverter().convert("1");
    }

    @Override
    public @NotNull String getName() {
        return "sum";
    }
}

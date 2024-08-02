package ru.introguzzle.mathparser;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.Context;
import ru.introguzzle.mathparser.common.NamingContext;
import ru.introguzzle.mathparser.common.SyntaxException;
import ru.introguzzle.mathparser.expression.Expression;
import ru.introguzzle.mathparser.expression.MathExpression;
import ru.introguzzle.mathparser.function.Function;
import ru.introguzzle.mathparser.operator.BinaryOperator;
import ru.introguzzle.mathparser.operator.Priorities;
import ru.introguzzle.mathparser.parse.MathParser;
import ru.introguzzle.mathparser.parse.Parser;
import ru.introguzzle.mathparser.symbol.Variable;
import ru.introguzzle.mathparser.tokenize.MathTokenizer;

import java.util.List;

public class Example {
    public static void main(String[] args) throws SyntaxException {
        MathTokenizer tokenizer = new MathTokenizer();
        Parser<Double> parser = new MathParser(tokenizer);

        tokenizer.addOperator(new BinaryOperator() {
            @Override
            public Double apply(List<Double> doubles) {
                return 999.0;
            }

            @Override
            public @NotNull String getName() {
                return "&&&&";
            }

            @Override
            public Association getAssociation() {
                return Association.LEFT;
            }

            @Override
            public int getPriority() {
                return Priorities.ADDITION_PRIORITY;
            }
        });

        tokenizer.addFunction(new Function() {
            @Override
            public int getRequiredArguments() {
                return 2;
            }

            @Override
            public boolean isVariadic() {
                return false;
            }

            @Override
            public Double apply(List<Double> doubles) {
                return 999.0;
            }

            @Override
            public @NotNull String getName() {
                return "example";
            }
        });

        Context context = new NamingContext();
        context.addSymbol(new Variable("x", 3));
        context.addSymbol(new Variable("y", 9));

        Expression expression = new MathExpression("1 &&&& 4 + example(1, 3) >>> x << y");
        System.out.println(parser.parse(expression, context));
    }
}

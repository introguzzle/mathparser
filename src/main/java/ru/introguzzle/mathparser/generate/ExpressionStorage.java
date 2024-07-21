package ru.introguzzle.mathparser.generate;

import ru.introguzzle.mathparser.common.Context;
import ru.introguzzle.mathparser.common.NamingContext;
import ru.introguzzle.mathparser.expression.Expression;
import ru.introguzzle.mathparser.expression.MathExpression;
import ru.introguzzle.mathparser.symbol.Variable;
import ru.introguzzle.mathparser.symbol.Variables;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ExpressionStorage {

    public static class Pair implements Map.Entry<Expression, Context> {
        private final Expression expression;
        private Context context;

        public Pair(Expression expression, Context context) {
            this.expression = expression;
            this.context = context;
        }

        @Override
        public Expression getKey() {
            return this.expression;
        }

        @Override
        public Context getValue() {
            return this.context;
        }

        @Override
        public Context setValue(Context context) {
            this.context = context;
            return this.context;
        }
    }

    private static Pair pair(String string, Variable... variables) {
        return new Pair(
                new MathExpression(string),
                new NamingContext(new Variables(variables))
        );
    }

    private static Variable v(String name) {
        return new Variable(name, 0);
    }

    public static final Collection<? extends Pair> EXPRESSIONS = List.of(
            pair("1 - 1"),
            pair("1 + 1"),
            pair("1 + 1 / 1"),
            pair("1 / 1 ** 1"),
            pair("x * y - z", v("x"), v("y"), v("z")),
            pair("(a + b) * c", v("a"), v("b"), v("c")),
            pair("sin(x) + cos(y)", v("x"), v("y")),
            pair("sqrt(a ** 2 + b ** 2)", v("a"), v("b")),
            pair("log(x) + exp(y)", v("x"), v("y")),
            pair("1 * (1 - 1) / 1"),
            pair("a * (b + c) - d", v("a"), v("b"), v("c"), v("d")),
            pair("x / (y + z) ** 2", v("x"), v("y"), v("z")),
            pair("tan(theta) - 1", v("theta")),
            pair("abs(x) + sign(y)", v("x"), v("y")),
            pair("x ** 2 + y ** 2 - 2 * x * y * cos(theta)", v("x"), v("y"), v("theta"))
    );
}

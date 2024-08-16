package ru.introguzzle.mathparser.generate;

import ru.introguzzle.mathparser.common.naming.Context;
import ru.introguzzle.mathparser.common.naming.NamingContext;
import ru.introguzzle.mathparser.expression.Expression;
import ru.introguzzle.mathparser.expression.MathExpression;
import ru.introguzzle.mathparser.symbol.Variable;
import ru.introguzzle.mathparser.symbol.Variables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public final class ExpressionStorage {
    public static final class Pair implements Map.Entry<Expression, Context<Double>> {
        private final Expression expression;
        private Context<Double> context;

        public Pair(Expression expression, Context<Double> context) {
            this.expression = expression;
            this.context = context;
        }

        @Override
        public Expression getKey() {
            return this.expression;
        }

        @Override
        public Context<Double> getValue() {
            return this.context;
        }

        @Override
        public Context<Double> setValue(Context<Double> context) {
            this.context = context;
            return this.context;
        }
    }

    @SafeVarargs
    private static Pair pair(String string, Variable<Double>... variables) {
        return new Pair(
                new MathExpression(string),
                new NamingContext<>(new Variables<>(variables))
        );
    }

    private static Variable<Double> v(String name) {
        return new Variable<>(name, 0.0);
    }

    public static final Collection<Pair> EXPRESSIONS = new ArrayList<>();

    static {
        EXPRESSIONS.add(pair("1 - 1"));
        EXPRESSIONS.add(pair("1 + 1"));
        EXPRESSIONS.add(pair("1 + 1 / 1"));
        EXPRESSIONS.add(pair("1 / 1 ** 1"));
        EXPRESSIONS.add(pair("x * y - z", v("x"), v("y"), v("z")));
        EXPRESSIONS.add(pair("(a + b) * c", v("a"), v("b"), v("c")));
        EXPRESSIONS.add(pair("sin(x) + cos(y)", v("x"), v("y")));
        EXPRESSIONS.add(pair("sqrt(a ** 2 + b ** 2)", v("a"), v("b")));
        EXPRESSIONS.add(pair("log(x) + exp(y)", v("x"), v("y")));
        EXPRESSIONS.add(pair("1 * (1 - 1) / 1"));
        EXPRESSIONS.add(pair("a * (b + c) - d", v("a"), v("b"), v("c"), v("d")));
        EXPRESSIONS.add(pair("x / (y + z) ** 2", v("x"), v("y"), v("z")));
        EXPRESSIONS.add(pair("tan(theta) - 1", v("theta")));
        EXPRESSIONS.add(pair("abs(x) + sign(y)", v("x"), v("y")));
        EXPRESSIONS.add(pair("x ** 2 + y ** 2 - 2 * x * y * cos(theta)", v("x"), v("y"), v("theta")));
        EXPRESSIONS.add(pair("(1 - 1) + (2 / 3) ** 2"));
        EXPRESSIONS.add(pair("((1 + 2) * (3 - 4)) / 5"));
        EXPRESSIONS.add(pair("sin(x + y) * cos(z - w)", v("x"), v("y"), v("z"), v("w")));
        EXPRESSIONS.add(pair("sqrt((a ** 2) + (b ** 3)) - (c / d)", v("a"), v("b"), v("c"), v("d")));
        EXPRESSIONS.add(pair("log(x) ** 2 + exp(y) / (z + 1)", v("x"), v("y"), v("z")));
        EXPRESSIONS.add(pair("(a * (b + c) ** 2) / (d - e)", v("a"), v("b"), v("c"), v("d"), v("e")));
        EXPRESSIONS.add(pair("(x / (y + z) ** 2) * (tan(theta) + 1)", v("x"), v("y"), v("z"), v("theta")));
        EXPRESSIONS.add(pair("abs(x - y) + sign(z * w)", v("x"), v("y"), v("z"), v("w")));
        EXPRESSIONS.add(pair("((x ** 2) + (y ** 2)) - (2 * x * y * cos(theta))", v("x"), v("y"), v("theta")));
        EXPRESSIONS.add(pair("atan2((y - 1), (x + 2)) * log(a + b)", v("y"), v("x"), v("a"), v("b")));
        EXPRESSIONS.add(pair("hypot((a - 1), (b + 2)) * cos(theta + pi)", v("a"), v("b"), v("theta"), v("pi")));
        EXPRESSIONS.add(pair("ceil(x + y) - floor(z / w)", v("x"), v("y"), v("z"), v("w")));
        EXPRESSIONS.add(pair("((x / y) + sqrt(z)) * (cos(a + b) - sin(c - d))", v("x"), v("y"), v("z"), v("a"), v("b"), v("c"), v("d")));
        EXPRESSIONS.add(pair("(e ** (x * y)) + (ln(z) - sin(w))", v("x"), v("y"), v("z"), v("w")));
        EXPRESSIONS.add(pair("(a * (b ** 3) - (c ** 2)) / (d + e)", v("a"), v("b"), v("c"), v("d"), v("e")));
        EXPRESSIONS.add(pair("(tan(x) + cos(y)) * (log(z) / sqrt(w))", v("x"), v("y"), v("z"), v("w")));
        EXPRESSIONS.add(pair("(x ** 3 + y ** 3 - 3 * x * y) / (a + b)", v("x"), v("y"), v("a"), v("b")));
        EXPRESSIONS.add(pair("(cos(a + b) ** 2) - (sin(c - d) ** 2)", v("a"), v("b"), v("c"), v("d")));
        EXPRESSIONS.add(pair("((x + y) * (z - w)) / (a * b + c)", v("x"), v("y"), v("z"), v("w"), v("a"), v("b"), v("c")));
        EXPRESSIONS.add(pair("(x ** 4) - (4 * x ** 2 * y ** 2) + (y ** 4)", v("x"), v("y")));
    }
}

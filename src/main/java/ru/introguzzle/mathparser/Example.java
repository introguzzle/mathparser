package ru.introguzzle.mathparser;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.SyntaxException;
import ru.introguzzle.mathparser.common.naming.Context;
import ru.introguzzle.mathparser.common.naming.NamingContext;
import ru.introguzzle.mathparser.complex.Complex;
import ru.introguzzle.mathparser.constant.real.DoubleConstant;
import ru.introguzzle.mathparser.expression.Expression;
import ru.introguzzle.mathparser.expression.MathExpression;
import ru.introguzzle.mathparser.function.real.DoubleFunction;
import ru.introguzzle.mathparser.operator.DoubleBinaryOperator;
import ru.introguzzle.mathparser.operator.Priorities;
import ru.introguzzle.mathparser.parse.ComplexParser;
import ru.introguzzle.mathparser.parse.MathParser;
import ru.introguzzle.mathparser.parse.Parser;
import ru.introguzzle.mathparser.symbol.Variable;
import ru.introguzzle.mathparser.unit.Unit;
import ru.introguzzle.mathparser.unit.measure.Measure;

import java.util.List;
import java.util.Set;

public class Example {
    public static void main(String[] args) throws SyntaxException {
        Parser<Double> parser = new MathParser();

        parser.getTokenizer().getOptions().addConstant(new DoubleConstant("z", 3) {});

        parser.getTokenizer().getOptions().addOperator(new DoubleBinaryOperator() {
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

        parser.getTokenizer().getOptions().addFunction(new DoubleFunction("example", 2) {
            @Override
            public boolean isVariadic() {
                return false;
            }

            @Override
            public @NotNull Double evaluate(List<Double> doubles) {
                return 999.0;
            }
        });

        Context<Double> context = new NamingContext<>();
        context.addSymbol(new Variable<>("x", 3.0));
        context.addSymbol(new Variable<>("y", 9.0));

        Expression expression = new MathExpression("1 &&&& z + example(1, 3) >>> x << y");
        System.out.println(parser.parse(expression, context));

        expression = new MathExpression("1111_2 << 1_2");
        System.out.println(parser.parse(expression, context));

        enum M implements Measure {
            MEASURE
        }

        abstract class BaseUnit implements Unit<M, BaseUnit> {
            private final String name;

            public BaseUnit(String name) {
                this.name = name;
            }

            @Override
            public @NotNull String getName() {
                return name;
            }

            @Override
            public @NotNull M getMeasure() {
                return M.MEASURE;
            }
        }

        class U1 extends BaseUnit {
            public U1() {
                super("FIRST");
            }

            @Override
            public double transform(double value, BaseUnit unit) {
                return value * 2;
            }

            @Override
            public @NotNull Set<String> getNames() {
                return Set.of();
            }
        }

        class U2 extends BaseUnit {
            public U2() {
                super("SECOND");
            }

            @Override
            public double transform(double value, BaseUnit unit) {
                return value * 2;
            }

            @Override
            public @NotNull Set<String> getNames() {
                return Set.of();
            }
        }

        parser.getTokenizer().getOptions().addUnit(new U1());
        parser.getTokenizer().getOptions().addUnit(new U2());
        // These units should have the same Measure and the same parent class.
        // This is because the Unit<M, U> interface is parameterized with a generic U,
        // which enforces that the unit being transformed (U) must be of the same type or a subtype of the current unit's type.

        // This decision was made so Number class can be safely transformed from one
        // measure to same measure, but in context of tokenizing and parsing it's apparently downside

        expression = new MathExpression("(1 FIRST to SECOND) FIRST to SECOND");
        System.out.println(parser.parse(expression, context));

        expression = new MathExpression("x m to km");
        System.out.println(parser.parse(expression, context));

        expression = new MathExpression("ln(e)");
        ComplexParser complexParser = new ComplexParser();
        Context<Complex> complexContext = new NamingContext<>();
        System.out.println(complexParser.parse(expression, complexContext));
    }
}

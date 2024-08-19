package ru.introguzzle.mathparser;

import org.jetbrains.annotations.NotNull;
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

    public static void main(String[] args) {
        basicMathOperationsExample();
        binaryShiftOperationsExample();
        unitConversionsExample();
        lambdaFunctionalityExample();
        complexNumberParsingExample();
    }

    private static void basicMathOperationsExample() {
        System.out.println("\n=== Basic Math Operations ===");
        Parser<Double> parser = new MathParser();

        // Adding constants and custom operators
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

        // Adding a custom function
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

        // Setting up context with variables
        Context<Double> context = new NamingContext<>();
        context.addSymbol(new Variable<>("x", 3.0));
        context.addSymbol(new Variable<>("y", 9.0));

        // Parse and evaluate expressions
        Expression expression = new MathExpression("1 &&&& z + example(1, 3) >>> x << y");
        System.out.println("Expression result: " + parser.tryParse(expression, context));
    }

    private static void binaryShiftOperationsExample() {
        System.out.println("\n=== Binary Shift Operations ===");
        Parser<Double> parser = new MathParser();

        Context<Double> context = new NamingContext<>();
        Expression expression = new MathExpression("1111_2 << 1_2");
        double result = parser.tryParse(expression, context).orElseThrow();
        System.out.println("1111 in binary shifted 1 bit to left = " + result);
        assert result == 30.0 : "Expected 30";
    }

    private static void unitConversionsExample() {
        System.out.println("\n=== Unit Conversions ===");
        Parser<Double> parser = new MathParser();

        // Define custom units with the same measure
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
            public @NotNull Set<String> getAlternativeNames() {
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
            public @NotNull Set<String> getAlternativeNames() {
                return Set.of();
            }
        }

        parser.getTokenizer().getOptions().addUnit(new U1());
        parser.getTokenizer().getOptions().addUnit(new U2());

        Context<Double> context = new NamingContext<>();
        Expression expression = new MathExpression("(1 FIRST to SECOND) FIRST to SECOND");
        double result = parser.tryParse(expression, context).orElseThrow();
        System.out.println("Unit conversion result: " + result);
        assert result == 4.0 : "Expected 8";
    }

    private static void lambdaFunctionalityExample() {
        System.out.println("\n=== Lambda Functionality ===");
        Parser<Double> parser = new MathParser();

        Context<Double> context = new NamingContext<>();
        context.addSymbol(new Variable<>("x", 3.0));
        context.addSymbol(new Variable<>("y", 9.0));

        Expression expression = new MathExpression("sum(x, y, n -> n)");
        double result = parser.tryParse(expression, context).orElseThrow();
        System.out.println("Sum lambda result: " + result);
        assert result == (double) (3 + 4 + 5 + 6 + 7 + 8 + 9) : "Expected 42";
    }

    private static void complexNumberParsingExample() {
        System.out.println("\n=== Complex Number Parsing ===");
        ComplexParser complexParser = new ComplexParser();

        Context<Complex> complexContext = new NamingContext<>();
        Expression expression = new MathExpression("ln(e)");
        Complex result = complexParser.tryParse(expression, complexContext).orElseThrow();
        System.out.println("ln(e) = " + result);
        assert result.equals(new Complex(1, 0)) : "Expected 1.0 + 0.0i";
    }
}

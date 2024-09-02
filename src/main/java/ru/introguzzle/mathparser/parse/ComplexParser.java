package ru.introguzzle.mathparser.parse;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.math.algebra.Algebra;
import ru.introguzzle.mathparser.common.math.algebra.ComplexAlgebra;
import ru.introguzzle.mathparser.common.naming.Context;
import ru.introguzzle.mathparser.complex.Complex;
import ru.introguzzle.mathparser.constant.complex.ComplexConstant;
import ru.introguzzle.mathparser.constant.complex.ComplexConstantReflector;
import ru.introguzzle.mathparser.function.Function;
import ru.introguzzle.mathparser.function.complex.ComplexFunction;
import ru.introguzzle.mathparser.function.complex.ComplexFunctionReflector;
import ru.introguzzle.mathparser.lambda.LambdaEvaluator;
import ru.introguzzle.mathparser.lambda.complex.ComplexLambdaEvaluator;
import ru.introguzzle.mathparser.operator.Operator;
import ru.introguzzle.mathparser.operator.complex.ComplexOperator;
import ru.introguzzle.mathparser.operator.complex.ComplexOperatorReflector;
import ru.introguzzle.mathparser.symbol.ImmutableSymbol;
import ru.introguzzle.mathparser.tokenize.MathTokenizer;
import ru.introguzzle.mathparser.tokenize.Tokenizer;
import ru.introguzzle.mathparser.tokenize.token.Tokens;

import java.util.Map;

/**
 * A parser implementation for mathematical expressions involving complex numbers.
 * <p>
 * The {@code ComplexParser} class is designed to parse and evaluate mathematical expressions
 * where the operands are complex numbers. It extends {@link AbstractParser} and provides specific
 * behavior tailored to complex arithmetic.
 * </p>
 * <h3>Key Characteristics:</h3>
 * <ul>
 *   <li><b>No Comparison Support:</b> Complex numbers cannot be compared in terms of greater than,
 *   less than, or equal to in the usual sense, because they are not ordered. As a result, methods
 *   related to comparison will either be unsupported or will throw exceptions if used.</li>
 *   <li><b>No Unit Conversion:</b> Complex numbers do not support conversion from or to physical units
 *   such as length, mass, or time. Any attempt to parse unit conversions involving complex numbers
 *   will result in an exception.</li>
 *   <li><b>Complex Arithmetic:</b> The parser supports complex arithmetic operations such as addition,
 *   subtraction, multiplication, and division, as well as functions that are well-defined for complex
 *   numbers (e.g., complex exponentials, logarithms).</li>
 * </ul>
 * <h3>Usage:</h3>
 * <p>
 * This parser is specifically configured to handle expressions involving complex numbers, using
 * custom functions, constants, and operators defined in the {@link ComplexFunctionReflector},
 * {@link ComplexConstantReflector}, and {@link ComplexOperatorReflector} classes, respectively.
 * </p>
 * <p>
 * The parser relies on a {@link MathTokenizer} that is preconfigured with the necessary components
 * for tokenizing expressions involving complex numbers.
 * </p>
 *
 * <h3>Example:</h3>
 * <pre>{@code
 * ComplexParser parser = new ComplexParser();
 * Complex result = parser.parse(new MathExpression("ln(1 + i)"));
 * System.out.println(result);  // Outputs the natural logarithm of the complex number 1 + i
 * }</pre>
 *
 * <h3>Limitations:</h3>
 * <ul>
 *   <li>Comparison operations are not supported for complex numbers and will throw an exception if invoked.</li>
 *   <li>Unit conversions are not supported and will throw an exception if attempted with complex numbers.</li>
 * </ul>
 *
 * @see AbstractParser
 * @see ComplexFunctionReflector
 * @see ComplexConstantReflector
 * @see ComplexOperatorReflector
 */
public class ComplexParser extends AbstractParser<Complex> {
    private final Algebra<Complex> algebra = new ComplexAlgebra();

    public ComplexParser() {
        super(new MathTokenizer(
                ComplexFunctionReflector.get(),
                ComplexConstantReflector.get(),
                ComplexOperatorReflector.get(),
                Map.of(),
                Map.of()
        ));
    }

    public ComplexParser(Tokenizer tokenizer) {
        super(tokenizer);
    }

    @Override
    public @NotNull Class<? extends LambdaEvaluator<Complex>> getLambdaClass() {
        return ComplexLambdaEvaluator.class;
    }

    @Override
    public @NotNull Class<? extends Operator<Complex>> getOperatorClass() {
        return ComplexOperator.class;
    }

    @Override
    public @NotNull Class<? extends Function<Complex>> getFunctionClass() {
        return ComplexFunction.class;
    }

    @Override
    public @NotNull Class<? extends ImmutableSymbol<Complex>> getSymbolClass() {
        return ComplexConstant.class;
    }

    @Override
    public Algebra<Complex> getAlgebra() {
        return algebra;
    }

    @Override
    protected Complex parseUnit(Complex value, Tokens tokens, Context<Complex> context) {
        throw new UnsupportedOperationException("Can't convert complex numbers");
    }

    @Override
    public NumberConverter<Complex> getConverter() {
        return NumberConverter.getComplexConverter();
    }

    // Customizing methods

    public ComplexParser addOperator(ComplexOperator operator) {
        getTokenizer().getOptions().addOperator(operator);
        return this;
    }

    public ComplexParser addFunction(ComplexFunction function) {
        getTokenizer().getOptions().addFunction(function);
        return this;
    }

    public ComplexParser addConstant(ComplexConstant constant) {
        getTokenizer().getOptions().addConstant(constant);
        return this;
    }

    public ComplexParser addLambdaEvaluator(ComplexLambdaEvaluator evaluator) {
        getTokenizer().getOptions().addLambdaEvaluator(evaluator);
        return this;
    }
}

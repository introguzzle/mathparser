package ru.introguzzle.mathparser.generate;

import org.jetbrains.annotations.Nullable;
import ru.introguzzle.mathparser.constant.real.DoubleConstant;
import ru.introguzzle.mathparser.expression.Expression;
import ru.introguzzle.mathparser.expression.MathExpression;
import ru.introguzzle.mathparser.function.Function;
import ru.introguzzle.mathparser.tokenize.Tokenizer;

public class ExpressionGenerator implements Generator<Expression> {
    private GeneratorOptions options = new GeneratorOptions(GeneratorOptions.INCLUDE_FLOATS) {};

    private static final class Depth {
        Integer current;
        Integer chance = 50;

        Depth(Integer current) {
            this.current = current;
        }

        void next() {
            this.current++;
            this.chance -= 5;
        }
    }

    private class ExpressionBuilder {

        final StringBuilder builder = new StringBuilder();
        int count = 0;

        ExpressionBuilder append(String s) {
            builder.append(s);
            return this;
        }

        ExpressionBuilder appendSpace() {
            append(" ");
            return this;
        }

        ExpressionBuilder appendOperator() {
            if (count < options.getMaxLength()) {
                String operator = createOperator();
                appendSpace().append(operator).appendSpace();
                count++;
            }

            return this;
        }

        ExpressionBuilder appendNumber() {
            if (count < options.getMaxLength()) {
                String number = createNumber();
                append(number);
                count++;
            }

            return this;
        }

        ExpressionBuilder appendSymbol() {
            if (count < options.getMaxLength()) {
                int random = Random.getRandomInteger(0, options.getDistribution().getTotal() - 1);
                int[] chances = options.getDistribution().getAccumulatedChances();

                if (random < chances[0]) {
                    return appendNumber();
                } else if (random < chances[1]) {
                    return appendConstant();
                } else if (random < chances[2]) {
                    return appendVariable();
                }
            }

            return this;
        }

        ExpressionBuilder appendVariable() {
            if (count < options.getMaxLength()) {
                String variable = options.match(GeneratorOptions.ONLY_DEFAULT_VARIABLE)
                        ? options.getDefaultVariable()
                        : createVariable();
                append(variable);
                count++;
            }

            return this;
        }

        ExpressionBuilder appendConstant() {
            if (count < options.getMaxLength()) {
                String constant = createConstant();
                append(constant);
                count++;
            }

            return this;
        }

        ExpressionBuilder appendFunction() {
            if (count < options.getMaxLength()) {
                return appendFunction(new Depth(0));
            }

            return this;
        }

        ExpressionBuilder appendFunction(Depth depth) {
            Function<?> function = Random.fromMap(tokenizer.getOptions().getFunctions());

            if (function == null) {
                throw new EmptyFunctionListException("No registered functions");
            }

            append(function.getName());
            append("(");
            int args = function.isVariadic()
                    ? function.getRequiredArguments() + Random.getRandomInteger(0, options.getMaxAdditionalVariadicArgs())
                    : function.getRequiredArguments();

            for (int i = 0; i < args; i++) {
                if (depth.current < options.getMaxDepth() && Random.getRandomInteger(0, 100) < depth.chance) {
                    appendFunction(depth);
                    depth.next();
                } else {
                    appendSymbol();
                }

                if (i < args - 1) {
                    append(", ");
                }
            }

            append(")");
            count++;

            return this;
        }

        ExpressionBuilder appendExpression() {
            if (count < options.getMaxLength()) {
                append("(")
                        .appendNumber()
                        .appendOperator()
                        .appendNumber()
                        .append(")");
                count++;
            }

            return this;
        }

        Expression get() {
            String s = builder.toString().stripTrailing();
            boolean lastOperator = false;

            String last = s.substring(s.length() - 1);

            for (int i = 0; i < options.getOperatorsSymbols().length; i++) {
                if (last.equals(options.getOperatorsSymbols()[i])) {
                    lastOperator = true;
                    break;
                }
            }

            if (lastOperator) {
                builder.append(createNumber());
            }

            return new MathExpression(builder.toString().strip());
        }
    }

    private String createVariable() {
        int i = Random.getRandomInteger(97, 122);
        String s = Character.toString((char) i);

        for (var symbol: tokenizer.getOptions().getConstants().values()) {
            if (symbol.getName().contentEquals(s)) {
                return createVariable();
            }
        }

        return s;
    }

    private String createConstant() {
        DoubleConstant constant = (DoubleConstant) Random.fromMap(tokenizer.getOptions().getConstants());
        if (constant == null) {
            throw new EmptyConstantListException("No constant present in tokenizer");
        }

        return constant.getName();
    }

    private final Tokenizer tokenizer;

    public ExpressionGenerator(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    @Override
    public Expression generate() {
        ExpressionBuilder builder = new ExpressionBuilder();
        builder.appendNumber()
                .appendOperator()
                .appendFunction()
                .appendOperator()
                .appendExpression();

        return builder.get();
    }

    @Override
    public GeneratorOptions getOptions() {
        return this.options;
    }

    public void restoreOptions() {
        this.options = new GeneratorOptions() {};
    }

    @Override
    public void setOptions(GeneratorOptions options) {
        this.options = options;
    }

    private String createOperator() {
        int number = Random.getRandomInteger(0, options.getOperatorsSymbols().length - 1);
        return options.getOperatorsSymbols()[number];
    }

    private String createInteger() {
        int number = Random.getRandomInteger(options.getMin(), options.getMax());
        return Integer.toString(number);
    }

    private String createFloat() {
        float number = Random.getRandomFloat(options.getMin(), options.getMax());
        String string = Float.toString(number);

        int decimalIndex = string.indexOf(".");
        int floatingLength = string.length() - decimalIndex - 1;

        return floatingLength > options.getMaxFloating()
                ? string.substring(0, decimalIndex + options.getMaxFloating() + 1)
                : string;
    }

    private @Nullable String createNumber() {
        if (options.matchAll(GeneratorOptions.INCLUDE_FLOATS, GeneratorOptions.INCLUDE_INTEGERS)) {
            if (Random.getRandomInteger(0, 100) < 50) {
                return createInteger();
            }

            return createFloat();
        }

        if (options.match(GeneratorOptions.INCLUDE_INTEGERS)) {
            return createInteger();
        }

        if (options.match(GeneratorOptions.INCLUDE_FLOATS)) {
            return createFloat();
        }

        return null;
    }
}

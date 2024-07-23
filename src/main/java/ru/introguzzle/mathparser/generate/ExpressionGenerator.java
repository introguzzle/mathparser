package ru.introguzzle.mathparser.generate;

import org.jetbrains.annotations.Nullable;
import ru.introguzzle.mathparser.constant.Constant;
import ru.introguzzle.mathparser.expression.Expression;
import ru.introguzzle.mathparser.function.Function;
import ru.introguzzle.mathparser.expression.MathExpression;
import ru.introguzzle.mathparser.tokenize.Tokenizer;

public class ExpressionGenerator implements Generator<Expression> {
    private GeneratorOptions options = new GeneratorOptions(GeneratorOptions.INCLUDE_FLOATS) {};

    private static class Depth {
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
            if (count < options.maxLength) {
                String operator = createOperator();
                appendSpace().append(operator).appendSpace();
                count++;
            }

            return this;
        }

        ExpressionBuilder appendNumber() {
            if (count < options.maxLength) {
                String number = createNumber();
                append(number);
                count++;
            }

            return this;
        }

        ExpressionBuilder appendSymbol() {
            if (count < options.maxLength) {
                int random = Random.randomInteger(0, options.distribution.getTotal() - 1);
                int[] chances = options.distribution.getAccumulatedChances();

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
            if (count < options.maxLength) {
                String variable = options.match(GeneratorOptions.ONLY_DEFAULT_VARIABLE)
                        ? options.defaultVariable
                        : createVariable();
                append(variable);
                count++;
            }

            return this;
        }

        ExpressionBuilder appendConstant() {
            if (count < options.maxLength) {
                String constant = createConstant();
                append(constant);
                count++;
            }

            return this;
        }

        ExpressionBuilder appendFunction() {
            if (count < options.maxLength) {
                return appendFunction(new Depth(0));
            }

            return this;
        }

        ExpressionBuilder appendFunction(Depth depth) {
            Function function = Random.pickFromCollection(tokenizer.getFunctions());

            if (function == null) {
                throw new EmptyFunctionListException("No registered functions");
            }

            append(function.getName());
            append("(");
            int args = function.isVariadic()
                    ? function.getRequiredArguments() + Random.randomInteger(0, options.maxAdditionalVariadicArgs)
                    : function.getRequiredArguments();

            for (int i = 0; i < args; i++) {
                if (depth.current < options.maxDepth && Random.randomInteger(0, 100) < depth.chance) {
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
            if (count < options.maxLength) {
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

            for (int i = 0; i < options.operators.length; i++) {
                if (last.equals(options.operators[i])) {
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
        int i = Random.randomInteger(97, 122);
        String s = Character.toString((char) i);

        for (var symbol: tokenizer.getConstants()) {
            if (symbol.getName().contentEquals(s)) {
                return createVariable();
            }
        }

        return s;
    }

    private String createConstant() {
        Constant constant = (Constant) Random.pickFromCollection(tokenizer.getConstants());
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
        int number = Random.randomInteger(0, options.operators.length - 1);
        return options.operators[number];
    }

    private String createInteger() {
        int number = Random.randomInteger(options.min, options.max);
        return Integer.toString(number);
    }

    private String createFloat() {
        float number = Random.randomFloat(options.min, options.max);
        String string = Float.toString(number);

        int decimalIndex = string.indexOf(".");
        int floatingLength = string.length() - decimalIndex - 1;

        return floatingLength > options.maxFloating
                ? string.substring(0, decimalIndex + options.maxFloating + 1)
                : string;
    }

    private @Nullable String createNumber() {
        if (options.matchAll(GeneratorOptions.INCLUDE_FLOATS, GeneratorOptions.INCLUDE_INTEGERS)) {
            if (Random.randomInteger(0, 100) < 50) {
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

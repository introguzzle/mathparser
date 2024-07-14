package ru.impl;

import org.jetbrains.annotations.Nullable;
import ru.constant.Constant;
import ru.contract.*;
import ru.contract.generator.Generator;
import ru.contract.generator.GeneratorOptions;
import ru.exceptions.generator.EmptyConstantListException;
import ru.exceptions.generator.EmptyFunctionListException;
import ru.function.Function;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class ExpressionGenerator implements Generator<Expression> {
    private GeneratorOptions options = new GeneratorOptions(GeneratorOptions.INCLUDE_FLOATS) {};

    /**
     *
     * @param min Minimal (included)
     * @param max Maximal (included)
     * @return Random integer
     */
    private static int randomInteger(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    private static float randomFloat(float min, float max) {
        return ThreadLocalRandom.current().nextFloat(min, max + 1.0f);
    }

    private static <T> T pickFromMap(Map<?, T> map) {
        Object key = pickFromCollection(map.keySet());
        return map.get(key);
    }

    private static <T> T pickFromCollection(Collection<? extends T> collection) {
        return collection.stream()
                .skip((int) (collection.size() * Math.random()))
                .findFirst()
                .orElse(null);
    }

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

        StringBuilder builder = new StringBuilder();
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
                int random = randomInteger(0, options.distribution.getTotal() - 1);
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
                String variable = createVariable();
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
            Function function = pickFromMap(tokenizer.getFunctions());

            if (function == null) {
                throw new EmptyFunctionListException("No registered functions");
            }

            append(function.getName());
            append("(");
            int args = function.isVariadic()
                    ? function.getRequiredArguments() + randomInteger(0, options.maxAdditionalVariadicArgs)
                    : function.getRequiredArguments();

            for (int i = 0; i < args; i++) {
                if (depth.current < options.maxDepth && randomInteger(0, 100) < depth.chance) {
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

            return new MathExpression(builder.toString().strip(), new ArrayList<>(), false);
        }
    }

    private String createVariable() {
        int i = randomInteger(97, 122);
        String s = Character.toString((char) i);

        for (var symbol: tokenizer.getConstants()) {
            if (symbol.getRepresentation().contentEquals(s)) {
                return createVariable();
            }
        }

        return s;
    }

    private String createConstant() {
        Constant constant = (Constant) pickFromCollection(tokenizer.getConstants());
        if (constant == null) {
            throw new EmptyConstantListException("No constant present in tokenizer");
        }

        return constant.getRepresentation();
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

    public GeneratorOptions getOptions() {
        return this.options;
    }

    public void restoreOptions() {
        this.options = new GeneratorOptions() {};
    }

    @Override
    public Generator<? extends Expression> setOptions(GeneratorOptions options) {
        this.options = options;
        return this;
    }

    private String createOperator() {
        int number = randomInteger(0, options.operators.length - 1);
        return options.operators[number];
    }

    private String createInteger() {
        int number = randomInteger(options.min, options.max);
        return Integer.toString(number);
    }

    private String createFloat() {
        float number = randomFloat(options.min, options.max);
        String string = Float.toString(number);

        int decimalIndex = string.indexOf(".");
        int floatingLength = string.length() - decimalIndex - 1;

        return floatingLength > options.maxFloating
                ? string.substring(0, decimalIndex + options.maxFloating + 1)
                : string;
    }

    private @Nullable String createNumber() {
        if (options.matchAll(GeneratorOptions.INCLUDE_FLOATS, GeneratorOptions.INCLUDE_INTEGERS)) {
            if (randomInteger(0, 100) < 50) {
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

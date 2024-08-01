package ru.introguzzle.mathparser.tokenize.token.type;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.operator.Operator;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public enum OperatorType implements ScalarType, Operator<Double> {
    ADDITION("+", Priorities.ADDITION_PRIORITY) {
        @Override
        public int operands() {
            return BINARY;
        }

        @Override
        public Double apply(List<Double> operands) {
            return operands.get(0) + operands.get(1);
        }

        @Override
        public Association getAssociation() {
            return Association.LEFT;
        }
    },

    SUBTRACTION("-", Priorities.ADDITION_PRIORITY) {
        @Override
        public int operands() {
            return BINARY;
        }

        @Override
        public Double apply(List<Double> operands) {
            return operands.get(0) - operands.get(1);
        }

        @Override
        public Association getAssociation() {
            return Association.LEFT;
        }
    },

    MULTIPLICATION("*", Priorities.MULTIPLICATION_PRIORITY) {
        @Override
        public int operands() {
            return BINARY;
        }

        @Override
        public Double apply(List<Double> operands) {
            return operands.get(0) * operands.get(1);
        }

        @Override
        public Association getAssociation() {
            return Association.LEFT;
        }
    },

    DIVISION("/", Priorities.MULTIPLICATION_PRIORITY) {
        @Override
        public int operands() {
            return BINARY;
        }

        @Override
        public Double apply(List<Double> operands) {
            return operands.get(0) / operands.get(1);
        }

        @Override
        public Association getAssociation() {
            return Association.LEFT;
        }
    },

    EXPONENT("**", Priorities.EXPONENT_PRIORITY) {
        @Override
        public int operands() {
            return BINARY;
        }

        @Override
        public Double apply(List<Double> operands) {
            return Math.pow(operands.get(0), operands.get(1));
        }

        @Override
        public Association getAssociation() {
            return Association.RIGHT;
        }
    },

    AND("&", Priorities.AND_PRIORITY) {
        @Override
        public int operands() {
            return BINARY;
        }

        @Override
        public Double apply(List<Double> operands) {
            return (double) (operands.get(0).longValue() & operands.get(1).longValue());
        }

        @Override
        public Association getAssociation() {
            return Association.LEFT;
        }
    },

    OR("|", Priorities.OR_PRIORITY) {
        @Override
        public int operands() {
            return BINARY;
        }

        @Override
        public Double apply(List<Double> operands) {
            return (double) (operands.get(0).longValue() | operands.get(1).longValue());
        }

        @Override
        public Association getAssociation() {
            return Association.LEFT;
        }
    },

    XOR("^", Priorities.XOR_PRIORITY) {
        @Override
        public int operands() {
            return BINARY;
        }

        @Override
        public Double apply(List<Double> operands) {
            return (double) (operands.get(0).longValue() ^ operands.get(1).longValue());
        }

        @Override
        public Association getAssociation() {
            return Association.LEFT;
        }
    },

    NOT("!", Priorities.UNARY_PRIORITY) {
        @Override
        public int operands() {
            return UNARY;
        }

        @Override
        public Double apply(List<Double> operands) {
            return (double) ~(operands.getFirst().longValue());
        }

        @Override
        public Association getAssociation() {
            return Association.LEFT;
        }
    },

    BITWISE_NOT("~", Priorities.UNARY_PRIORITY) {
        @Override
        public int operands() {
            return UNARY;
        }

        @Override
        public Double apply(List<Double> operands) {
            return (double) ~(operands.getFirst().longValue());
        }

        @Override
        public Association getAssociation() {
            return Association.LEFT;
        }
    },

    LEFT_SHIFT("<<", Priorities.SHIFT_PRIORITY) {
        @Override
        public int operands() {
            return BINARY;
        }

        @Override
        public Double apply(List<Double> operands) {
            return (double) (operands.get(0).longValue() << operands.get(1).longValue());
        }

        @Override
        public Association getAssociation() {
            return Association.LEFT;
        }
    },

    RIGHT_SHIFT(">>", Priorities.SHIFT_PRIORITY) {
        @Override
        public int operands() {
            return BINARY;
        }

        @Override
        public Double apply(List<Double> operands) {
            return (double) (operands.get(0).longValue() >> operands.get(1).longValue());
        }

        @Override
        public Association getAssociation() {
            return Association.LEFT;
        }
    },

    EQUALS("==", Priorities.COMPARISON_PRIORITY) {
        @Override
        public int operands() {
            return BINARY;
        }

        @Override
        public Double apply(List<Double> operands) {
            return Objects.equals(operands.get(0), operands.get(1)) ? 1.0 : 0.0;
        }

        @Override
        public Association getAssociation() {
            return Association.LEFT;
        }
    },

    NOT_EQUALS("!=", Priorities.COMPARISON_PRIORITY) {
        @Override
        public int operands() {
            return BINARY;
        }

        @Override
        public Double apply(List<Double> operands) {
            return Objects.equals(operands.get(0), operands.get(1)) ? 0.0 : 1.0;
        }

        @Override
        public Association getAssociation() {
            return Association.LEFT;
        }
    },

    GREATER(">", Priorities.COMPARISON_PRIORITY) {
        @Override
        public int operands() {
            return BINARY;
        }

        @Override
        public Double apply(List<Double> operands) {
            return operands.get(0) > operands.get(1) ? 1.0 : 0.0;
        }

        @Override
        public Association getAssociation() {
            return Association.LEFT;
        }
    },

    LESS("<", Priorities.COMPARISON_PRIORITY) {
        @Override
        public int operands() {
            return BINARY;
        }

        @Override
        public Double apply(List<Double> operands) {
            return operands.get(0) < operands.get(1) ? 1.0 : 0.0;
        }

        @Override
        public Association getAssociation() {
            return Association.LEFT;
        }
    },

    GREATER_OR_EQUALS(">=", Priorities.COMPARISON_PRIORITY) {
        @Override
        public int operands() {
            return BINARY;
        }

        @Override
        public Double apply(List<Double> operands) {
            return operands.get(0) >= operands.get(1) ? 1.0 : 0.0;
        }

        @Override
        public Association getAssociation() {
            return Association.LEFT;
        }
    },

    LESS_OR_EQUALS("<=", Priorities.COMPARISON_PRIORITY) {
        @Override
        public int operands() {
            return BINARY;
        }

        @Override
        public Double apply(List<Double> operands) {
            return operands.get(0) <= operands.get(1) ? 1.0 : 0.0;
        }

        @Override
        public Association getAssociation() {
            return Association.LEFT;
        }
    };

    public static final int BINARY = 2;
    public static final int UNARY = 1;

    private final String representation;
    private final int priority;

    OperatorType(final String representation, final int priority) {
        this.representation = representation;
        this.priority = priority;
    }

    public static List<Integer> getPriorities() {
        return Arrays.stream(OperatorType.values())
                .map(OperatorType::getPriority)
                .toList();
    }

    public static List<String> getRepresentations() {
        return Arrays.stream(OperatorType.values())
                .map(OperatorType::getRepresentation)
                .toList();
    }

    @Override
    public Category getCategory() {
        return Category.OPERATOR;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public @NotNull String getRepresentation() {
        return representation;
    }
}

package ru.introguzzle.mathparser.operator;

public final class Priorities {
    // First are functions, last are comparison operators
    private Priorities() {

    }

    public static final int FUNCTION_PRIORITY = 1;
    public static final int PARENTHESIS_PRIORITY = 1;
    public static final int UNARY_PRIORITY = 2;
    public static final int EXPONENT_PRIORITY = 3;
    public static final int MULTIPLICATION_PRIORITY = 4;
    public static final int ADDITION_PRIORITY = 5;
    public static final int SHIFT_PRIORITY = 6;
    public static final int AND_PRIORITY = 7;
    public static final int XOR_PRIORITY = 8;
    public static final int OR_PRIORITY = 9;
    public static final int COMPARISON_PRIORITY = 10;
}

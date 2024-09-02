package ru.introguzzle.mathparser.constant.bigdecimal;

import java.math.BigDecimal;

public class EulerConstant extends BigDecimalConstant {
    public EulerConstant() {
        super("e", new BigDecimal(Math.E));
    }
}

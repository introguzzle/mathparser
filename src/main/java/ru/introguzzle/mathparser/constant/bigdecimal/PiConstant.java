package ru.introguzzle.mathparser.constant.bigdecimal;

import java.math.BigDecimal;

public class PiConstant extends BigDecimalConstant {
    public PiConstant() {
        super("pi", new BigDecimal(Math.PI));
    }
}

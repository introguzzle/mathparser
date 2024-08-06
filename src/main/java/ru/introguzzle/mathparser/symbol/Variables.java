package ru.introguzzle.mathparser.symbol;

import java.util.List;

public class Variables<N extends Number> extends MutableSymbolList<Variable<N>, N> {

    public Variables() {
        super();
    }

    public Variables(List<Variable<N>> variableList) {
        super(variableList);
    }

    @SafeVarargs
    public Variables(Variable<N>... variables) {
        super(variables);
    }

    public void add(String name, N value) {
        this.add(new Variable<>(name, value));
    }
}

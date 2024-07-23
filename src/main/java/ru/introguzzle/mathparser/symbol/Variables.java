package ru.introguzzle.mathparser.symbol;

import java.util.List;

public class Variables extends MutableSymbolList<Variable> {

    public Variables() {
        super();
    }

    public Variables(List<Variable> variableList) {
        super(variableList);
    }

    public Variables(Variable... variables) {
        super(variables);
    }

    public void add(String name, double value) {
        this.add(new Variable(name, value));
    }
}

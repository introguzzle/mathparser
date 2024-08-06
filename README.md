# Math Parser

[![Build Status](https://travis-ci.org/yourusername/math-parser.svg?branch=master)](https://travis-ci.org/yourusername/math-parser)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.example/math-parser/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.example/math-parser)
[![License: Apache 2.0](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

Math Parser is a Java library for parsing and evaluating mathematical expressions with support for variables, constants, and functions.

## Features

- Parse and evaluate mathematical expressions
- Support for variables
- Support for functions (e.g., `sin`, `cos`, `sqrt`, `pow`, etc.)
- Support for binary operations (e.g., `&`, `|`, `^`, `~`, etc.)
- Configurable constants (e.g., `PI`, `E`, etc.)
- Customizable function definitions
- Customizable unary and binary operators with any association
- Various implementations

## Examples

```
package ru.introguzzle.mathparser;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.Context;
import ru.introguzzle.mathparser.common.NamingContext;
import ru.introguzzle.mathparser.common.SyntaxException;
import ru.introguzzle.mathparser.expression.Expression;
import ru.introguzzle.mathparser.expression.MathExpression;
import ru.introguzzle.mathparser.function.real.DoubleFunction;
import ru.introguzzle.mathparser.operator.DoubleBinaryOperator;
import ru.introguzzle.mathparser.operator.Priorities;
import ru.introguzzle.mathparser.parse.MathParser;
import ru.introguzzle.mathparser.parse.Parser;
import ru.introguzzle.mathparser.symbol.Variable;
import ru.introguzzle.mathparser.tokenize.MathTokenizer;

import java.util.List;

public class Example {
    public static void main(String[] args) throws SyntaxException {
        MathTokenizer tokenizer = new MathTokenizer();
        Parser<Double> parser = new MathParser(tokenizer);

        tokenizer.addOperator(new DoubleBinaryOperator() {
            @Override
            public Double apply(List<Double> doubles) {
                return 999.0;
            }

            @Override
            public @NotNull String getName() {
                return "&&&&";
            }

            @Override
            public Association getAssociation() {
                return Association.LEFT;
            }

            @Override
            public int getPriority() {
                return Priorities.ADDITION_PRIORITY;
            }
        });

        tokenizer.addFunction(new DoubleFunction("example", 2) {
            @Override
            public boolean isVariadic() {
                return false;
            }

            @Override
            public Double apply(List<Double> doubles) {
                return 999.0;
            }
        });

        Context<Double> context = new NamingContext<>();
        context.addSymbol(new Variable<>("x", 3.0));
        context.addSymbol(new Variable<>("y", 9.0));

        Expression expression = new MathExpression("1 &&&& 4 + example(1, 3) >>> x << y");
        System.out.println(parser.parse(expression, context));
    }
}

```

## Installation

To use Math Parser in your project, add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>ru.introguzzle</groupId>
    <artifactId>mathparser</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>

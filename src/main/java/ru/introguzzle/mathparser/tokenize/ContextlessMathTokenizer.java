package ru.introguzzle.mathparser.tokenize;

import ru.introguzzle.mathparser.common.Context;
import ru.introguzzle.mathparser.common.Nameable;
import ru.introguzzle.mathparser.expression.Expression;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public abstract class ContextlessMathTokenizer extends MathTokenizer {

    private final Map<String, Nameable> temporary = new HashMap<>();
    public abstract Type defaultTokenType();
    public abstract Supplier<Nameable> factory(CharSequence sequence);

    public Map<String, Nameable> getTemporary() {
        return temporary;
    }

    /**
     * Adapter class
     */
    public static class TokenizerResult extends Tokens {
        private final Map<String, Nameable> temporary = new HashMap<>();

        public Map<String, Nameable> getTemporary() {
            return temporary;
        }

        public TokenizerResult(Tokens tokens) {
            this.getTokens().clear();
            this.getTokens().addAll(tokens.getTokens());
        }
    }

    @Override
    public synchronized TokenizerResult tokenize(Expression expression, Context context) throws TokenizeException {
        Tokens tokens = super.tokenize(expression, context);
        TokenizerResult result = new TokenizerResult(tokens);
        result.getTemporary().putAll(temporary);
        temporary.clear();

        return result;
    }

    @Override
    protected Result findFromContext(Context context, CharSequence symbols) {
        temporary.put(symbols.toString(), factory(symbols).get());

        return new Result(true, new Token(defaultTokenType(), symbols));
    }
}

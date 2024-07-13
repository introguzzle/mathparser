package ru.implementation;

import org.jetbrains.annotations.NotNull;
import ru.constant.Constants;
import ru.main.Expression;
import ru.main.Symbol;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SymbolProcessor {

    private final List<? extends Symbol> symbols;

    public SymbolProcessor() {
        this.symbols = Constants.get();
    }

    public SymbolProcessor(List<? extends Symbol> symbols) {
        this.symbols = symbols;
    }

    private @NotNull Optional<? extends Symbol> find(String representation) {
        return symbols.stream()
                .filter(symbol -> symbol.getRepresentation().equals(representation))
                .findFirst();
    }

    public Expression process(@NotNull Expression expression) {
        Pattern pattern = Pattern.compile("\\b\\w+\\b");
        Matcher matcher = pattern.matcher(expression.toString());
        StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            int symbolStart = matcher.start();

            if (symbolStart >= 0 && symbolStart < expression.getLength()) {
                String symbol = matcher.group();
                Optional<? extends Symbol> match = this.find(symbol);

                if (match.isPresent()) {
                    String value = String.valueOf(match.get().getValue());
                    matcher.appendReplacement(result, value);
                } else {
                    matcher.appendReplacement(result, symbol);
                }
            }
        }

        matcher.appendTail(result);
        return new MathExpression(result.toString());
    }
}

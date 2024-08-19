package ru.introguzzle.mathparser.common.naming;

import ru.introguzzle.mathparser.symbol.MutableSymbol;
import ru.introguzzle.mathparser.symbol.MutableSymbolList;

import java.util.Optional;
import java.util.Set;

/**
 * Interface representing a context for managing symbols.
 * <p>
 * The Context interface is used for managing a collection of symbols,
 * which can be variables or constants within a specific context.
 * It supports hierarchical contexts, allowing symbols to be resolved
 * in parent contexts if they are not found in the current one.
 * </p>
 *
 * @param <N> The type of numbers used by the symbols in this context.
 */
public interface Context<N extends Number> {

    /**
     * Gets the parent context of this context.
     * <p>
     * The parent context is used to resolve symbols that are not found
     * in the current context. If no parent is set, the context is considered
     * to be the root.
     * </p>
     *
     * @return The parent context, or null if no parent is set.
     */
    Context<N> getParent();

    /**
     * Sets the parent context for this context.
     * <p>
     * The parent context allows for hierarchical symbol resolution,
     * where symbols not found in the current context are searched in the parent.
     * </p>
     *
     * @param parent The parent context to set.
     */
    void setParent(Context<N> parent);

    /**
     * Gets the list of symbols associated with this context.
     * <p>
     * The symbols are stored in a mutable list, allowing for the addition,
     * removal, and modification of symbols in the context.
     * </p>
     *
     * @return The list of mutable symbols in this context.
     */
    MutableSymbolList<MutableSymbol<N>, N> getSymbols();

    /**
     * Retrieves a symbol by name from this context.
     * <p>
     * If the symbol is not found in the current context, and a parent context
     * is set, the symbol may be searched in the parent context.
     * </p>
     *
     * @param name The name of the symbol to retrieve.
     * @return An Optional containing the symbol if found, or empty if not found.
     */
    Optional<MutableSymbol<N>> getSymbol(String name);

    /**
     * Adds a symbol to this context.
     * <p>
     * The symbol is added to the current context's list of symbols.
     * </p>
     *
     * @param symbol The symbol to add.
     * @throws NamingException If symbol's name is already in this context
     */
    void addSymbol(MutableSymbol<N> symbol) throws NamingException;

    /**
     * Removes a symbol from this context by name.
     * <p>
     * If the symbol is found in the current context, it is removed.
     * </p>
     *
     * @param name The name of the symbol to remove.
     * @return true if the symbol was removed, false if it was not found.
     */
    boolean removeSymbol(String name);

    /**
     * Removes a symbol from this context.
     * <p>
     * The symbol is removed if it exists in the current context.
     * </p>
     *
     * @param symbol The symbol to remove.
     * @return true if the symbol was removed, false if it was not found.
     */
    boolean removeSymbol(MutableSymbol<N> symbol);

    /**
     * Replaces the current list of symbols with a new list.
     * <p>
     * This operation clears the existing symbols and adds all symbols from the
     * provided list.
     * </p>
     *
     * @param symbols The new list of symbols to set.
     */
    void setSymbols(MutableSymbolList<? extends MutableSymbol<N>, N> symbols);

    /**
     * Gets the set of names for all symbols in this context.
     * <p>
     * The names are unique identifiers for the symbols managed by this context.
     * </p>
     *
     * @return A view of set of symbol names in this context.
     */
    Set<String> getNames();

    /**
     * Checks if the context contains a symbol with the specified name.
     * <p>
     * The search may extend to the parent context if the symbol is not found
     * in the current context.
     * </p>
     *
     * @param name The name of the symbol to check for.
     * @return true if the symbol exists in this context or its parent; false otherwise.
     */
    boolean contains(CharSequence name);
}

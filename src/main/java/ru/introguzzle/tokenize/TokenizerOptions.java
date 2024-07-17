package ru.introguzzle.tokenize;

import ru.introguzzle.common.Options;

public class TokenizerOptions implements Options {
    private int flags;

    /**
     * If false, tokenizer will not check strict match of mutable symbols used in context
     * and actual mutable symbols in expression
     */
    public boolean strictMode = false;

    public TokenizerOptions() {

    }

    public TokenizerOptions(int flags) {
        this.flags = flags;
    }

    @Override
    public int getFlags() {
        return flags;
    }
}

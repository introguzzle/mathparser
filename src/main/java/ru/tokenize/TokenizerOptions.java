package ru.tokenize;

import ru.common.Options;

public class TokenizerOptions implements Options {
    private int flags;

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

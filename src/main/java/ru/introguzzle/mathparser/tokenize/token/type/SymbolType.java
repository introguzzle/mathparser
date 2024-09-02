package ru.introguzzle.mathparser.tokenize.token.type;

public enum SymbolType implements Type {
    CONSTANT {
        @Override
        public boolean isMutable() {
            return false;
        }
    },

    VARIABLE {
        @Override
        public boolean isMutable() {
            return true;
        }
    },

    COEFFICIENT {
        @Override
        public boolean isMutable() {
            return true;
        }
    },

    COMPLEX_CONSTANT {
        @Override
        public boolean isMutable() {
            return false;
        }
    },

    LAMBDA_ARGUMENT {
        @Override
        public boolean isMutable() {
            return true;
        }
    },

    BIG_DECIMAL_CONSTANT {
        @Override
        public boolean isMutable() {
            return false;
        }
    };

    public abstract boolean isMutable();

    @Override
    public Category getCategory() {
        return Category.SYMBOL;
    }
}

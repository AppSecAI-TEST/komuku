package enumeration;

public enum ComboDeep {

    ZERO(0),

    TWO(2),

    FOUR(4),

    SIX(6),

    EIGHT(8),

    TEN(10);

    private int value;

    ComboDeep(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

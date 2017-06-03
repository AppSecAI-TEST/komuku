package enumeration;

public enum ComboDeep {

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

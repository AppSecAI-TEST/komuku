package enumeration;

public enum Deep {

    FOUR(4),

    SIX(6),

    EIGHT(8),

    TEN(10),

    TWELVE(12);

    Deep(int value) {
        this.value = value;
    }

    int value;

    public int getValue() {
        return value;
    }
}

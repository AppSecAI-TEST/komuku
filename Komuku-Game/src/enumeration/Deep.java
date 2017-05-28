package enumeration;

public enum Deep {

    FOUR(4),

    SIX(6);

    Deep(int value) {
        this.value = value;
    }

    int value;

    public int getValue() {
        return value;
    }
}

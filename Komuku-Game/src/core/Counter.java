package core;

public class Counter {

    int count;

    int finishStep;

    int allStep;

    void clear() {
        count = 0;
        finishStep = 0;
        allStep = 0;
    }

    void plus() {
        finishStep++;
    }

    public int getCount() {
        return count;
    }

    public int getFinishStep() {
        return finishStep;
    }

    public int getAllStep() {
        return allStep;
    }

    void setAllStep(int allStep) {
        this.allStep = allStep;
    }

}

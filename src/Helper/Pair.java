public class Pair<V,S> {

    private V firstValue;
    private S secValue;

    Pair(V firstValue, S secValue) {
        this.firstValue = firstValue;
        this.secValue = secValue;
    }

    V getFirstValue() {
        return firstValue;
    }

    S getSecValue() {
        return secValue;
    }

    public void setFirstValue(V firstValue) {
        this.firstValue = firstValue;
    }

    public void setSecValue(S secValue) {
        this.secValue = secValue;
    }
}

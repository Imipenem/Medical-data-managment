package Helper;

public class Pair<V,S> {

    private V firstValue;
    private S secValue;

    public Pair(V firstValue, S secValue) {
        this.firstValue = firstValue;
        this.secValue = secValue;
    }

    public V getFirstValue() {
        return firstValue;
    }

    public S getSecValue() {
        return secValue;
    }

    public void setFirstValue(V firstValue) {
        this.firstValue = firstValue;
    }

    public void setSecValue(S secValue) {
        this.secValue = secValue;
    }
}

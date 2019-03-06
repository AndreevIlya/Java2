import java.util.Objects;

public class DoubleKeys<K1,K2> {
    private K1 key1;
    private K2 key2;
    DoubleKeys(K1 key1, K2 key2){
        this.key1 = key1;
        this.key2 = key2;
    }

    @Override
    public int hashCode() {
        return key1.hashCode()&key2.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (o instanceof DoubleKeys) {
            DoubleKeys<K1,K2> e = (DoubleKeys<K1,K2>) o;
            return (Objects.equals(this.key1, e.key1) &&
                    Objects.equals(this.key2, e.key2));
        }
        return false;
    }
}

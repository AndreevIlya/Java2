package Multi;

public class DoubleKeys<K1,K2> {
    private K1 key1;
    private K2 key2;
    DoubleKeys(K1 key1, K2 key2){
        this.key1 = key1;
        this.key2 = key2;
    }

    K1 getKey1() {
        return key1;
    }

    K2 getKey2() {
        return key2;
    }

    @Override
    public int hashCode() {
        return key1.hashCode() + key2.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}

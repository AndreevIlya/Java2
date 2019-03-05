public interface MultiMap<K1,K2,V> {
    void put(K1 key1,K2 key2, V value);
    V get(K1 key1,K2 key2);
}

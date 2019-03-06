import java.util.HashMap;

public class MultiHashMap<K1,K2,V> extends HashMap<DoubleKeys<K1,K2>,V>
        implements MultiMap<K1,K2,V> {
    private HashMap<DoubleKeys<K1,K2>,V> hashMap = new HashMap<>();

    @Override
    public void put(K1 key1, K2 key2, V value) {
        DoubleKeys<K1,K2> doubleKey = new DoubleKeys<>(key1,key2);
        hashMap.put(doubleKey,value);
        System.out.println(key1.toString() + key2.toString() +" "+ hashMap.get(doubleKey));
    }

    @Override
    public V get(K1 key1, K2 key2){
        DoubleKeys<K1,K2> doubleKey = new DoubleKeys<>(key1,key2);
        System.out.println(hashMap.get(doubleKey));
        return hashMap.get(doubleKey);
    }
}

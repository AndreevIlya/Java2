import java.util.HashMap;

public class MultiHashMap<K1,K2,V> extends HashMap<K1,HashMap<K2,V>>
        implements MultiMap<K1,K2,V> {
    private HashMap<K2,V> hashMapInner = new HashMap<>();
    private HashMap<K1,HashMap<K2,V>> hashMap = new HashMap<>();

    @Override
    public void put(K1 key1, K2 key2, V value) {
        hashMapInner.put(key2,value);
        hashMap.put(key1,hashMapInner);
    }

    @Override
    public V get(K1 key1, K2 key2){
        return hashMap.get(key1).get(key2);
    }
}

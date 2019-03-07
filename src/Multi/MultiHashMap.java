package Multi;

import java.util.HashMap;

public class MultiHashMap<K1,K2,V> extends HashMap<DoubleKeys<K1,K2>,V>
        implements MultiMap<K1,K2,V> {
    private HashMap<DoubleKeys<K1,K2>,V> hashMap = new HashMap<>();

    @Override
    public void put(K1 key1, K2 key2, V value) {
        DoubleKeys<K1,K2> doubleKey = new DoubleKeys<>(key1,key2);
        hashMap.put(doubleKey,value);
    }

    @Override
    public V get(K1 key1, K2 key2){
        V value = null;
        for (DoubleKeys<K1,K2> doubleKey : hashMap.keySet()){
            if(doubleKey.getKey1() == key1 && doubleKey.getKey2()== key2) {
                value = hashMap.get(doubleKey);
                if (value != null) {
                    break;
                }
            }
        }
        return value;
    }

    @Override
    public boolean containsKey(K1 key1, K2 key2) {
        for(DoubleKeys<K1,K2> hashMapKey : hashMap.keySet()){
            if(hashMapKey.getKey1() == key1 &&
                    hashMapKey.getKey2() == key2){
                return true;
            }
        }
        return false;
    }
}

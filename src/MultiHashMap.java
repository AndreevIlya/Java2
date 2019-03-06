import java.util.HashMap;

public class MultiHashMap<K1,K2,V> extends HashMap<DoubleKeys<K1,K2>,V>
        implements MultiMap<K1,K2,V> {
    private HashMap<DoubleKeys<K1,K2>,V> hashMap = new HashMap<>();

    @Override
    public void put(K1 key1, K2 key2, V value) {
        DoubleKeys<K1,K2> doubleKey = new DoubleKeys<>(key1,key2);
        hashMap.put(doubleKey,value);
        System.out.println(doubleKey.getKey1() + " " + doubleKey.getKey2() + " " + hashMap.get(doubleKey));
    }

    @Override
    public V get(K1 key1, K2 key2){
        V value = null;
        for (DoubleKeys<K1,K2> doubleKey : hashMap.keySet()){
            System.out.println(doubleKey.getKey1() + " " + doubleKey.getKey2());
            if((value = hashMap.get(doubleKey)) != null){
                break;
            }
        }
        return value;
    }


    boolean containsKey(K1 key1, K2 key2) {
        for(DoubleKeys<K1,K2> hashMapKey : hashMap.keySet()){
            if(hashMapKey.getKey1() == key1 &&
                    hashMapKey.getKey2() == key2){
                return true;
            }
        }
        return false;
    }
}

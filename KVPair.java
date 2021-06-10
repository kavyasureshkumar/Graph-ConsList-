package cons;

public class KVPair<K,V> {
K key;
V value;
public KVPair(K ke, V val) {
	key = ke;
	value = val;
}
public static void main(String[] args)
{
	KVPair b = new KVPair<Integer,Integer>(2,3);
	System.out.println((Integer) b.key + (Integer) b.value);
	//System.out.println(c);
}
}

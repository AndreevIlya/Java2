import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.List;

public class ListConcurrent {
    private List<Integer> list = new LinkedList<>();
    private int quantity = 20;

    private void fillList(){
        for (int i =0;i < quantity;i++) {
            list.add(i);
        }
    }

    private void deleteNthElement(int n){
        int i =0;
        for (Integer elem : list) {
            if (i == n - 1) list.remove(elem);//But remove(i) works w/o an exception.
            i++;
        }
    }

    public static void main(String[] a){
        ListConcurrent listConcurrent = new ListConcurrent();
        listConcurrent.fillList();
        try {
            listConcurrent.deleteNthElement(4);
        }catch (ConcurrentModificationException exc){
            System.out.println("Ha-ha! ConcurrentModificationException!");

        }
    }
}

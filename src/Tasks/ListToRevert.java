package Tasks;

import java.util.ArrayList;

public class ListToRevert {
    private ArrayList<Integer> list = new ArrayList<>();

    public ListToRevert(int n){
        for(int i = 0; i < n; i++){
            list.add(i);
        }
    }

    public void revertList(){
        int size = list.size();
        ArrayList<Integer> newList = new ArrayList<>();
        for (int i = 0; i < size; i++){
            newList.add(i,list.get(size - i - 1));
        }
        list = newList;
    }

    public void showList(){
        for (Integer i : list) {
            System.out.print(i + ", ");
        }
        System.out.println();
    }
}

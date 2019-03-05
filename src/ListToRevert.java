import java.util.ArrayList;

class ListToRevert {
    private ArrayList<Integer> list = new ArrayList<>();

    ListToRevert(int n){
        for(int i = 0; i < n; i++){
            list.add(i);
        }
    }

    void revertList(){//Two ways - which better?
        int size = list.size();
        /*for (int i = size - 1; i >= 0; i--){
            list.add(size,list.get(i));
            list.remove(i);
        }*/
        ArrayList<Integer> newList = new ArrayList<>();
        for (int i = 0; i < size; i++){
            newList.add(i,list.get(size - i - 1));
        }
        list = newList;
    }

    void showList(){
        for (Integer i : list) {
            System.out.print(i + ", ");
        }
        System.out.println();
    }
}

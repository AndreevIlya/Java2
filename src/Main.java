public class Main {
    public static void main(String[] args) {
        OneLinkedList<Integer> list = new OneLinkedList<>(0);
        try {
            for (int i = 1; i < 10; i++) {
                list.add(i);
            }
            list.add(10, 10);
            list.add(12, 11);
        }catch(OutOfSizeException exc){
            System.out.println(exc.getMessage());
        }
        System.out.println("Before:");
        list.show();
        try {
            list.get(14);
        }catch(OutOfSizeException exc){
            System.out.println(exc.getMessage());
        }
        System.out.println("After:");
        list = list.revert();
        list.show();
    }
}

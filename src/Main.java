public class Main {

    public static void main(String[] args) {
        String[][] array = {{"1","2","3","4"},
                {"1","","3","4"},
                {"1","2","3","4"},
                {"1","2","3","4"}};
        try{
            MyArray arr = new MyArray(array);
            arr.show();
            arr.arrayToInt();
            System.out.println(arr.sum());
        }catch(MyArraySizeException exc){
            System.out.println("Wrong array size! Object is not created.");
        }catch(MyArrayDataException exc){
            System.out.println(exc.getMessage());
        }

    }
}

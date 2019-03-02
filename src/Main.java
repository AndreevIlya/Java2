public class Main {

    public static void main(String[] args) {
        String[][] array = {{"1","2","3","4"},
                {"1","2","3","4"},
                {"1","2","3","4"},
                {"1","2","3","4"}};
        try{
            MyArray arr = new MyArray(array);
            arr.showArray();
        }catch(MyArraySizeException exc){
            System.out.println("Wrong array size! Object is not created.");
        }

    }
}

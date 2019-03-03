import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        /*
        * Add more rows or items to any row to get a MyArraySizeException.
        * Substitute any number on anything NaN or just delete a number to get
        * a MyArrayDataException with respective message.
        * Leave it as it is to pass without exceptions.
        */
        String[][] array = {{"1","2","3","4"},
                {"1","2","3","4"},
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

        System.out.println();

        try(MyClass object = new MyClass()){
            object.read(5); //set argument to 1 to avoid an IOException
        }catch(IOException exc){
            System.out.println("Unable to read.");
        }catch (Exception exc){
            System.out.println("Unable to close.");
        }
    }
}

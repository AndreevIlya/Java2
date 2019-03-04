import static java.lang.Integer.parseInt;

class MyArray {
    private static int arraySizeLimit = 4;
    private String[][] arrayString = new String[arraySizeLimit][arraySizeLimit];
    private int[][] arrayInt = new int[arraySizeLimit][arraySizeLimit];

    MyArray(String[][] array) throws MyArraySizeException{
        if(array.length != 4){
            throw new MyArraySizeException();
        }
        for(String[] arr : array){
            if(arr.length != 4){
                throw new MyArraySizeException();
            }
        }
        for(int i = 0; i < arraySizeLimit; i++){
            System.arraycopy(array[i],0, arrayString[i],0,arraySizeLimit);
        }
    }

    void show(){
        System.out.print("{");
        for(int i = 0; i < arraySizeLimit; i++){
            showLine(i);
        }
        System.out.println("}");
    }

    void showLine(int i){
        System.out.print("{");
        for(int j = 0; j < arraySizeLimit; j++){
            if(j != arraySizeLimit - 1) {
                System.out.print(arrayString[i][j] + ", ");
            }else{
                System.out.print(arrayString[i][j]);
            }
        }
        if(i != arraySizeLimit - 1){
            System.out.println("},");
        }else{
            System.out.print("}");
        }
    }

    void arrayToInt() throws MyArrayDataException{
        for(int i = 0; i < arraySizeLimit; i++){
            for(int j = 0; j < arraySizeLimit; j++){
                if(!arrayString[i][j].equals("")) {
                    try{
                        arrayInt[i][j] = parseInt(arrayString[i][j]);
                    }catch(NumberFormatException exc){
                        throw new MyArrayDataException("In the cell at " + (i + 1) +
                                " " + (j + 1) + " there\'s no number present");
                    }
                }else{
                    throw new MyArrayDataException("The cell at " + (i + 1) +
                            " " + (j + 1) + " is empty");
                }
            }
        }
    }

    int sum(){
        int sum = 0;
        for(int i = 0; i < arraySizeLimit; i++){
            for(int j = 0; j < arraySizeLimit; j++){
                sum += arrayInt[i][j];
            }
        }
        return sum;
    }
}

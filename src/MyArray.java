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
        System.out.println("}");
    }

    void arrayToInt() throws MyArrayDataException{
        for(int i = 0; i < arraySizeLimit; i++){
            for(int j = 0; j < arraySizeLimit; j++){
                if(!arrayString[i][j].equals("")) {
                    for (int k = 0; k < arrayString[i][j].length(); k++) {
                        char c = arrayString[i][j].charAt(k);
                        if (!(c == '1' || c == '2' || c == '3' || c == '4' || c == '5' ||
                                c == '6' || c == '7' || c == '8' || c == '9' || c == '0')) {
                            throw new MyArrayDataException("In the cell at " + (i + 1) +
                                    " " + (j + 1) + " there\'s no number present");
                        }
                    }
                    arrayInt[i][j] = parseInt(arrayString[i][j]);
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

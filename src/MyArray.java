class MyArray {
    private static int arraySizeLimit = 4;
    private String[][] arrayFilled = new String[arraySizeLimit][arraySizeLimit];

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
            System.arraycopy(array[i],0,arrayFilled[i],0,arraySizeLimit);
        }
    }
    void showArray(){
        System.out.print("{");
        for(int i = 0; i < arraySizeLimit; i++){
            System.out.print("{");
            for(int j = 0; j < arraySizeLimit; j++){
                if(j != arraySizeLimit - 1) {
                    System.out.print(arrayFilled[i][j] + ", ");
                }else{
                    System.out.print(arrayFilled[i][j]);
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
}

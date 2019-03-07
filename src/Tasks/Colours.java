package Tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class Colours {
    private ArrayList<String> coloursArrayList = new ArrayList<>();
    private HashSet<String> coloursSet;
    private HashMap<String,Integer> coloursMap = new HashMap<>();

    public void showOccurrences(){
        getOccurrences();
        int counter;
        for(String colour : coloursSet){
            counter = coloursMap.get(colour);
            if (counter == 1){
                System.out.println("Colour " + colour + " occurs once.");
            }else {
                System.out.println("Colour " + colour + " occurs " + counter + " times.");
            }
        }
    }
    private void getOccurrences(){
        int counter;
        for(String colour : coloursSet){
            counter = 0;
            for(String colourFromList : coloursArrayList){
                if(colour.equals(colourFromList)){
                    counter++;
                }
            }
            coloursMap.put(colour,counter);
        }
    }

    public void findUnique(){
        coloursSet = new HashSet<>(coloursArrayList);
    }

    public int countUnique(){
        return coloursSet.size();
    }

    public Colours(){
        Random random = new Random();
        for(int i = 0; i < 20; i++){
            int randomNumber = random.nextInt(8);
            switch (randomNumber) {
                case 0:
                    coloursArrayList.add("Red");
                    break;
                case 1:
                    coloursArrayList.add("Green");
                    break;
                case 2:
                    coloursArrayList.add("Blue");
                    break;
                case 3:
                    coloursArrayList.add("Magenta");
                    break;
                case 4:
                    coloursArrayList.add("Cyan");
                    break;
                case 5:
                    coloursArrayList.add("Black");
                    break;
                case 6:
                    coloursArrayList.add("White");
                    break;
                case 7:
                    coloursArrayList.add("Yellow");
                    break;
            }
        }
    }
}

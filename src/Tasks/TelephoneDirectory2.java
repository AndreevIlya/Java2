package Tasks;

import Multi.MultiHashMap;
import java.util.Random;

public class TelephoneDirectory2{
    private final int entriesQuantity = 15;
    private final Random random = new Random();
    private final String[] surnames = {"Smith","Jones","Clark","Davidson","Hamilton",
            "Speed","Stroll","Coulthard","Stewart","Tyrrell"};
    private MultiHashMap<String,Integer,String> phoneDirectory = new MultiHashMap<>();

    public void getPhonesOfAll(){
        for(String surname : surnames){
            getPhones(surname);
        }
    }

    public void getPhones(String surname){
        if(checkSurname(surname)){
            System.out.println(surname + "\'s phone numbers are:");
            for(int i =0;i < entriesQuantity;i++){
                String phone = getPhone(surname, i);
                if(phone != null){
                    System.out.println(getPhone(surname, i));
                }
            }
            System.out.println();
        }else{
            System.out.println("Nothing is known about the phones of " + surname + ".\n");
        }
    }

    private boolean checkSurname(String surname){
        for(int i =0;i < entriesQuantity;i++){
            if(phoneDirectory.containsKey(surname, i)){
                return true;
            }
        }
        return false;
    }

    private String getPhone(String surname, int i){
        return phoneDirectory.get(surname,i);
    }

    public void addEntries(){
        for(int i =0;i < entriesQuantity;i++){
            addEntry(i);
        }
    }

    private void addEntry(int i){
        int randomNumber = random.nextInt(10);
        String surname = surnames[randomNumber];
        phoneDirectory.put(surname,i,generatePhone());
    }

    private String generatePhone(){
        StringBuilder phone = new StringBuilder(7);
        for(int i = 0; i < 7; i++){
            phone.append(random.nextInt(10));
        }
        return phone.toString();
    }
}

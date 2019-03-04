import java.util.*;

class TelephoneDirectory {
    private final Random random = new Random();
    private final String[] surnames = {"Smith","Jones","Clark","Davidson","Hamilton",
            "Speed","Stroll","Coulthard","Stewart","Tyrrell"};
    private HashMap<String,Telephones> phoneDirectory = new HashMap<>();

    private class Telephones{
        private HashMap<Integer,String> phonesOfOne = new HashMap<>();

        Telephones(){
            phonesOfOne.put(0,generatePhone());
        }

        String[] getPhonesOfOne(){
            String[] phones = new String[phonesOfOne.keySet().toArray().length];
            for(Integer phoneID : phonesOfOne.keySet()){
                phones[phoneID] = phonesOfOne.get(phoneID);
            }
            return phones;
        }

        void addPhone(){
            phonesOfOne.put(maxKey() + 1,generatePhone());
        }

        Integer maxKey(){
            Set<Integer> keys = phonesOfOne.keySet();
            Integer max = 0;
            for(Integer key : keys){
                if(max < key) max = key;
            }
            return max;
        }

        private String generatePhone(){
            StringBuilder phone = new StringBuilder(7);
            for(int i = 0; i < 7; i++){
                phone.append(random.nextInt(10));
            }
            return phone.toString();
        }

    }

    void getPhonesOfAll(){
        for(String surname : surnames){
            getPhones(surname);
        }
    }

    void getPhones(String surname){
        if(checkSurname(surname)){
            System.out.println(surname + "\'s phone numbers are:");
            String[] phones = getPhone(surname);
            for(String phone : phones){
                System.out.println(phone);
            }
            System.out.println();
        }else{
            System.out.println("Nothing is known about the phones of " + surname + ".\n");

        }
    }

    private boolean checkSurname(String surname){
        return phoneDirectory.containsKey(surname);
    }

    private String[] getPhone(String surname){
        Telephones phones = phoneDirectory.get(surname);
        return phones.getPhonesOfOne();
    }

    void addEntries(int quantity){
        for(int i = 0; i < quantity; i++){
            addEntry();
        }
    }

    private void addEntry(){
        int randomNumber = random.nextInt(10);
        String surname = surnames[randomNumber];
        if(phoneDirectory.containsKey(surname)){
            Telephones phones = phoneDirectory.get(surname);
            phones.addPhone();
            phoneDirectory.put(surnames[randomNumber],phones);
        }else{
            phoneDirectory.put(surnames[randomNumber],new Telephones());
        }

    }
}

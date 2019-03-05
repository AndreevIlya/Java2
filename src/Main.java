public class Main {

    public static void main(String[] args) {
        Colours colours = new Colours();
        colours.findUnique();
        System.out.println("There are " + colours.countUnique() + " colours.");
        colours.showOccurrences();
        System.out.println();

        TelephoneDirectory2 phones = new TelephoneDirectory2();//remove 2 to use first class
        phones.addEntries();

        phones.getPhones("Coulthard");
        phones.getPhonesOfAll();
        System.out.println();

        ListToRevert list = new ListToRevert(10);
        System.out.println("Before:");
        list.showList();
        System.out.println("After:");
        list.revertList();
        list.showList();


    }

}

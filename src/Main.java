public class Main {

    public static void main(String[] args) {
        Colours colours = new Colours();
        colours.findUnique();
        System.out.println("There are " + colours.countUnique() + " colours.");
        colours.showOccurrences();
        System.out.println();

        TelephoneDirectory phones = new TelephoneDirectory();
        phones.addEntries(20);

        phones.getPhones("Coulthard");

        phones.getPhonesOfAll();

    }

}

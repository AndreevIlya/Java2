import Tasks.Colours;
import Tasks.ListToRevert;
import Tasks.TelephoneDirectory;
import Tasks.TelephoneDirectory2;

public class Main {

    public static void main(String[] args) {
        Colours colours = new Colours();
        colours.findUnique();
        System.out.println("There are " + colours.countUnique() + " colours.");
        colours.showOccurrences();
        System.out.println();

        TelephoneDirectory phones = new TelephoneDirectory();
        phones.addEntries();
        phones.getPhones("Jones");
        phones.getPhonesOfAll();
        System.out.println("First variant over.\n");

        TelephoneDirectory2 phones2 = new TelephoneDirectory2();
        phones2.addEntries();
        phones2.getPhones("Coulthard");
        phones2.getPhonesOfAll();
        System.out.println();

        ListToRevert list = new ListToRevert(10);
        System.out.println("Before:");
        list.showList();
        System.out.println("After:");
        list.revertList();
        list.showList();


    }

}

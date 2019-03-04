public class Main {

    public static void main(String[] args) {
        Colours colours = new Colours();
        colours.findUnique();
        System.out.println("There are " + colours.countUnique() + " colours.");
        colours.showOccurrences();

    }

}

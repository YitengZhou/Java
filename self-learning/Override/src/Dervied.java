public class Dervied extends Base {
    private String name = "dervied";

    public Dervied() {
        tellName();
        printName();
    }

    public void tellName() {
        System.out.println("Dervied tell name: " + name);
    }

    public void printName() {
        System.out.println("Dervied print name: " + name);
    }
    public void printSuperName() {
        super.printName();
    }
}

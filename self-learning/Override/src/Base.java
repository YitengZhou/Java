public class Base {
    private String name = "base";

    public Base() {
        tellName();
        printName();
    }
    public void tellName() {
        System.out.println("Base tell name: " + name);
    }

    public void printName() {
        System.out.println("Base print name: " + name);
    }
}

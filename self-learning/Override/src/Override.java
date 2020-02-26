public class Override {

    public static void main(String[] args) {
        Base a = new Base();
        Dervied b = new Dervied();
        a.printName();
        b.printName();
        b.printSuperName();
    }
}

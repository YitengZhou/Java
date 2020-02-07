package doll;
import doll.Doll;
import doll.SmartDoll;

public class AppMain
{
    public static void main(String args[])
    {
        Doll beibei = new Doll("beibei");
        Doll jingjing = new Doll("jingjing");
        Doll huanhuan = new Doll("huanhuan");
        Doll yingying = new Doll("yingying");
        Doll nini = new Doll("nini");
        beibei.speak();
        jingjing.speak();
        huanhuan.speak();
        yingying.speak();
        nini.speak();
        
    }
}
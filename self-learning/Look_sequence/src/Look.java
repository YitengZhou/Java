public class Look {
    public static void main(String[] args) {
        String start = "1";
        for (int i = 0; i < 41; i++) {
            System.out.println(i + " generation length is " + start.length() + ":"+ start);
            System.out.println("1:2:3 = " + Count(start, '1') + ":" +
                    Count(start, '2') + ":" + Count(start, '3'));
            start = NextGeneration(start);
        }

    }

    public static String NextGeneration(String s){
        String newString = "";
        int flag= 1;
        for (int q = 0; q < s.length(); q++) {
            if (q<s.length()-1 && s.charAt(q)==s.charAt(q+1) ){
                flag++;
            }
            else{
                newString += flag + s.substring(q,q+1);
                flag=1;
            }
        }
        return newString;
    }

    public static int Count(String s, char num)
    {
        int count=0;
        for (int q = 0; q < s.length(); q++) {
            if (s.charAt(q)==num){
                count++;
            }
        }
        return count;
    }
}

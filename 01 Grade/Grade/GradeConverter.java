import java.io.*;

// Class to convert unit marks to grades
class GradeConverter
{
    // Converts a numerical mark (0 to 100) into a textual grade
    // Returns "Invalid" if the number is invalid
    String convertMarkToGrade(int mark)
    {   
        if (mark>=0 && mark <50)
        {
            return "Fail";
        }
        else if (mark>=50 && mark<60)
        {
            return "Pass";
        }
        else if (mark>=60 && mark<70)
        {
            return "Merit";
        }
        else if (mark>=70 && mark<=100)
        {
            return "Distinction";
        }
        else {
            return "Invalid";
        }
    }

    // Reads a mark from a String and returns the mark as an int (0 to 100)
    // Returns -1 if the string is invalid
    int convertStringToMark(String text)
    {
        int num = 0;
        int length = text.length();
        int flag = 0;
        if (length==0){
            return -1;
        }
        if (length>1 && text.charAt(0)=='0'){
            return -1;
        }
        if (text.charAt(length-1)=='%'){
            length--;
        }
        for(int i=0;i<length;i++){
            if (text.charAt(i)=='.')
            {
                length=i;
                if (text.charAt(i+1)>='5' && text.charAt(i+1)<='9'){
                    flag =1;
                }
            }
        }
        for(int i=0;i<length;i++)
        {
            int cvt = convertCharToInt(text.charAt(i));
            if (cvt == -1){
                return -1;
            }
            else
            {
                num = num + cvt*(int)Math.pow(10,(length-i-1));
            }
        }
        num = num + flag;
        if (num>=0 && num<=100)
        {
            return num;
        }
        else
        {
            return -1;
        }
    }

    // Convert a single character to an int (0 to 9)
    // Returns -1 if char is not numerical
    int convertCharToInt(char c)
    {
        int number = (int)c-(int)'0';
        if (number>=0 && number<=9){
            return number;
        }
        else{
            return -1;
        }
    }

    public static void main(String[] args) throws IOException
    {
        GradeConverter converter = new GradeConverter();
        while(true) {
            System.out.print("Please enter your mark: ");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String input = reader.readLine();
            int mark = converter.convertStringToMark(input);
            String grade = converter.convertMarkToGrade(mark);
            System.out.println("A mark of " + input + " is " + grade);
        }
    }
}

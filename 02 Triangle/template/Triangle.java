class Triangle
{
    private int firstLength;
    private int secondLength;
    private int thirdLength;
    private TriangleType type;

    // Class to represent trinagles
    Triangle(int first, int second, int third)
    {
        firstLength = first;
        secondLength = second;
        thirdLength = third;
        type = identifyTriangleType(first, second, third);
    }

    // Returns the (previously indentified) type of this triangle
    TriangleType getType()
    {
        return type;
    }

    // Returns a printable string that describes this triangle
    public String toString()
    {

        return "(" + firstLength + "," + secondLength + "," + thirdLength + ")";
    }

    // Works out what kind of triangle this is !
    static TriangleType identifyTriangleType(int first, int second, int third)
    {
        long first_l,second_l,third_l;
        first_l = first;
        second_l = second;
        third_l =third;
        if (first_l <= 0 || second_l <= 0 || third_l <= 0){
            return TriangleType.Illegal;
        }
        if (first_l + second_l < third_l || second_l + third_l < first_l || first_l + third_l < second_l){
            return TriangleType.Impossible;
        }
        if (first_l + second_l == third_l || second_l + third_l == first_l || first_l + third_l == second_l){
            return TriangleType.Flat;
        }
        if (first_l*first_l + second_l*second_l == third_l*third_l
                || second_l*second_l + third_l*third_l == first_l*first_l
                || first_l*first_l + third_l*third_l == second_l*second_l){
            return TriangleType.Right;
        }
        if (first_l == second_l && second_l == third_l){
            return TriangleType.Equilateral;
        }
        if (first_l == second_l || second_l == third_l ||first_l == third_l){
            return TriangleType.Isosceles;
        }
        if (first_l != second_l && second_l != third_l && first_l != third_l){
            return TriangleType.Scalene;
        }
        return null;
    }
}

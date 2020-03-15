public class Entity {
    private String name;
    private String description;
    private String owner;

    public Entity(String inputName, String inputDescription){
        name = inputName;
        description = inputDescription;
        owner = null;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getOwner(){
        return null;
    }
}

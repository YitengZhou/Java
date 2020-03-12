public class Entity {
    private String name;
    private String description;

    public Entity(String inputName, String inputDescription){
        name = inputName;
        description = inputDescription;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}

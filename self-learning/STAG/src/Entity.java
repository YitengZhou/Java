public class Entity {
    private String name;
    private String description;
    private String owner;
    private boolean isMovable;

    public Entity(String inputName, String inputDescription){
        name = inputName;
        description = inputDescription;
        owner = null;
        isMovable = false;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getOwner(){
        return owner;
    }
    
    public Boolean getIsMovable(){
        return isMovable;
    }
}

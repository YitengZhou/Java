/** Entity class include artefact, character, furniture, location and player class */
public class Entity {
    private String name;
    private String description;
    private boolean isMovable;

    public Entity(String inputName, String inputDescription){
        name = inputName;
        description = inputDescription;
        isMovable = false;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
    
    public Boolean getIsMovable(){
        return isMovable;
    }
}

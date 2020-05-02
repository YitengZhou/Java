/** Artefact class represent the movable artefact */
public class Artefact extends Entity {

    private boolean isMovable;

    public Artefact(String inputName, String inputDescription) {
        super(inputName, inputDescription);
        isMovable = true;
    }

    @Override
    public Boolean getIsMovable() {
        return isMovable;
    }
}

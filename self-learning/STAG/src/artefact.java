public class Artefact extends Entity {

    private String owner;
    private boolean isMovable;

    public Artefact(String inputName, String inputDescription) {
        super(inputName, inputDescription);
        isMovable = true;
    }

    public void setOwner(String ownerPlayer) {
        owner = ownerPlayer;
    }

    public String getOwner() {
        return owner;
    }

    @Override
    public Boolean getIsMovable() {
        return isMovable;
    }
}

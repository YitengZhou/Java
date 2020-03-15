public class Artefact extends Entity {

    private String owner;

    public Artefact(String inputName, String inputDescription) {
        super(inputName, inputDescription);
    }

    public void setOwner(String ownerPlayer) {
        owner = ownerPlayer;
    }

    public String getOwner() {
        return owner;
    } //可能不需要
}

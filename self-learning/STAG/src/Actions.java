// Use HashSet because any name in files is unique
import java.util.HashSet;

public class Actions {
    private HashSet<String> triggers;
    private HashSet<String> subjects;
    private HashSet<String> consumed;
    private HashSet<String> produced;
    private String narration;

    public Actions (HashSet<String> inputTriggers,HashSet<String> inputSubjects,
                  HashSet<String> inputConsumed,HashSet<String> inputProduced,
                  String inputNarration){
        triggers = inputTriggers;
        subjects = inputSubjects;
        consumed = inputConsumed;
        produced = inputProduced;
        narration = inputNarration;
    }

    public HashSet<String> getTriggers(){
        return triggers;
    }

    public HashSet<String> getSubjects(){
        return subjects;
    }

    public HashSet<String> getConsumed(){
        return consumed;
    }

    public HashSet<String> getProduced(){
        return produced;
    }

    public String getNarration(){
        return narration;
    }

}

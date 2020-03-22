/** This ParseAction class could read .json file */
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class ParseAction {
    final private ArrayList<Actions> totalActions = new ArrayList<>();

    public ParseAction(String actionFilename) throws IOException {
        try {
            JSONParser parser = new JSONParser();
            FileReader reader = new FileReader(actionFilename);
            JSONObject totalAction = (JSONObject) parser.parse(reader);
            JSONArray actions = (JSONArray) totalAction.get("actions");
            for (int i =0; i<actions.size();i++){
                JSONObject action = (JSONObject) actions.get(i);
                JSONArray triggers = (JSONArray) action.get("triggers");
                JSONArray subjects = (JSONArray) action.get("subjects");
                JSONArray consumed = (JSONArray) action.get("consumed");
                JSONArray produced = (JSONArray) action.get("produced");

                HashSet<String> actionTriggers = new HashSet<>();
                HashSet<String> actionSubjects = new HashSet<>();
                HashSet<String> actionConsumed = new HashSet<>();
                HashSet<String> actionProduced = new HashSet<>();

                actionTriggers = getActionKey(triggers);
                actionSubjects = getActionKey(subjects);
                actionConsumed = getActionKey(consumed);
                actionProduced = getActionKey(produced);
                totalActions.add(new Actions(
                        actionTriggers,
                        actionSubjects,
                        actionConsumed,
                        actionProduced,
                        action.get("narration").toString())
                );
            }
        } catch (FileNotFoundException fnfe) {
            System.out.println(fnfe);
        } catch (org.json.simple.parser.ParseException pe){
            System.out.println(pe);
        }
    }

    private HashSet<String> getActionKey(JSONArray array){
        HashSet<String> actionKey = new HashSet<>();
        for (int i=0;i<array.size();i++){
            String key = (String) array.get(i);
            actionKey.add(key);
        }
        return actionKey;
    }

    public ArrayList<Actions> getTotalActions(){
        return totalActions;
    }
}

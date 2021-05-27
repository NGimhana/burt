package sealab.burt.server.statecheckers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sealab.burt.qualitychecker.EBChecker;
import sealab.burt.qualitychecker.QualityResult;
import sealab.burt.server.StateVariable;
import sealab.burt.server.actions.ActionName;
import sealab.burt.server.conversation.UserMessage;
import sealab.burt.server.output.outputMessageObj;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static sealab.burt.server.StateVariable.*;
import static sealab.burt.server.actions.ActionName.*;

public class EBDescriptionStateChecker extends StateChecker {

    private static final Logger LOGGER = LoggerFactory.getLogger(OBDescriptionStateChecker.class);

    private final static ConcurrentHashMap<String, ActionName> nextActions= new ConcurrentHashMap<>(){{
        put(QualityResult.Result.MATCH.name(), PROVIDE_S2R_FIRST);
        put(QualityResult.Result.NO_MATCH.name(), CLARIFY_EB);
        put(QualityResult.Result.NO_PARSED.name(), PROVIDE_EB_NO_PARSE);
    }};

    public EBDescriptionStateChecker(ActionName defaultAction) {
        super(defaultAction);
    }

    @Override
    public ActionName nextAction(ConcurrentHashMap<StateVariable, Object> state) {
        try {
            QualityResult result = runEBCheck(state);
//            String description = result.getDescription();
//            String screenshotPath= result.getScreenshotPath();
//            state.put(EB_DESCRIPTION, description);
//            state.put(EB_SCREEN, screenshotPath);
            String description = "EB description";
            String screenshotPath = "app_logos/EBScreen.png";
            state.put(EB_DESCRIPTION, description);
            state.put(EB_SCREEN, screenshotPath);

            if (result.getResult().equals(QualityResult.Result.MATCH)){
//                UserMessage userMessage = (UserMessage) state.get(CURRENT_MESSAGE);
//                String message = userMessage.getMessages().get(0).getMessage();
                if (!state.containsKey(REPORT_EB)){

                    List<outputMessageObj> outputMessageList = new ArrayList<>();
                    outputMessageList.add(new outputMessageObj(description,screenshotPath));
                    state.put(REPORT_EB, outputMessageList);
                }else{
                    List<outputMessageObj> outputMessage = (List<outputMessageObj>) state.get(REPORT_EB);
                    outputMessage.add(new outputMessageObj(description, screenshotPath));
                }
            }
            return nextActions.get(result.getResult().name());
        } catch (Exception e) {
            LOGGER.error("There was an error", e);
            return null;
        }
    }

}

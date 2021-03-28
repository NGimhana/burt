package sealab.burt.server.actions.step2reproduce;

import sealab.burt.server.ChatbotMessage;
import sealab.burt.server.MessageObj;
import sealab.burt.server.UserMessage;
import sealab.burt.server.actions.ChatbotAction;
import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ConfirmSelectedAmbiguousAction extends ChatbotAction {
    static String nextIntent = "";
    @Override
    public ChatbotMessage execute(ConcurrentHashMap<String, Object> state) {
        UserMessage msg = (UserMessage) state.get("CURRENT_MESSAGE");
        String response = "";
        if (!msg.getMessages().isEmpty()) {
            String confirmMessage = msg.getMessages().get(0).getMessage();
            if (confirmMessage.equals("done")) {
                nextIntent = "";
                 List<String> S2RScreens =  msg.getMessages().get(0).getSelectedValues();
                response = MessageFormat.format("Ok, you select {0}, what is the next step?",  S2RScreens.get(0));
            }else{
                nextIntent = "none";
                // give other screens to let user choose?
                response = " Ok, what is the next step?";
                return new ChatbotMessage(response);
            }
        }
        return new ChatbotMessage(response);
    }
    @Override
    public String nextExpectedIntent() {
        return "S2R_DESCRIPTION";
    }

}

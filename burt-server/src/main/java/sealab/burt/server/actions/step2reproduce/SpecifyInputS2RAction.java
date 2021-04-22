package sealab.burt.server.actions.step2reproduce;

import sealab.burt.server.conversation.ChatbotMessage;
import sealab.burt.server.actions.ChatbotAction;
import sealab.burt.server.msgparsing.Intent;

import java.util.concurrent.ConcurrentHashMap;

public class SpecifyInputS2RAction extends ChatbotAction {

    public SpecifyInputS2RAction(Intent nextExpectedIntent) {
        super(nextExpectedIntent);
    }

    @Override
    public ChatbotMessage execute(ConcurrentHashMap<String, Object> state) {
        return new ChatbotMessage("It seems you forget to specify input. Can you please provide the input to make the step more accurately?");
    }

}

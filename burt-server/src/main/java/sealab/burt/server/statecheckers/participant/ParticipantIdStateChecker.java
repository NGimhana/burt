package sealab.burt.server.statecheckers.participant;

import sealab.burt.server.actions.ActionName;
import sealab.burt.server.conversation.state.ConversationState;
import sealab.burt.server.conversation.entity.UserResponse;
import sealab.burt.server.statecheckers.StateChecker;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Token;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static sealab.burt.server.StateVariable.*;

public class ParticipantIdStateChecker extends StateChecker {

    private static final List<String> PARTICIPANTS = IntStream.range(1, 100)
            .mapToObj(i -> "P" + i)
            .collect(Collectors.toList());

    public ParticipantIdStateChecker() {
        super(null);
    }

    @Override
    public ActionName nextAction(ConversationState state) {

        //-----------------------------
        //parse the message into tokens
        UserResponse userResponse = (UserResponse) state.get(CURRENT_MESSAGE);
        String message = userResponse.getFirstMessage().getMessage();
        List<Token> tokens = TextProcessor.processText(message).get(0).getTokens();

        //validate if any of the tokes is the participant ID
        Optional<Token> token = tokens.stream().filter(tok -> PARTICIPANTS.stream()
                .anyMatch(part -> tok.getWord().equalsIgnoreCase(part)))
                .findFirst();

        //is the participant id valid?
        boolean validParticipant = token.isPresent();
        state.put(PARTICIPANT_VALIDATED, validParticipant);

        if (validParticipant) {
            state.remove(PARTICIPANT_ASKED);
            state.put(PARTICIPANT_ID, token.get().getWord().toUpperCase());
            return ActionName.SELECT_APP;
        } else {
            return ActionName.PROVIDE_PARTICIPANT_ID;
        }
    }
}

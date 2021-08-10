package sealab.burt.server.msgparsing;

public enum Intent {

    // General Intents
    APP_SELECTED, AFFIRMATIVE_ANSWER, NEGATIVE_ANSWER, END_CONVERSATION, CONFIRM_END_CONVERSATION,

    // OB Intents
    OB_DESCRIPTION, OB_SCREEN_SELECTED,

    // EB Intents
    EB_DESCRIPTION,

    // S2R Intents
    S2R_DESCRIPTION, S2R_PREDICTED_SELECTED, S2R_MISSING_SELECTED, S2R_AMBIGUOUS_SELECTED, S2R_INPUT,

    // others
    PARTICIPANT_PROVIDED, NO_EXPECTED_INTENT
}


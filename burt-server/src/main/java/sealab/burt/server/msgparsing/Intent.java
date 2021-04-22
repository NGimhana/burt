package sealab.burt.server.msgparsing;

public enum Intent {

//    General Intents
        GREETING, APP_SELECTED, AFFIRMATIVE_ANSWER, NEGATIVE_ANSWER, THANKS, END_CONVERSATION,
//     OB Intents
        OB_DESCRIPTION, OB_SCREEN_SELECTED,
//    EB Intents
        EB_DESCRIPTION,
//    S2R Intents
        S2R_DESCRIPTION, S2R_PREDICTED_SELECTED, S2R_MISSING_SELECTED, S2R_AMBIGUOUS_SELECTED,
        //others
        NO_EXPECTED_INTENT
}


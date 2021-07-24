package sealab.burt.server;

public enum StateVariable {
    //general ones
    NEXT_INTENTS, CURRENT_MESSAGE, SESSION_ID, PARTICIPANT_ID, PARTICIPANT_VALIDATED, PARTICIPANT_ASKED,

    //app variables
    APP_NAME, APP_VERSION, APP_PACKAGE, APP_ASKED,

    //ob variables
    OB_SCREEN_SELECTED, OB_STATE, OB_DESCRIPTION, OB_MATCHED_CONFIRMATION,
    CURRENT_OB_SCREEN_POSITION, CURRENT_ATTEMPT_OB_SCREENS,
    CURRENT_ATTEMPT_OB_MATCHED,
    CURRENT_ATTEMPT_OB_NO_MATCH,

    //eb variables
    EB_SCREEN_CONFIRMATION, EB_DESCRIPTION, EB_STATE,

    //s2r variables
    COLLECTING_S2R, CONFIRM_LAST_STEP, DISAMBIGUATE_S2R, S2R_ALL_MISSING, S2R_HQ_MISSING, PREDICTING_S2R,
    PREDICTED_S2R_NUMBER_OF_PATHS, PREDICTED_S2R_PATHS_WITH_LOOPS, PREDICTED_S2R_CURRENT_PATH,

    //quality checkers/results
    EB_CHECKER, S2R_CHECKER, OB_CHECKER, EB_QUALITY_RESULT, OB_QUALITY_RESULT, S2R_QUALITY_RESULT,

    //report summary
    REPORT_OB, REPORT_EB, REPORT_S2R, REPORT_GENERATED,

    START_TIME, S2R_MATCHED_CONFIRMATION, S2R_MATCHED_MSG, END_TIME

}

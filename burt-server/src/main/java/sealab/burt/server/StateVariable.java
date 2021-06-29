package sealab.burt.server;

public enum StateVariable {
    //general ones
    NEXT_INTENTS, CURRENT_MESSAGE, SESSION_ID, PARTICIPANT_ID, PARTICIPANT_VALIDATED,  PARTICIPANT_ASKED,

    //app variables
    APP_NAME, APP_VERSION, APP_PACKAGE, APP_ASKED, APP_CONFIRMATION,

    //ob variables
    OB_SCREEN_SELECTED, OB_STATE, OB_DESCRIPTION,

    //eb variables
    //COLLECTING_EB,
    EB_SCREEN_CONFIRMATION, EB_DESCRIPTION, EB_STATE,

    //s2r variables
    COLLECTING_S2R, CONFIRM_LAST_STEP, DISAMBIGUATE_S2R, S2R_ALL_MISSING, S2R_HQ_MISSING,

    //quality checkers/results
    EB_CHECKER, S2R_CHECKER, OB_CHECKER, EB_QUALITY_RESULT, OB_QUALITY_RESULT, S2R_QUALITY_RESULT,

    //report summary
    REPORT_OB, REPORT_EB, REPORT_S2R,  OB_QUALITY_FEEDBACK,
      REPORT_GENERATED
}

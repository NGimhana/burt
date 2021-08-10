package sealab.burt.server.actions;

public enum ActionName {

    //participant
    PROVIDE_PARTICIPANT_ID,

    //app selection
    SELECT_APP, CONFIRM_APP,

    //ob actions
    PROVIDE_OB, REPHRASE_OB, SELECT_OB_SCREEN, CONFIRM_SELECTED_OB_SCREEN, PROVIDE_OB_NO_PARSE, CONFIRM_MATCHED_OB,

    //eb actions
    PROVIDE_EB, CLARIFY_EB, PROVIDE_EB_NO_PARSE,

    //general s2r actions
    PROVIDE_S2R_FIRST,  PROVIDE_S2R,  CONFIRM_LAST_STEP,

    //s2r prediction
    PREDICT_FIRST_S2R_PATH, PREDICT_NEXT_S2R_PATH,

    //missing s2r
    SELECT_MISSING_S2R, CONFIRM_SELECTED_MISSING_S2R,

    //ambiguous s2r
    DISAMBIGUATE_S2R,  CONFIRM_SELECTED_AMBIGUOUS_S2R,

    //incorrect input
    REPHRASE_S2R, SPECIFY_INPUT_S2R,  SPECIFY_NEXT_INPUT_S2R,

    //s2r match and no parse/match
   PROVIDE_S2R_NO_PARSE, CONFIRM_MATCHED_S2R, PROVIDE_S2R_NO_MATCH,

    //bug report
    REPORT_SUMMARY,

    //error
    UNEXPECTED_ERROR,

    //end
    CONFIRM_END_CONVERSATION_ACTION, END_CONVERSATION_ACTION

    //others

}

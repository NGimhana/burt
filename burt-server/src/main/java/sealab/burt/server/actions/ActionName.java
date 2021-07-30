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

    //s2r actions
    PROVIDE_S2R_FIRST, PREDICT_FIRST_S2R_PATH, PREDICT_NEXT_S2R_PATH, PROVIDE_S2R, CONFIRM_PREDICTED_SELECTED_S2R_SCREENS,
    DISAMBIGUATE_S2R, REPHRASE_S2R, SPECIFY_INPUT_S2R, SELECT_MISSING_S2R, CONFIRM_SELECTED_AMBIGUOUS_S2R,
    CONFIRM_SELECTED_MISSING_S2R, CONFIRM_LAST_STEP, PROVIDE_S2R_NO_PARSE, CONFIRM_MATCHED_S2R, CHECK_S2R_INPUT,
    SPECIFY_NEXT_INPUT_S2R,

    //bug report
    REPORT_SUMMARY,

    //error
    UNEXPECTED_ERROR,

    //end
    END_CONVERSATION, PROVIDE_S2R_NO_MATCH,

    //others

}

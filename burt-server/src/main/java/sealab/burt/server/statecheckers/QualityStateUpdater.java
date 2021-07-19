package sealab.burt.server.statecheckers;

import lombok.extern.slf4j.Slf4j;
import sealab.burt.qualitychecker.S2RChecker;
import sealab.burt.qualitychecker.UtilReporter;
import sealab.burt.qualitychecker.graph.AppStep;
import sealab.burt.qualitychecker.graph.GraphState;
import sealab.burt.qualitychecker.graph.GraphTransition;
import sealab.burt.qualitychecker.s2rquality.S2RQualityAssessment;
import sealab.burt.server.StateVariable;
import sealab.burt.server.actions.commons.ScreenshotPathUtils;
import sealab.burt.server.output.BugReportElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static sealab.burt.server.StateVariable.*;

public @Slf4j
class QualityStateUpdater {

    /**
     * add the S2R (missing steps but HQ) after adding the selected missing S2Rs
     */
    public static void addStepAndUpdateGraphState(ConcurrentHashMap<StateVariable, Object> state,
                                                  String stringStep,
                                                  S2RQualityAssessment assessment) {
        List<BugReportElement> stepElements = (List<BugReportElement>) state.get(REPORT_S2R);
        AppStep appStep = assessment.getMatchedSteps().get(0);
        String screenshotFile = ScreenshotPathUtils.getScreenshotPathForStep(appStep, state);
        if (!state.containsKey(REPORT_S2R)) {
            stepElements = new ArrayList<>(Collections.singletonList(
                    new BugReportElement(stringStep, appStep, screenshotFile)
            ));
        } else {
            stepElements.add(new BugReportElement(stringStep, appStep, screenshotFile));
        }
        state.put(REPORT_S2R, stepElements);

        //---------------------

        S2RChecker s2rChecker = (S2RChecker) state.get(S2R_CHECKER);
//        OBChecker obChecker = (OBChecker) state.get(OB_CHECKER);
//        EBChecker ebChecker = (EBChecker) state.get(EB_CHECKER);

        updateStateBasedOnStep(appStep, s2rChecker);

    }

    private static void updateStateBasedOnStep(AppStep appStep, S2RChecker s2rChecker) {

        //FIXME: some of the steps do not have transitions because they are built artificially, we should only update
        // to the target state of the transition, but right now, as a workaround, I am updating to the current state
        // of the step if the transition is null
        GraphTransition transition = appStep.getTransition();
        if (transition != null)
            s2rChecker.updateState(transition.getTargetState());
//        else {
//            s2rChecker.updateState(appStep.getCurrentState());
//        }

    }

    /**
     * add predicted step and update graph current state
     */
    public static void addPredictedStepAndUpdateGraphState(ConcurrentHashMap<StateVariable, Object> state,
                                                           AppStep appStep) {
        // add steps to state
        List<AppStep> appStepList = Collections.singletonList(appStep);
        addStepsToState(state, appStepList);
        // update graph state
        S2RChecker s2rChecker = (S2RChecker) state.get(S2R_CHECKER);
        updateStateBasedOnStep(appStep, s2rChecker);
    }


    /**
     * add the intermediate missing steps into state
     */
    public static void addStepsToState(ConcurrentHashMap<StateVariable, Object> state,
                                       List<AppStep> selectedSteps) {
        List<BugReportElement> stepElements = (List<BugReportElement>) state.get(REPORT_S2R);
        if (!state.containsKey(REPORT_S2R)) {
            stepElements = getBugReportElementsFromSteps(selectedSteps, state);
        } else {
            stepElements.addAll(getBugReportElementsFromSteps(selectedSteps, state));
        }
        state.put(REPORT_S2R, stepElements);
    }


    private static List<BugReportElement> getBugReportElementsFromSteps(List<AppStep> selectedSteps,
                                                                        ConcurrentHashMap<StateVariable, Object> state) {
        //we need to return a modifiable list
        return new ArrayList<>(selectedSteps.stream()
                .map(step -> {
                    String screenshotFile = ScreenshotPathUtils.getScreenshotPathForStep(step, state);
                    return new BugReportElement(UtilReporter.getNLStep(step, false), step, screenshotFile);
                })
                .collect(Collectors.toList()));
    }


    public static void updateOBState(ConcurrentHashMap<StateVariable, Object> state, GraphState obState) {

        log.debug("Updating OB state to: " + obState);

        String screenshotFile = ScreenshotPathUtils.getScreenshotPathForGraphState(obState, state);
        state.put(REPORT_OB, Collections.singletonList(
                new BugReportElement((String) state.get(OB_DESCRIPTION), obState, screenshotFile)));
    }


    public static void updateEBState(ConcurrentHashMap<StateVariable, Object> state, GraphState ebState) {

        log.debug("Updating EB state to: " + ebState);

        String screenshotFile = ScreenshotPathUtils.getScreenshotPathForGraphState(ebState, state);
        state.put(REPORT_EB, Collections.singletonList(
                new BugReportElement((String) state.get(EB_DESCRIPTION), ebState, screenshotFile)));
    }
}
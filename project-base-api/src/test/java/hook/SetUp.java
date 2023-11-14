package hook;

import constants.SessionVariable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.ParameterType;
import io.cucumber.java.Scenario;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import smartosc.base.scrennplay.actions.ActionCommon;

import java.io.IOException;

public class SetUp {
    @Before
    public void setTheStage(Scenario scenario) {
        OnStage.setTheStage(new OnlineCast());
        ActionCommon.setSessionVariable(SessionVariable.Common.SCENARIO, scenario.getName());
        ActionCommon.setSessionVariable(SessionVariable.Common.PATH_FEATURE, scenario.getUri().getPath());
        ActionCommon.setSessionVariable(SessionVariable.Common.DIRECTORY_FEATURE, scenario.getUri().getPath());
    }
    @After
    public void runSerenityAggregate(Scenario scenario) {
        System.out.println("After run " + scenario.getName());
    }

    @ParameterType(".*")
    public Actor actor(String actor) {
        return OnStage.theActorCalled(actor);
    }

}

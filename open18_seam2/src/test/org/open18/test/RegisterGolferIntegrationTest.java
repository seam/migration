package org.open18.test;

import java.util.List;

import javax.faces.application.FacesMessage;

import org.jboss.seam.Component;
import org.jboss.seam.core.Init;
import org.jboss.seam.mock.SeamTest;
import org.open18.model.Golfer;
import org.testng.annotations.Test;

public class RegisterGolferIntegrationTest extends SeamTest {
    
    @Test(groups = {"level.integration", "speed.slow"})
    public void registerValidGolfer() throws Exception {
		
        new FacesRequest("/register.xhtml") {

            @Override
            protected void updateModelValues() {
				Golfer golfer = (Golfer) Component.getInstance("newGolfer");
				golfer.setFirstName("Tommy");
				golfer.setLastName("Twoputt");
				golfer.setUsername("twoputt");
				golfer.setEmailAddress("twoputt@open18.org");
				setValue("#{passwordBean.password}", "ilovegolf");
				setValue("#{passwordBean.confirm}", "ilovegolf");
            }
            
            @Override
            protected void invokeApplication() {
				// FIXME: I want to verify that the event is thrown, but not call the events
				Init.instance().getObserverMethods("golferRegistered").clear();
                invokeAction("#{registerAction.register}");
				assert "success".equals(getOutcome());
				// verify we persisted
				assert (Boolean) getValue("#{newGolfer.id != null}");
				// verify quiet login
				assert (Boolean) getValue("#{identity.loggedIn}");
				assert (Boolean) getValue("#{s:hasRole('golfer')}");
            }

            @Override
            protected void renderResponse() throws Exception {
                List<FacesMessage> messages = (List<FacesMessage>) getValue("#{facesMessages.currentMessages}");
                assert messages.size() == 1;
				assert messages.get(0).getSeverity().equals(FacesMessage.SEVERITY_INFO);
				// NOTE: be i18n sensitive
				assert messages.get(0).getSummary().contains("Tommy Twoputt");
            }
        }.run();
    }
}

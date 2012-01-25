package org.open18.test;

import java.util.HashMap;
import java.util.Map;
import javax.faces.model.ListDataModel;
import org.jboss.seam.contexts.Context;
import org.testng.annotations.Test;
import org.jboss.seam.mock.SeamTest;
import org.open18.model.Golfer;
import org.jboss.seam.contexts.Contexts;

public class ProfileActionTest extends SeamTest {

	@Test
	public void selectGolferFromNewGolferList() throws Exception {
		
		final Map<String, Object> pageScopedCtxVars = new HashMap<String, Object>();
		
		new NonFacesRequest("/home.xhtml") {

			@Override
			protected void renderResponse() {
				// NOTE: emulate if fine-grained page descriptors are not on test classpath (I fixed this)
				//invokeMethod("#{profileAction.findNewGolfers}");
				
				ListDataModel newGolfers = (ListDataModel) Contexts.getPageContext().get("newGolfers");
				assert newGolfers != null && newGolfers.getRowCount() == 1;
				newGolfers.setRowIndex(0);
				Object firstRow = newGolfers.getRowData();
				assert firstRow instanceof Golfer;
				
				// NOTE: Seam doesn't preserve page-scoped context variables in test lifecycle
				Context pageContext = Contexts.getPageContext();
				for (String name : pageContext.getNames()) {
					pageScopedCtxVars.put(name, pageContext.get(name));
				}
			}
			
		}.run();

		new FacesRequest("/home.xhtml") {

			@Override
			protected void applyRequestValues() throws Exception {
				// NOTE: Seam doesn't preserve page-scoped context variables in test lifecycle
				Context pageContext = Contexts.getPageContext();
				for (Map.Entry<String, Object> entry : pageScopedCtxVars.entrySet()) {
					pageContext.set(entry.getKey(), entry.getValue());
				}
			}

			@Override
			protected void invokeApplication() {
				ListDataModel newGolfers = (ListDataModel) Contexts.getPageContext().get("newGolfers");
				newGolfers.setRowIndex(0);
				Golfer expectedSelectedGolfer = (Golfer) newGolfers.getRowData();
				invokeAction("#{profileAction.view}");
				assert "/profile.xhtml".equals(getOutcome());
				Golfer selectedGolfer = (Golfer) Contexts.getConversationContext().get("selectedGolfer");
				assert selectedGolfer != null && selectedGolfer.equals(expectedSelectedGolfer);
			}

			@Override
			protected void renderResponse() throws Exception {
				assert "/profile.xhtml".equals(getViewId());
				Golfer selectedGolfer = (Golfer) Contexts.getConversationContext().get("selectedGolfer");
				assert selectedGolfer != null;
				assert selectedGolfer.getId() != null;
			}
		}.run();
	}
	
	@Test
	public void loadGolferFromGetRequest() throws Exception {
		new NonFacesRequest("/profile.xhtml") {

			@Override
			protected void beforeRequest() {
				setParameter("golferId", "1");
			}

			@Override
			protected void renderResponse() throws Exception {
				Golfer selectedGolfer = (Golfer) Contexts.getConversationContext().get("selectedGolfer");
				assert selectedGolfer != null;
				assert selectedGolfer.getId() != null && selectedGolfer.getId().equals(1L);
				assert selectedGolfer.getName() != null && selectedGolfer.getName().equals("Jack Hackit");
			}
			
		}.run();
	}
}

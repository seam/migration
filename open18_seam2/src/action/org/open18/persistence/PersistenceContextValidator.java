package org.open18.persistence;

import javax.persistence.EntityManager;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;
import org.jboss.seam.core.Expressions.ValueExpression;

@Name("persistenceContextValidator")
@Scope(ScopeType.APPLICATION)
@Startup
@Install(false) // only needed if using persistence manager from JNDI
public class PersistenceContextValidator {
	private ValueExpression<EntityManager> entityManager;

	@Create	public void onStartup() {
		 if (entityManager != null) {
			 try {
				 EntityManager em = entityManager.getValue();
				 entityManager.setValue(null);
			 } catch (Exception e) {
				 throw new RuntimeException("The persistence context "
					 + entityManager.getExpressionString()
					 + " is not properly configured.", e);
			 }
		 }
	}

	public void setEntityManager(ValueExpression<EntityManager> entityManager) {
		 this.entityManager = entityManager;
	}
}

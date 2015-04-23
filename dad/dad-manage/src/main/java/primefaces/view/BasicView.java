package primefaces.view;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

public abstract class BasicView {
	
	public ExternalContext getContext() {
		ExternalContext externalContext = FacesContext.getCurrentInstance()
				.getExternalContext();
		return externalContext;
	}
}

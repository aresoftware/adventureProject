package adventure.servicios;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.event.FlowEvent;

public class UserWizard {
     
    private boolean skip;
       
     
    public boolean isSkip() {
        return skip;
    }
 
    public void setSkip(boolean skip) {
        this.skip = skip;
    }
     
    public String onFlowProcess(FlowEvent event) {
        if(skip) {
            skip = false;   //reset in case user goes back
            return "confirm";
        }
        else {
            return event.getNewStep();
        }
    }
}
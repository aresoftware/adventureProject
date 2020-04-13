package adventure.servicios;

import java.time.LocalDateTime;
import java.util.Map;

import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleRenderingMode;

public class ScheduleEventCita {
	
	
	private ScheduleEvent event = new DefaultScheduleEvent();
	private Cita cita;
	
	public Cita getCita() {
		return cita;
	}

	public void setCita(Cita cita) {
		this.cita = cita;
	}

	public ScheduleEvent getEvent() {
		return event;
	}

	public void setEvent(ScheduleEvent event) {
		this.event = event;
	}

	
	
	
}

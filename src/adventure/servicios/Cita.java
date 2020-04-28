package adventure.servicios;

import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.ScheduleEvent;

import com.ideario.bean.Consultas;
import com.ideario.vo.Usuario;

public class Cita {
	
	private long idCita;
	private String diaCita;
	private String horaIni;
	private String horaFin;
	private Usuario funcionarioUsu;
	private String funcionario;
	private String entrevistado;
	private Usuario entrevistadoUsu;
	private String room;
	private String url;
	private String scale;
	
	private int parTipoProceso;
	private int arpNroProceso;
	private String rprFechaRad;
	private int parEstadoActu;
	
	private Proceso proceso;
	
	public Proceso getProceso() {
		if (funcionarioUsu == null) {
			Consultas c = new Consultas();
			proceso = c.getProceso(parTipoProceso, arpNroProceso, rprFechaRad); 
		}
		return proceso;
	}

	public void setProceso(Proceso proceso) {
		this.proceso = proceso;
	}

	public Usuario getFuncionarioUsu() {
		if (funcionarioUsu == null) {
			Consultas c = new Consultas();
			funcionarioUsu = c.getUsuario(funcionario); 
		}
		return funcionarioUsu;
	}

	public void setFuncionarioUsu(Usuario funcionarioUsu) {
		this.funcionarioUsu = funcionarioUsu;
	}

	public Usuario getEntrevistadoUsu() {
		if (entrevistadoUsu == null) {
			Consultas c = new Consultas();
			entrevistadoUsu = c.getUsuario(entrevistado); 
		}
		return entrevistadoUsu;
	}

	public void setEntrevistadoUsu(Usuario entrevistadoUsu) {
		this.entrevistadoUsu = entrevistadoUsu;
	}

	private String citEstadoCita;
	private ScheduleEvent event = new DefaultScheduleEvent();
	
	public ScheduleEvent getEvent() {
		return event;
	}

	public void setEvent(ScheduleEvent event) {
		this.event = event;
	}

	public long getIdCita() {
		return idCita;
	}

	public void setIdCita(long idCita) {
		this.idCita = idCita;
	}

	public String getDiaCita() {
		return diaCita;
	}

	public void setDiaCita(String diaCita) {
		this.diaCita = diaCita;
	}

	public String getHoraIni() {
		return horaIni;
	}

	public void setHoraIni(String horaIni) {
		this.horaIni = horaIni;
	}

	public String getHoraFin() {
		return horaFin;
	}

	public void setHoraFin(String horaFin) {
		this.horaFin = horaFin;
	}

	public String getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(String funcionario) {
		this.funcionario = funcionario;
	}

	public String getEntrevistado() {
		return entrevistado;
	}

	public void setEntrevistado(String entrevistado) {
		this.entrevistado = entrevistado;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public int getParTipoProceso() {
		return parTipoProceso;
	}

	public void setParTipoProceso(int parTipoProceso) {
		this.parTipoProceso = parTipoProceso;
	}

	public int getArpNroProceso() {
		return arpNroProceso;
	}

	public void setArpNroProceso(int arpNroProceso) {
		this.arpNroProceso = arpNroProceso;
	}

	public String getRprFechaRad() {
		return rprFechaRad;
	}

	public void setRprFechaRad(String rprFechaRad) {
		this.rprFechaRad = rprFechaRad;
	}

	public int getParEstadoActu() {
		return parEstadoActu;
	}

	public void setParEstadoActu(int parEstadoActu) {
		this.parEstadoActu = parEstadoActu;
	}

	public String getCitEstadoCita() {
		return citEstadoCita;
	}

	public void setCitEstadoCita(String citEstadoCita) {
		this.citEstadoCita = citEstadoCita;
	}

	public String getUrl() {
		//url = "http://localhost:8080/adventureProject/faces/entrevista.xhtml?room="+getRoom();
		url = "http://localhost:8080/adventureProject/faces/entrevista.xhtml#a1"+getIdCita();
		//url = "https://adventureproject.net/entrevista.xhtml#a1"+getRoom();
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getScale() {
		scale = "https://adventure-a5f70.web.app/scale/scaledrone.html#3"+getRoom();
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}
	
	
	
}

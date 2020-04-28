package adventure.servicios;

import org.primefaces.model.file.UploadedFile;

public class Segex {
	
	private int parEstadoActu;
    private String nombreEstado;
    private String fecIni;
    private String fecFin;
    private UploadedFile file;
    
    
    
	public UploadedFile getFile() {
		return file;
	}
	public void setFile(UploadedFile file) {
		this.file = file;
	}
	public int getParEstadoActu() {
		return parEstadoActu;
	}
	public void setParEstadoActu(int parEstadoActu) {
		this.parEstadoActu = parEstadoActu;
	}
	public String getNombreEstado() {
		return nombreEstado;
	}
	public void setNombreEstado(String nombreEstado) {
		this.nombreEstado = nombreEstado;
	}
	public String getFecIni() {
		return fecIni;
	}
	public void setFecIni(String fecIni) {
		this.fecIni = fecIni;
	}
	public String getFecFin() {
		return fecFin;
	}
	public void setFecFin(String fecFin) {
		this.fecFin = fecFin;
	}
    
    

}

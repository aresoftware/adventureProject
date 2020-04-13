package adventure;

import java.util.ArrayList;
import java.util.List;

import adventure.servicios.TipoDocs;

public class Servicios {
	
	private String tipoDocId;
	private String tipoDocstr;
	private TipoDocs tipoDoc;
	private List<TipoDocs> tipoDocs;

	public List<TipoDocs> getTipoDocs() {
		tipoDocs = new ArrayList<>();
		tipoDocs.add(new TipoDocs(1, "CEDULA DE CIUDADANIA"));
		tipoDocs.add(new TipoDocs(2, "N.I.T."));
		tipoDocs.add(new TipoDocs(3, "CEDULA DE EXTRANJERIA"));
		tipoDocs.add(new TipoDocs(4, "TARJETA IDENTIDAD"));
		tipoDocs.add(new TipoDocs(5, "PASAPORTE"));
		tipoDocs.add(new TipoDocs(6, "REGISTRO CIVIL"));
		return tipoDocs;
	}

	public void setTipoDocs(List<TipoDocs> tipoDocs) {
		this.tipoDocs = tipoDocs;
	}

	public TipoDocs getTipoDoc() {
		return tipoDoc;
	}

	public void setTipoDoc(TipoDocs tipoDoc) {
		this.tipoDoc = tipoDoc;
	}
	
	public String getTipoDocstr() {
		return tipoDocstr;
	}

	public void setTipoDocstr(String tipoDocstr) {
		this.tipoDocstr = tipoDocstr;
	}

	public String getTipoDocId() {
		return tipoDocId;
	}

	public void setTipoDocId(String tipoDocId) {
		this.tipoDocId = tipoDocId;
	}

}

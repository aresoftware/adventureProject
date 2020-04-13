package adventure.servicios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.ideario.bean.Consultas;
import com.ideario.bean.DBConneccionSchool;

public class Proceso {
	
	private int parTipoProceso;
	private String nombreProceso;
	private int arpNroProceso;
	private String rprFechaRad;
	private int parEstadoActu;
	private String nombreEstado;
	
	private Segex sege;
	private List<Segex> seges;
	
	private boolean enFirme= false;
	private boolean salidaAutorizada= false;
	
	public boolean isSalidaAutorizada() {
		return salidaAutorizada;
	}
	public void setSalidaAutorizada(boolean salidaAutorizada) {
		this.salidaAutorizada = salidaAutorizada;
	}
	public boolean isEnFirme() {
		return enFirme;
	}
	public void setEnFirme(boolean enFirme) {
		this.enFirme = enFirme;
	}
	public int getParTipoProceso() {
		return parTipoProceso;
	}
	public void setParTipoProceso(int parTipoProceso) {
		this.parTipoProceso = parTipoProceso;
	}
	public String getNombreProceso() {
		return nombreProceso;
	}
	public void setNombreProceso(String nombreProceso) {
		this.nombreProceso = nombreProceso;
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
	public String getNombreEstado() {
		return nombreEstado;
	}
	public void setNombreEstado(String nombreEstado) {
		this.nombreEstado = nombreEstado;
	}
	public Segex getSege() {
		return sege;
	}
	public void setSege(Segex sege) {
		this.sege = sege;
	}
	public List<Segex> getSeges() {
		return seges;
	}
	public void setSeges(List<Segex> seges) {
		this.seges = seges;
		for (Segex s: seges) {
			this.parEstadoActu = s.getParEstadoActu();
			if (s.getParEstadoActu() == 147) {
				this.setEnFirme(true);
			}else if (s.getParEstadoActu() == 3) {
				this.setSalidaAutorizada(true);
			}
		}
	}
	
	public void guardarAudiencia() {
		if (ejecutarCambio(17))
			addMessage("Success", "Audiencia Guardada");
		else
			addMessage("Faild", "Audiencia con Error");
    }
	
	public void dejarEnFirme() {
		if (ejecutarCambio(147)) {
			addMessage("Success", "Proceso Dejado en Firme");
			this.setEnFirme(true);
		}
		else
			addMessage("Faild", "Audiencia con Error");
        
    }
	
	public void autorizarSalida() {
		if (ejecutarCambio(3)) {
			addMessage("Success", "Proceso Autorizado");
			this.setEnFirme(true);
			this.setSalidaAutorizada(true);
		}
		else
			addMessage("Faild", "Audiencia con Error");
        
    }
 
    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public boolean ejecutarCambio(int parEstadoActual) {
    	boolean ok = true;
		// System.err.println("ejecutarCambio");

		String lstrQuery = "";
		DBConneccionSchool db = new DBConneccionSchool();
		Connection conn = null;
		PreparedStatement lps = null;
		ResultSet rs = null;
		
		try {
			conn = db.getConexion();
			// actualiza el estado anterior con fecha de cierre
			//current en informix
			lstrQuery = "UPDATE segex_7058 set sge_fecha_fin = SYSDATE() " + 
					" where par_tipo_proceso ="+parTipoProceso + 
					" and arp_nro_proceso ="+arpNroProceso +
					" and rpr_fecha_rad ='" + rprFechaRad +
					"' and par_estado_actu = " + parEstadoActu;

			
			System.out.println("actualiza segex: " + lstrQuery);

			conn.createStatement().executeUpdate(lstrQuery);

			// inserta el nuevo estado en segex
			lstrQuery = " insert into segex_7058 (par_tipo_proceso,arp_nro_proceso,rpr_fecha_rad,"
					+ " par_estado_actu,sge_fecha_ini,usr_logname,aud_fecha_proceso)"
					+ " values (" + parTipoProceso 
					+ " ," + arpNroProceso 
					+ " ,'"+ rprFechaRad 
					+ "'," + parEstadoActual//parEstadoInserta el del cambio 
					+ ", sysdate()" //current
					+ ", current_user"
					+ ", sysdate())";
					//+ ", current, current)"; //informix
			
			System.out.println(lstrQuery);
			
			conn.createStatement().executeUpdate(lstrQuery);
			
			
		} catch (SQLException x) {
			ok = false;
			x.printStackTrace();
		}finally{
			db.cerrarConeccion(conn, lps, rs);
		}	
		
		Consultas c = new Consultas();
		this.setSeges(c.getSeguimiento(parTipoProceso, arpNroProceso, rprFechaRad));
		
		// System.out.println("EJECUTO CAMBIO1");

		// CERRAR LA SALIDA A PATIOS

		// Al cerrar el proceso, si el vehiculo estaba con salida provisional por
		// defecto la deja en definitiva
		int tipoProceso = parTipoProceso;

		if (tipoProceso != 14 || tipoProceso != 16) {
			if (parEstadoActu == 3) {
				String placa = "", entrega = "";
				// persona autorizada
				String tipo_doc = "";
				String nro_doc = "";
				String grua = "", escolta = "", astr21horas = "", astr24horas = "", astrOtro = "", astrObservacion = "",
						astrNroEntrada = "", astrDiaEntrada = "";
				String diaEntrada = "", lstrDiaEntrada = "", tipo_doc_aut = "", nro_doc_aut = "";
				
				try {

					String gstrQuery = "SELECT env_placa,asp_entrega,autsp_8037.par_tipo_doc,autsp_8037.per_nro_doc,asp_grua,"
							+ " asp_escolta ,asp_21horas,asp_24horas,asp_otro,asp_observacion, see_numero_ent, ens_dia,"
							+ " par_tipo_doc_aut, per_nro_doc_aut" + " FROM radpr_8112, autsp_8037"
							+ " where radpr_8112.par_tipo_proceso = " + parTipoProceso
							+ " and  radpr_8112.arp_nro_proceso=" + arpNroProceso 
							+ " and radpr_8112.rpr_fecha_rad='"+rprFechaRad+ "'"
							+ " and radpr_8112.par_tipo_proceso =autsp_8037.par_tipo_proceso "
							+ " and radpr_8112.arp_nro_proceso =autsp_8037.arp_nro_proceso "
							+ " and radpr_8112.rpr_fecha_rad=autsp_8037.rpr_fecha_rad ";
					// System.out.println(gstrQuery);

					rs = conn.createStatement().executeQuery(gstrQuery);
					if (rs.next()) {
						placa = rs.getString("env_placa").trim();
						entrega = rs.getString("asp_entrega");
						tipo_doc = rs.getString("par_tipo_doc");
						nro_doc = rs.getString("per_nro_doc");
						grua = rs.getString("asp_grua");
						escolta = rs.getString("asp_escolta");
						astr21horas = rs.getString("asp_21horas");
						astr24horas = rs.getString("asp_24horas");
						astrOtro = rs.getString("asp_otro");
						astrObservacion = rs.getString("asp_observacion");
						astrNroEntrada = rs.getString("see_numero_ent");
						astrDiaEntrada = rs.getString("ens_dia");

						// SOLO SI LA ENTREGA ES 2- PROVISIONAL
						/*
						if (entrega.equalsIgnoreCase("2")) {

							SalidaPatios sp = new SalidaPatios(gclaOrigen.seguridad, gclaOrigen.gconConn, gclaOrigen);
							sp.setPlaca(placa);
							sp.setTipEntrega(entrega);
							sp.setTipoProceso(Validaciones.sacarCodigo(gclaOrigen.gcomTipo));
							sp.setNroProceso(astrDatos[3]);
							sp.setFechaProceso("" + gclaOrigen.gtexFecha.getText());
							sp.setTipoDocPersona(tipo_doc);
							sp.setNroDocPersona(nro_doc);
							sp.setGrua(grua);
							sp.setTipDocSalida(tipo_doc);
							sp.setNroDocSalida(nro_doc);
							sp.setEscolta(escolta);
							sp.set21Horas(astr21horas);
							sp.set24Horas(astr24horas);
							sp.setOtro(astrOtro);
							sp.setObservacion(astrObservacion);
							sp.setCompaProceso(gclaOrigen.controlComparendo.getNumeroComparendoCompleto()); // getComparendo());
							// SE INGRESA EL NUEVO ESTADO DE DEFINITIVO

							// System.out.println("NRO PERSONA=" + sp.getNroDocPersona());

							sp.FinalizarPatio();

							// COMO EL ESTADO QEUDA COMO N AUTORIZADO, ES NECESARIO AUTORIZARLO
							gstrQuery = "update autsp_8037 set asp_estado='D'" + ", par_tipo_doc_aut="
									+ res.getString("par_tipo_doc_aut") + ", per_nro_doc_aut="
									+ res.getString("per_nro_doc_aut") + " where env_placa = '" + placa + "'"
									+ " and asp_estado in('N')" + " and autsp_8037.par_tipo_proceso = "
									+ Validaciones.sacarCodigo(gclaOrigen.gcomTipo)
									+ " and  autsp_8037.arp_nro_proceso=" + astrDatos[3]
									+ " and autsp_8037.rpr_fecha_rad='" + Validaciones.convertirFechaSql(astrDatos[4])
									+ "'";

							// System.out.println("" + gstrQuery);
							gclaOrigen.gconConn.createStatement().executeUpdate(gstrQuery);

							try {
								gclaOrigen.gconConn.createStatement().executeUpdate(gstrQuery);
								gstrQuery = "update enspa_8035 set ens_estado='D'" + " where env_placa = '" + placa
										+ "'" + "  and ens_estado in('N')" + " and see_numero_ent=" + astrNroEntrada
										+ " and ens_dia='" + astrDiaEntrada + "'";
								// System.out.println(gstrQuery);
								gclaOrigen.gconConn.createStatement().executeUpdate(gstrQuery);
							} catch (SQLException x) {
								x.printStackTrace();
							}

						}*/

					}
				} catch (SQLException e) {
					ok = false;
					e.printStackTrace();
				}

			}

		}
		return ok;
	}

    public void actualizaEnFirme(){
    	this.enFirme = true;
    	
    }
    public void actualizaSalida(){
    	this.enFirme = true;
    	this.salidaAutorizada = true;
    }
}

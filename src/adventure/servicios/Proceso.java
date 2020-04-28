package adventure.servicios;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

//import com.datatools.admobiblio.ListaHash;
//import com.datatools.admobiblio.Validaciones;
//import com.datatools.admobiblio.info.Comparendo;
//import com.datatools.admobiblio.info.InfoNotFoundException;
//import com.datatools.inspeccion.ConsultasGenerales;
//import com.datatools.inspeccion.procesos.GestorDecisionJudicial;
import com.ideario.bean.Consultas;
import com.ideario.bean.DBConneccionSchool;

import adventure.servicios.vo.EstadoParam;

//import biblio_it.Fechas;
//import inspeccion.datos.DataSeguimiento;

public class Proceso {
	
	private int parTipoProceso;
	private String nombreProceso;
	private int arpNroProceso;
	private String rprFechaRad;
	private int parEstadoActu =0;
	private int parEstadoAnt = 0;
	private String nombreEstado;
	private String comNumero;
	
	private Segex sege;
	private List<Segex> seges;
	
	private EstadoParam estado;
	private List<EstadoParam> estados;
	private HtmlDataTable htmlDataTable;
	
	private boolean enFirme= false;
	private boolean salidaAutorizada= false;
	private boolean informix = false;
	
	public boolean isInformix() {
		return informix;
	}
	public void setInformix(boolean informix) {
		this.informix = informix;
	}
	public String getComNumero() {
		return comNumero;
	}
	public void setComNumero(String comNumero) {
		this.comNumero = comNumero;
	}
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
	
	public int getParEstadoAnt() {
		return parEstadoAnt;
	}
	public void setParEstadoAnt(int parEstadoAnt) {
		this.parEstadoAnt = parEstadoAnt;
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
	}
	
	public EstadoParam getEstado() {
		return estado;
	}
	public void setEstado(EstadoParam estado) {
		this.estado = estado;
	}
	public List<EstadoParam> getEstados() {
		return estados;
	}
	public void setEstados(List<EstadoParam> estados) {
		this.estados = estados;
	}
	
	public HtmlDataTable getHtmlDataTable() {
		return htmlDataTable;
	}
	public void setHtmlDataTable(HtmlDataTable htmlDataTable) {
		this.htmlDataTable = htmlDataTable;
	}
	
	public String guardarAudiencia(int paso) {
		if (ejecutarCambio(paso))
			addMessage("Success", "Audiencia Guardada");
		else
			addMessage("Faild", "Audiencia con Error");
		
		return "";
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
    public String getFechaActual(){
    	
    		return "today";
    	
    }
    
    public String getUsrDataBase(){
    	
    		return "user";
    	
    }
    
    public String getHoraActual(){
    	
    		return "(SELECT CURRENT HOUR TO MINUTE from empre_0000)";
    	
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
			lstrQuery = "UPDATE segex_7058 set sge_fecha_fin = today " +  
					" where par_tipo_proceso ="+parTipoProceso + 
					" and arp_nro_proceso ="+arpNroProceso +
					" and rpr_fecha_rad ='" + rprFechaRad +
					"' and par_estado_actu = " + parEstadoActu;

			
			System.out.println("actualiza segex: " + lstrQuery);

			conn.createStatement().executeUpdate(lstrQuery);

			// inserta el nuevo estado en segex
			lstrQuery = " insert into segex_7058 (par_tipo_proceso,arp_nro_proceso,rpr_fecha_rad,"
					+ " par_estado_actu,sge_fecha_ini,sge_hora_ini,usr_logname,aud_fecha_proceso)"
					+ " values (" + parTipoProceso 
					+ " ," + arpNroProceso 
					+ " ,'"+ rprFechaRad 
					+ "'," + parEstadoActual//parEstadoInserta el del cambio 
					+ ", "+getFechaActual()
					+ ", "+getHoraActual()
					+ ", "+getUsrDataBase()
					+ ", "+getFechaActual()+")";
			
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
    
    public boolean ejecutarCambioInformix(int parEstadoActual) {
    	boolean ok = true;
		// System.err.println("ejecutarCambio");

		String lstrQuery = "";
		DBConneccionSchool db = new DBConneccionSchool();
		Connection conn = null;
		PreparedStatement lps = null;
		ResultSet rs = null;
		
		try {
			//conn = db.getConexion();
			conn = db.getConexionInformix();
			// actualiza el estado anterior con fecha de cierre
			//current en informix
			lstrQuery = "UPDATE segex_7058 set sge_fecha_fin = current " + 
					" where par_tipo_proceso ="+parTipoProceso + 
					" and arp_nro_proceso ="+arpNroProceso +
					" and rpr_fecha_rad ='" + rprFechaRad +
					"' and par_estado_actu = " + parEstadoActu;

			
			System.out.println("actualiza segex: " + lstrQuery);

			conn.createStatement().executeUpdate(lstrQuery);

			// inserta el nuevo estado en segex
			lstrQuery = " insert into segex_7058 (par_tipo_proceso,arp_nro_proceso,rpr_fecha_rad,"
					+ " par_estado_actu,sge_fecha_ini, sge_hora_ini, usr_logname, aud_fecha_proceso)"
					+ " values (" + parTipoProceso 
					+ " ," + arpNroProceso 
					+ " ,'"+ rprFechaRad 
					+ "'," + parEstadoActual//parEstadoInserta el del cambio 
					+ ", today"
					+ ", '10:00"
					+ "', user, today)";
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
    
    /*****************************************************************************
	 * Método cambiar estado
	 *****************************************************************************/

	public void cambiarEstado() {
		String lstrQuery = "";
		String[] lstrDatos = new String[8];
		boolean lbooAbrir = true;
		String pasoSiguiente1 = "";
		

		if (parEstadoActu == 404)
			parEstadoAnt = 0;

		lstrQuery = "select distinct grasp_8110.par_estado_sig, A.par_descripcion,grasp_8110.forma"
				+ " from grasp_8110,param_0031 A" + 
				" where grasp_8110.par_tipo_proceso = " + parTipoProceso + 
				" and grasp_8110.par_estado_ant =" + parEstadoAnt
				+ " and grasp_8110.par_estado_actu = " + parEstadoActu
				+ " and grasp_8110.par_estado_sig = A.par_codigo";
		
		if (parTipoProceso == 100) {
			lstrQuery = lstrQuery + " and A.cla_clase = 224";
		} else {
			lstrQuery = lstrQuery + " and A.cla_clase = 72";
		}
		
		DBConneccionSchool db = new DBConneccionSchool();
		Connection conn = null;
		PreparedStatement lps = null;
		ResultSet rs = null;
		try {
			conn = db.getConexionInformix();
			lps = conn.prepareStatement(lstrQuery);
			rs = lps.executeQuery(); 
			estados = new ArrayList<EstadoParam>();
			while (rs.next()) {
				
				EstadoParam state = new EstadoParam();
				state.setCodigo(rs.getInt("par_estado_sig"));
				state.setDesc(rs.getString("par_descripcion"));
				state.setForma(rs.getString("forma"));
				estados.add(state);
				//addMessage("Faild", "No existen mas pasos para el proceso");
				
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally{
			db.cerrarConeccion(conn, lps, rs);
		}

		/*
		 * Req 7191 - Decisiones judiciales De existir un proceso judicicial abierto
		 * solo es posible avanzar a determinados estados de los procesos 9, 105, 106 y
		 * 107
		 *
		String compaTotal = gclaOrigen.controlComparendo.getNumeroComparendoCompleto();
		if (com.datatools.inspeccion.util.Utilidades.existComparendoCompa(gclaOrigen.gconConn,
				com.datatools.admobiblio.Empresa.getInstance().getCodigo(), compaTotal)) {
			Comparendo compa;
			try {
				compa = new Comparendo(compaTotal, false, gclaOrigen.gconConn);

				if (GestorDecisionJudicial.isProcesoAbiertoByCarNroRegistro(gclaOrigen.gconConn,
						compa.getGintCarNroRegistro())) {
					java.sql.Date fechaNotif = GestorDecisionJudicial.getDateNotifByCarNroRegistro(gclaOrigen.gconConn,
							compa.getGintCarNroRegistro());
					int tipoProceso = Integer.parseInt(Validaciones.sacarCodigo(gclaOrigen.gcomTipo));
					String estados = GestorDecisionJudicial.getNextStatusPDJToSql(gclaOrigen.gconConn, tipoProceso);

					if (fechaNotif != null && !estados.equals("")) {
						lstrQuery += "AND grasp_8110.par_estado_sig IN (" + estados + ") ";
					} else {
						gclaOrigen.glabStatusbar.setText("No existen mas pasos para el proceso.");
						return;
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (InfoNotFoundException e) {
				e.printStackTrace();
			}
		}

		/*
		 * Inicio Edicion segun Req 7130 by: Jeison Rodriguez
		 *
		if (Validaciones.sacarCodigo(gclaOrigen.gcomTipo).equals(ConsultasGenerales.PRCESO_SUBSAMACIONES)) {

			int tipoProceso = Integer.parseInt(Validaciones.sacarCodigo(gclaOrigen.gcomTipo));
			int numeroProceso = Integer.parseInt(gclaOrigen.gtexCodigo.getText());
			Date fechaProceso = Fechas.fecha_string_a_fecha_sql(gclaOrigen.gtexFecha.getText());

			PreparedStatement ps;
			ResultSet rs;
			String causal = "";

			String query = "select sge_causal from segex_7058 where " + "par_tipo_proceso = ?"
					+ " and arp_nro_proceso = ? and rpr_fecha_rad = ? ";

			if (gstrEstadoActual.equals(ConsultasGenerales.NO_PRESENTA_RECURSOS)) {

				if (existeEstado(tipoProceso, numeroProceso, fechaProceso,
						Integer.parseInt(ConsultasGenerales.FALLO_ABSOLVER))) {
					pasoSiguiente1 = ConsultasGenerales.PARA_ARCHIVO;
				} else if (existeEstado(tipoProceso, numeroProceso, fechaProceso,
						Integer.parseInt(ConsultasGenerales.FALLO_IMPONE_SANCION))) {
					pasoSiguiente1 = ConsultasGenerales.EN_FIRME_SUBSANACION;
				}
				lstrQuery = lstrQuery + " and A.par_codigo = " + pasoSiguiente1;

				if (com.datatools.admobiblio.FrameGenerico.DEBUG) {
					System.out.println("Query cambiar estado: " + lstrQuery);
				}

			} else if (gstrEstadoActual.equals(ConsultasGenerales.NOTIFICACION_PERSONAL_RECURSO)
					|| gstrEstadoActual.equals(ConsultasGenerales.NOTIFICACION_EDICTO_RECURSO)
					|| gstrEstadoActual.equals(ConsultasGenerales.NOTIFICACION_AVISO_RECURSO)
					|| gstrEstadoActual.equals(ConsultasGenerales.NOTIFICACION_POR_CONDUCTA_CONCLUYENTE_RECURSO)) {

				try {
					query = query + " and par_estado_actu = ? order by sge_fechatran ";
					ps = gclaOrigen.gconConn.prepareStatement(query);
					ps.setInt(1, tipoProceso);
					ps.setInt(2, numeroProceso);
					ps.setDate(3, fechaProceso);
					if (existeEstado(tipoProceso, numeroProceso, fechaProceso,
							Integer.parseInt(ConsultasGenerales.RESUELVE_RECUSO_DE_REPOSICION))) {
						ps.setString(4, ConsultasGenerales.RESUELVE_RECUSO_DE_REPOSICION);
					} else if (existeEstado(tipoProceso, numeroProceso, fechaProceso, Integer
							.parseInt(ConsultasGenerales.RESUELVE_RECUSO_DE_REPOSICION_CON_SUBCIDIO_DE_APELACION))) {
						ps.setString(4, ConsultasGenerales.RESUELVE_RECUSO_DE_REPOSICION_CON_SUBCIDIO_DE_APELACION);
					}

					rs = ps.executeQuery();
					if (rs.next()) {
						causal = rs.getString(1);
					} else {
						return;
					}

					if (causal.equals("2")) {
						pasoSiguiente1 = ConsultasGenerales.ENVIO_FALLO_A_SI_O_SEGUNDO_ENVIO_A_SI;
					} else if (causal.equals("1")) {
						pasoSiguiente1 = ConsultasGenerales.PARA_ARCHIVO;
					}
					if (!pasoSiguiente1.equals("") && pasoSiguiente1 != null) {
						lstrQuery = lstrQuery + " and A.par_codigo in (" + pasoSiguiente1 + ")";
					}

					ps.close();
					rs.close();
					if (com.datatools.admobiblio.FrameGenerico.DEBUG) {
						System.out.println("Query cambiar estado: " + lstrQuery);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}

				// }

			} else if (gstrEstadoActual.equals(ConsultasGenerales.CIERRE)) {
				if (existeEstado(tipoProceso, numeroProceso, fechaProceso,
						Integer.parseInt(ConsultasGenerales.RESUELVE_RECUSO_DE_REPOSICION))) {
					try {
						query = query + " and par_estado_actu = ?";
						ps = gclaOrigen.gconConn.prepareStatement(query);
						ps.setInt(1, tipoProceso);
						ps.setInt(2, numeroProceso);
						ps.setDate(3, fechaProceso);
						ps.setString(4, ConsultasGenerales.RESUELVE_RECUSO_DE_REPOSICION);
						rs = ps.executeQuery();
						if (rs.next()) {
							causal = rs.getString(1);

						} else {

							return;
						}

						if (causal.equals("1")) {
							pasoSiguiente1 = ConsultasGenerales.NO_MOSTRAR_MAS_PASOS;
						} else {
							pasoSiguiente1 = ConsultasGenerales.ENVIO_COACTIVO;
						}
						lstrQuery = lstrQuery + " and A.par_codigo = " + pasoSiguiente1;
						ps.close();
						rs.close();
						if (com.datatools.admobiblio.FrameGenerico.DEBUG) {
							System.out.println("Query cambiar estado: " + lstrQuery);
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}

				} else {
					if (existeEstado(tipoProceso, numeroProceso, fechaProceso,
							Integer.parseInt(ConsultasGenerales.SEGUNDA_INSTANCIA_CONFIRMA_FALLO))
							|| existeEstado(tipoProceso, numeroProceso, fechaProceso,
									Integer.parseInt(ConsultasGenerales.SEGUNDA_INSTANCIA_REVOCA))
							|| existeEstado(tipoProceso, numeroProceso, fechaProceso,
									Integer.parseInt(ConsultasGenerales.SEGUNDA_INSTANCIA_MODIFICA_DECISION))) {
						pasoSiguiente1 = "1";
						lstrQuery = lstrQuery + " and A.par_codigo in (" + pasoSiguiente1 + ")";
						if (com.datatools.admobiblio.FrameGenerico.DEBUG) {
							System.out.println("Query cambiar estado: " + lstrQuery);
						}
					}
				}
			} else if (gstrEstadoActual
					.equals(ConsultasGenerales.RESUELVE_RECUSO_DE_REPOSICION_CON_SUBCIDIO_DE_APELACION)) {
				String fallo = ConsultasGenerales.RESUELVE_RECUSO_DE_REPOSICION_CON_SUBCIDIO_DE_APELACION;
				try {
					query = query + " and par_estado_actu = ?";
					ps = gclaOrigen.gconConn.prepareStatement(query);
					ps.setInt(1, Integer.parseInt(Validaciones.sacarCodigo(gclaOrigen.gcomTipo)));
					ps.setInt(2, gclaOrigen.getNroProceso());
					ps.setString(3, fallo);
					rs = ps.executeQuery();
					if (rs.next()) {
						causal = rs.getString(1);
					} else {
						return;
					}

					if (causal.equals("1") || causal.equals("2")) {
						pasoSiguiente1 = ConsultasGenerales.CITACION_A_NOTIFICACION_RECURSO;
					} else {
						pasoSiguiente1 = "236, 237";
					}
					lstrQuery = lstrQuery + " and A.par_codigo in (" + pasoSiguiente1 + ")";
					ps.close();
					rs.close();
					if (com.datatools.admobiblio.FrameGenerico.DEBUG) {
						System.out.println("Query cambiar estado: " + lstrQuery);
					}
				} catch (SQLException E) {
					E.printStackTrace();
				}

			} else if (gstrEstadoActual.equals(ConsultasGenerales.RESUELVE_RECURSO_DE_APELACION)) {
				String fallo = ConsultasGenerales.RESUELVE_RECURSO_DE_APELACION;
				int a = 0;
				try {
					query = query + " and par_estado_actu = ?";
					ps = gclaOrigen.gconConn.prepareStatement(query);
					ps.setInt(1, tipoProceso);
					ps.setInt(2, numeroProceso);
					ps.setDate(3, fechaProceso);
					ps.setString(4, fallo);
					rs = ps.executeQuery();
					if (rs.next()) {
						causal = rs.getString(1);
					} else {
						return;
					}

					if (causal.equals("2")) {
						pasoSiguiente1 = "236, 237";
					} else {
						pasoSiguiente1 = "" + ConsultasGenerales.ENVIO_FALLO_A_SI_O_SEGUNDO_ENVIO_A_SI + ","
								+ ConsultasGenerales.PARA_ARCHIVO;
					}
					lstrQuery = lstrQuery + " and A.par_codigo in (" + pasoSiguiente1 + ")";
					ps.close();
					rs.close();
					if (com.datatools.admobiblio.FrameGenerico.DEBUG) {
						System.out.println("Query cambiar estado: " + lstrQuery);
					}
				} catch (SQLException e) {

					e.printStackTrace();
				}
			} else if (gstrEstadoActual.equals(ConsultasGenerales.PRESENTA_QUEJA)
					|| gstrEstadoAnt.equals(ConsultasGenerales.NO_PRESENTA_QUEJA)) {

				if (gstrEstadoActual.equals(ConsultasGenerales.PRESENTA_QUEJA)) {
					pasoSiguiente1 = ConsultasGenerales.ENVIO_FALLO_A_SI_O_SEGUNDO_ENVIO_A_SI + ","
							+ ConsultasGenerales.PARA_ARCHIVO;
				} else if (gstrEstadoActual.equals(ConsultasGenerales.NO_PRESENTA_QUEJA)) {
					pasoSiguiente1 = ConsultasGenerales.CITACION_A_NOTIFICACION_RECURSO + ","
							+ ConsultasGenerales.PARA_ARCHIVO;
				}
				lstrQuery = lstrQuery + " and A.par_codigo in (" + pasoSiguiente1 + ")";

				if (com.datatools.admobiblio.FrameGenerico.DEBUG) {
					System.out.println("Query cambiar estado: " + lstrQuery);
				}
			}
		}
		/*
		 * Fin edicion Req 7130
		 */

		/*
		 * 8164 No permitir dar paso de cierre si no hay dejar en firme
		 *

		//lstrQuery += validarDejarEnFirme();

		// 7483 Agregar validacion de perfiles para abogados y autorizador
		//lstrQuery += validaAbogadoAutorizador();
		
		DBConneccionSchool db = new DBConneccionSchool();
		Connection conn = null;
		PreparedStatement lps = null;
		ResultSet rs = null;
		try {
			conn = db.getConexionInformix();
			lps = conn.prepareStatement(lstrQuery);
			rs = lps.executeQuery(); 
			
			if (!rs.next()) {
				addMessage("Faild", "No existen mas pasos para el proceso");
				return;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally{
			db.cerrarConeccion(conn, lps, rs);
		}

		tring[] columnNames = new String[] { "Estado", "Descripcion", "Forma" };
		int[] ancho = { 50, 220, 100 };
		ListaHash forma = new ListaHash(gclaOrigen, lstrQuery, gclaOrigen.gconConn, columnNames, ancho);
		forma.setVisible(true);
		if (forma.getOK()) {
			if (com.datatools.admobiblio.FrameGenerico.DEBUG)
				System.out.println("FORMA.GETOK");
			lstrDatos[0] = forma.getTablaDatos().getValueAt(forma.getFilaSeleccionada(), 0).toString(); // codigo paso
																										// seleccionado
			lstrDatos[1] = forma.getTablaDatos().getValueAt(forma.getFilaSeleccionada(), 1).toString(); // descripcion
																										// paso
																										// seleccionado
			lstrDatos[2] = (forma.getTablaDatos().getValueAt(forma.getFilaSeleccionada(), 2) != null
					? forma.getTablaDatos().getValueAt(forma.getFilaSeleccionada(), 2).toString()
					: ""); // forma seleccionada
			lstrDatos[3] = gclaOrigen.gtexCodigo.getText().trim(); // numero proceso PI
			lstrDatos[4] = gclaOrigen.gtexFecha.getText(); // fecha proceso PI
			lstrDatos[5] = Validaciones.sacarCodigo(gclaOrigen.gcomTipo); // tipo proceso PI
			lstrDatos[6] = gclaOrigen.controlComparendo.getNumeroComparendoCompleto(); // numero comparendo total
			lstrDatos[7] = Validaciones.sacarCodigo(gclaOrigen.gcomGrupo); // codigo de inspeccion PI

			if (Validaciones.sacarCodigo(gclaOrigen.gcomTipo).equalsIgnoreCase("9") && lstrDatos[0].equals("385")) {
				insertarEstadoSegundaInstancia(lstrDatos);
				ejecutarCambioSegundaInstancia(484);
				limpiarTextbox();
				iniciarToolbars();
				return;
			}

			if (lstrDatos[0].equals(PROCESO_SALIDA_PATIOS)) {

				String tipoProceso = Validaciones.sacarCodigo(gclaOrigen.gcomTipo);
				String numeroProceso = gclaOrigen.gtexCodigo.getText().trim();
				String fechaProceso = gclaOrigen.gtexFecha.getText();
				String fechaProcesos = biblio_it.Fechas.arreglar_fecha_sql(fechaProceso);

				String placa = traerPlacaProceso(tipoProceso, numeroProceso, fechaProcesos);

				boolean placaEmbargada = biblio_it.Coactivo.placaEmbargada(placa, gclaOrigen.gconConn);

				if (placaEmbargada && !enviarCorreo(placa)) {
					return;

				}
			}

			if (lstrDatos[0].equals(ConsultasGenerales.CIERRE)
					|| lstrDatos[0].equals(ConsultasGenerales.NO_PRESENTA_QUEJA)) {

				ejecutarCambio(lstrDatos);
				limpiarTextbox();
				iniciarToolbars();
			} else {
				limpiarTextbox();
				iniciarToolbars();
				abrirForma(lstrDatos);
			}
		}*/
	}
	
	public void onRowSelect(SelectEvent<EstadoParam> event) {
        FacesMessage msg = new FacesMessage("Selected", event.getObject().getDesc());
        FacesContext.getCurrentInstance().addMessage(null, msg);
        estado = event.getObject();
        System.out.println(estado.getDesc());
    }
 
    public void onRowUnselect(UnselectEvent<EstadoParam> event) {
        FacesMessage msg = new FacesMessage("Unselected", event.getObject().getDesc());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    public String mostrarPaso() {
    	//estado = (EstadoParam)htmlDataTable.getRowData();
    	
    	if (estado != null)
    		System.out.println(estado.getDesc());
    	else
    		System.out.println("navegue pues");
    	
    	return "estado";
    }
    
    public String mostrarPasoSel() {
    	
    	System.out.println(estado.getDesc());
    	
    	return "entrevista";
    }
	
}

package com.ideario.bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.ideario.vo.ResultadoBusqueda;
import com.ideario.vo.TiposUsuario;
import com.ideario.vo.Usuario;

import adventure.servicios.Cita;
import adventure.servicios.Proceso;
import adventure.servicios.Segex;
import adventure.servicios.vo.EstadoParam;


public class Consultas {
	
		
	public List<TiposUsuario> getTiposUsuario(){
		ArrayList<TiposUsuario> l = new ArrayList();
		
		DBConneccionSchool db = new DBConneccionSchool();
		Connection conn = null;
		PreparedStatement lps = null;
		ResultSet rs = null;
		try {
			conn = db.getConexion();
			
			/*lps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS usuarios " +
					"(idusuario int(11) not null, " +
					"nombreusuario varchar(100), " +
					"mail varchar(100), " +
					"idrol int(100), " +
					"usuario varchar(100), " +
					"clave varchar(100), " +
					"PRIMARY KEY (idusu)) ");
			lps.executeUpdate();
			
			TiposUsuario a = new TiposUsuario();
			a.setIdTipo(1);
			a.setNombreTipo("usuario Se creó");
			l.add(a);*/
			
			
			/*lps = conn.prepareStatement("REPLACE INTO tipos (idTipo, nombreTipo) VALUES "+
					"(1, 'Inventor'),"+
					"(2, 'Empresa'),"+
					"(3, 'Investigador')," +
					"(4, 'Emprendedor')");
			lps.executeUpdate();*/
			
			lps = conn.prepareStatement("select * from tipos " +
    				    				" order by idtipo");
    		rs = lps.executeQuery(); 

    		while(rs.next()){
    			TiposUsuario d = new TiposUsuario();
    			d.setIdTipo(rs.getInt("idTipo"));
    			d.setNombreTipo(rs.getString("nombreTipo"));
    			l.add(d);
    			
    		}
			
			
			
			
		} catch (Exception e) {
			
			TiposUsuario y = new TiposUsuario();
			y.setIdTipo(1);
			y.setNombreTipo("error line "+ e.getStackTrace()[0].toString());
			l.add(y);
			
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			db.cerrarConeccion(conn, lps, rs);
		}		
		
		return l;
	}

		
	
/*	public void guardarEncuentro(Torneo ator ){
		
		DBConneccionSchool db = new DBConneccionSchool();
		Connection conn = null;
		PreparedStatement lps = null;
		ResultSet rs = null;
		
		
		try {
			
			conn = db.getConexion();
    		
    		lps = conn.prepareStatement("INSERT INTO fechatorneo (idTorneo, idFecha, dia, idEquipoLocal, golesLocal, idEquipoVisita, golesVisita, hora, idarbitro, idcancha) "+
    				" VALUES (?, ?, ?, ?, 0, ?, 0, ?, ?, ?)");
    		
    		Encuentro a = ator.getPartido();
    		//System.out.println("hora time "+a.getHoraDate().toString());
			lps.setInt(1,  a.getIdTorneo());
			lps.setInt(2, a.getIdFecha());
			java.sql.Date d = new java.sql.Date(2019).valueOf(a.getDia());
			lps.setDate(3, d);
			lps.setInt(4, a.getIdEquipoLocal());
			lps.setInt(5, a.getIdEquipoVisita());
			lps.setString(6, a.getHora());
			lps.setInt(7, a.getIdArbitro());
			lps.setInt(8,  a.getIdCancha());
			lps.executeUpdate(); 
			

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			db.cerrarConeccion(conn, lps, rs);
		}		
		
		
		
	}
	
	public void EliminarDBEncuentro(Encuentro en){
		DBConneccionSchool db = new DBConneccionSchool();
		Connection conn = null;
		PreparedStatement lps = null;
		ResultSet rs = null;
		
		
		try {
			
			conn = db.getConexion();
    		
    		lps = conn.prepareStatement("DELETE FROM fechatorneo WHERE idpartido = ?");
    		
    		
			lps.setInt(1,  en.getIdPartido());

			lps.executeUpdate(); 
			

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			db.cerrarConeccion(conn, lps, rs);
		}		
		
	}


	*/
	
	

	public ArrayList<ResultadoBusqueda> buscarIdeas(String palabra){
		ArrayList<ResultadoBusqueda> l = new ArrayList();
		DBConneccionSchool db = new DBConneccionSchool();
		Connection conn = null;
		PreparedStatement lps = null;
		ResultSet rs = null;
		try {
			conn = db.getConexion();
			
			/*lps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS ideas " +
					"(ididea int(11) not null, " +
					"cip varchar(100), " +
					"inventor varchar(100), " +
					"titulo varchar(100), " +
					"PRIMARY KEY (ididea)) ");
			lps.executeUpdate();
			
			ResultadoBusqueda a = new ResultadoBusqueda();
			a.setIdidea(1);
			a.setTitulo("tabla idea Se creó");
			l.add(a);
			
			
			lps = conn.prepareStatement("REPLACE INTO ideas (ididea, cip, inventor, titulo) VALUES "+
						"(1, 'B43L19/00', 'JAVIER TORRES PAEZ', 'BORRADOR PORTÁTIL ABSORBENTE CON TAPA PARA SUPERFICIES ACRÍLICAS'),"+
						"(2, 'B43K29/00', 'NAVILLE E. ANDREWS', 'MONTAJE DE ESCRITURA CON MONTAJE BORRADOR'),"+
						"(3, 'B43L19/00', 'LOPEZ SAGUEZ MANUEL','BORRADOR ELÉCTRICO PARA PIZARRAS')");
			lps.executeUpdate();*/
		
			lps = conn.prepareStatement("select * from ideas " +
    				    				" where titulo like '%"+
					palabra.trim()+
					"%'");
    		rs = lps.executeQuery(); 

    		while(rs.next()){
    			ResultadoBusqueda d = new ResultadoBusqueda();
    			d.setIdidea(rs.getInt("ididea"));
    			d.setCip(rs.getString("cip"));
    			d.setInventor(rs.getString("inventor"));
    			d.setTitulo(rs.getString("titulo"));
    			l.add(d);
    			
    		}
			
			
			
			
		} catch (Exception e) {
			
			ResultadoBusqueda y = new ResultadoBusqueda();
			y.setIdidea(1);
			y.setTitulo("error line "+ e.getStackTrace()[0].toString());
			l.add(y);
			
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			db.cerrarConeccion(conn, lps, rs);
		}
		
		return l;
	}
	/***
	 *  "(1, 'Inventor'),"+
	 *	"(2, 'Empresa'),"+
	 *	"(3, 'Investigador')," +
	 *	"(4, 'Emprendedor')"); 
	 * @param usu
	 * @param clave
	 * @return Usuario
	 */
	public Usuario getIngreso(String usu, String clave){
		Usuario u = new Usuario();
		u.setNombres(usu);
		u.setClave(clave);
		//u.setExiste(true);
		
		System.out.println("consultando "+usu);
		
		u.setExiste(false);
		DBConneccionSchool db = new DBConneccionSchool();
		Connection conn = null;
		PreparedStatement lps = null;
		ResultSet rs = null;
		try {
			conn = db.getConexion();
			
			/*lps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS usuarios " +
					"(idusuario int(11) not null AUTO_INCREMENT, " +
					"nombreusuario varchar(100), " +
					"idrol varchar(100), " +
					"mail varchar(100), " +
					"usuario varchar(100), " +
					"clave varchar(100), " +
					"dir varchar(100), " +
					"tel varchar(100), " +
					"nuip varchar(100), " +
					"PRIMARY KEY (idusuario)) ");
			lps.executeUpdate();
			 	*/
			
			
			lps = conn.prepareStatement("select * from adv_usuario " +
    				    				" where usuario = ? "+
										" and clave = ? ");
			lps.setString(1, usu);
			lps.setString(2, clave);
    		rs = lps.executeQuery(); 

    		if(rs.next()){
    			u.setIdusuario(rs.getInt("idusuario"));
    			u.setUsuario(rs.getString("usuario"));
    			u.setClave(rs.getString("clave"));
    			u.setNombres(rs.getString("nombres"));
    			u.setApellidos(rs.getString("apellidos"));
    			u.setPar_tipo_doc(rs.getInt("par_tipo_doc"));
    			u.setPer_nro_doc(rs.getString("per_nro_doc"));
    			u.setDir(rs.getString("dir"));
    			u.setCiudad(rs.getString("ciudad"));
    			u.setEmail(rs.getString("email"));
    			u.setIdrol(rs.getString("idrol"));
    			u.setExiste(true);
    		}
			
			
			
    		System.out.println("pudo consultar "+usu);
		} catch (Exception e) {
			System.out.println("no pudo consultar "+usu);
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			db.cerrarConeccion(conn, lps, rs);
		}
		
		
		
		return u;
	}
	
	public Usuario getUsuario(String perNroDoc){
		Usuario u = new Usuario();
		
		u.setExiste(false);
		DBConneccionSchool db = new DBConneccionSchool();
		Connection conn = null;
		PreparedStatement lps = null;
		ResultSet rs = null;
		try {
			conn = db.getConexion();
			
			lps = conn.prepareStatement("select * from adv_usuario " +
    				    				" where per_nro_doc = ? ");
			lps.setString(1, perNroDoc);
    		rs = lps.executeQuery(); 

    		if(rs.next()){
    			u.setIdusuario(rs.getInt("idusuario"));
    			u.setUsuario(rs.getString("usuario"));
    			u.setClave(rs.getString("clave"));
    			u.setNombres(rs.getString("nombres"));
    			u.setApellidos(rs.getString("apellidos"));
    			u.setPar_tipo_doc(rs.getInt("par_tipo_doc"));
    			u.setPer_nro_doc(rs.getString("per_nro_doc"));
    			u.setDir(rs.getString("dir"));
    			u.setCiudad(rs.getString("ciudad"));
    			u.setEmail(rs.getString("email"));
    			u.setIdrol(rs.getString("idrol"));
    			u.setExiste(true);
    		}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			db.cerrarConeccion(conn, lps, rs);
		}
		return u;
	}
	

	public TiposUsuario getTipo(int idrol){
		TiposUsuario t = new TiposUsuario();
		
		switch(idrol){
		case 1:
			t.setIdTipo(1);
			t.setNombreTipo("Entrevistado");
			break;
		case 2:
			t.setIdTipo(2);
			t.setNombreTipo("Abogado");
			break;
		case 3:
			t.setIdTipo(3);
			t.setNombreTipo("Administrador");
			break;
		case 4:
			t.setIdTipo(4);
			t.setNombreTipo("Emprendedor");
			break;
		}
		
		return t;  
	}
	
	
	public boolean insertarUsuario(Usuario usu){
		boolean ok = false;
		DBConneccionSchool db = new DBConneccionSchool();
		Connection conn = null;
		PreparedStatement lps = null;
		ResultSet rs = null;
		
		
		try {
			
			conn = db.getConexion();
    		
    		lps = conn.prepareStatement("INSERT INTO adv_usuario (" +
					"usuario, " +
					"clave, " +
					"nombres, " +
					"apellidos, " +
					"par_tipo_doc, per_nro_doc, "+
					"dir, ciudad, celular, email, idrol ) "+
    				" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1)");
    		
			lps.setString(1, usu.getUsuario());
			lps.setString(2, usu.getClave());
			lps.setString(3, usu.getNombres());
			lps.setString(4, usu.getApellidos());
			lps.setInt(5, usu.getPar_tipo_doc());
			lps.setString(6, usu.getPer_nro_doc());
			lps.setString(7, usu.getDir());
			lps.setString(8, usu.getCiudad());
			lps.setString(9, usu.getCelular());
			lps.setString(10, usu.getEmail());
			
			lps.executeUpdate(); 
			
			ok = true;
		} catch (Exception e) {
			ok = false;
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			db.cerrarConeccion(conn, lps, rs);
		}
		
		return ok;
	}
	
	
	public List<Cita> getCitasAbogado(String perNroDoc){
		ArrayList<Cita> l = new ArrayList<Cita>();
		
		DBConneccionSchool db = new DBConneccionSchool();
		Connection conn = null;
		PreparedStatement lps = null;
		ResultSet rs = null;
		try {
			//conn = db.getConexion();
			conn = db.getConexionInformix();
			lps = conn.prepareStatement("select * from cita_0001 " +
										" where cit_funcionario = "+perNroDoc.trim()+
										//" and cit_dia > (today-)"
    				    				" order by cit_dia");
    		rs = lps.executeQuery(); 

    		while(rs.next()){
    			Cita c = new Cita();
    			c.setIdCita(rs.getLong("idCita"));
    			c.setDiaCita(rs.getString("cit_dia"));
    			c.setHoraIni(rs.getString("cit_hora_ini"));
    			c.setHoraFin(rs.getString("cit_hora_Fin"));
    			c.setFuncionario(rs.getString("cit_funcionario"));
    			c.setEntrevistado(rs.getString("cit_entrevistado"));
    			c.setRoom(rs.getString("cit_room"));
    			c.setParTipoProceso(rs.getInt("par_tipo_proceso"));
    			c.setArpNroProceso(rs.getInt("arp_nro_proceso"));
    			c.setRprFechaRad(rs.getString("rpr_fecha_rad"));
    			c.setParEstadoActu(rs.getInt("par_estado_actu"));
    			c.setCitEstadoCita(rs.getString("cit_estado_cita"));
    			
    			l.add(c);
    			
    		}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			db.cerrarConeccion(conn, lps, rs);
		}		
		
		return l;
	}
	
	public List<Cita> getCitasEntrevistado(String perNroDoc){
		ArrayList<Cita> l = new ArrayList<Cita>();
		
		DBConneccionSchool db = new DBConneccionSchool();
		Connection conn = null;
		PreparedStatement lps = null;
		ResultSet rs = null;
		try {
			//conn = db.getConexion();
			conn = db.getConexionInformix();
			lps = conn.prepareStatement("select * from cita_0001 " +
										" where cit_entrevistado = "+perNroDoc.trim()+
										//" and cit_dia > (today-)"
    				    				" order by cit_dia");
    		rs = lps.executeQuery(); 

    		while(rs.next()){
    			Cita c = new Cita();
    			c.setIdCita(rs.getLong("idCita"));
    			c.setDiaCita(rs.getString("cit_dia"));
    			c.setHoraIni(rs.getString("cit_hora_ini"));
    			c.setHoraFin(rs.getString("cit_hora_Fin"));
    			c.setFuncionario(rs.getString("cit_funcionario"));
    			c.setEntrevistado(rs.getString("cit_entrevistado"));
    			c.setRoom(rs.getString("cit_room"));
    			c.setParTipoProceso(rs.getInt("par_tipo_proceso"));
    			c.setArpNroProceso(rs.getInt("arp_nro_proceso"));
    			c.setRprFechaRad(rs.getString("rpr_fecha_rad"));
    			c.setParEstadoActu(rs.getInt("par_estado_actu"));
    			c.setCitEstadoCita(rs.getString("cit_estado_cita"));
    			
    			l.add(c);
    			
    		}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			db.cerrarConeccion(conn, lps, rs);
		}		
		
		return l;
	}
	
	
	public String getNombreParam(int cla_clase, int par_codigo) {
		String n="";
		DBConneccionSchool db = new DBConneccionSchool();
		Connection conn = null;
		PreparedStatement lps = null;
		ResultSet rs = null;
		try {
			conn = db.getConexion();
			
			lps = conn.prepareStatement("select * from param_0031 " +
										" where cla_clase = "+cla_clase+
										" and par_codigo = "+ par_codigo);
    		rs = lps.executeQuery(); 

    		if(rs.next()){
    			n = rs.getString("par_descripcion");
    		}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			db.cerrarConeccion(conn, lps, rs);
		}
		
		return n;
	}
	
	
	public Proceso getProceso(int parTipoProceso, int arpNroProceso, String rprFechaRad){
		Proceso p = new Proceso();
		
		p.setParTipoProceso(parTipoProceso);
		p.setArpNroProceso(arpNroProceso);
		p.setNombreProceso(getNombreParam(120, parTipoProceso));
		p.setRprFechaRad(rprFechaRad);
		
		p.setSeges(getSeguimiento(parTipoProceso, arpNroProceso, rprFechaRad));
		p.setComNumero(getComNumero(parTipoProceso, arpNroProceso, rprFechaRad));
		
		
		
		for (Segex s: p.getSeges()) {
			p.setParEstadoAnt(p.getParEstadoActu());
			//p.parEstadoAnt = p.parEstadoActu; 
			p.setParEstadoActu(s.getParEstadoActu());
			//p.parEstadoActu = s.getParEstadoActu();
			if (s.getParEstadoActu() == 147) {
				p.setEnFirme(true);
			}else if (s.getParEstadoActu() == 3) {
				p.setSalidaAutorizada(true);
			}
		}
		//cambiarEstado();
		
		
		return p;
	}
	
	public List<Segex> getSeguimiento(int parTipoProceso, int arpNroProceso, String rprFechaRad){
		ArrayList<Segex> l = new ArrayList();
		
		DBConneccionSchool db = new DBConneccionSchool();
		Connection conn = null;
		PreparedStatement lps = null;
		ResultSet rs = null;
		try {
			//conn = db.getConexion();
			conn = db.getConexionInformix();
			
						
			String str = "select * "
					+ "from segex_7058 s, param_0031 p where " 
					+ "s.par_tipo_proceso = "+parTipoProceso+" and "
					+ "s.arp_nro_proceso = "+arpNroProceso+ " and "
					+ "s.rpr_fecha_rad ='"+rprFechaRad+"' and "
					+ "s.par_estado_actu = p.par_codigo"+ " and "
					+ "p.cla_clase=72 ";
					//+ "order by sge_fecha_ini";
					lps = conn.prepareStatement(str);
					rs = lps.executeQuery(); 

    		while(rs.next()){
    			Segex d = new Segex();
    			d.setFecFin(rs.getString("sge_fecha_fin"));
    			d.setFecIni(rs.getString("sge_fecha_ini"));
    			d.setNombreEstado(rs.getString("par_descripcion").trim());
    			d.setParEstadoActu(rs.getInt("par_estado_actu"));
    			l.add(d);
    			 
    		}
			
		} catch (Exception e) {
			
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			db.cerrarConeccion(conn, lps, rs);
		}		
		
		return l;
	}
	
	public String getComNumero(int parTipoProceso, int arpNroProceso, String rprFechaRad){
		String com = "";
		DBConneccionSchool db = new DBConneccionSchool();
		Connection conn = null;
		PreparedStatement lps = null;
		ResultSet rs = null;
		try {
			//conn = db.getConexion();
			conn = db.getConexionInformix();
			
						
			String str = "select * "
					+ "from radpr_8112 s where " 
					+ "s.par_tipo_proceso = "+parTipoProceso+" and "
					+ "s.arp_nro_proceso = "+arpNroProceso+ " and "
					+ "s.rpr_fecha_rad ='"+rprFechaRad+"'";
					lps = conn.prepareStatement(str);
					rs = lps.executeQuery(); 

    		while(rs.next()){
    			com = rs.getString("com_numero_total");
    		}
			
		} catch (Exception e) {
			
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			db.cerrarConeccion(conn, lps, rs);
		}
		return com;
	}

	public List<EstadoParam> cambiarEstado(Proceso p) {
		int parTipoProceso = p.getParTipoProceso(); 
		int parEstadoAnt = p.getParEstadoAnt();
		int parEstadoActu = p.getParEstadoActu();
		String lstrQuery = "";
		String[] lstrDatos = new String[8];
		boolean lbooAbrir = true;
		String pasoSiguiente1 = "";
		
		List<EstadoParam> estados = new ArrayList<EstadoParam>();

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
		
		return estados;
		}
	
}

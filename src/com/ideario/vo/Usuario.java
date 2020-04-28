package com.ideario.vo;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.ideario.bean.Consultas;

import adventure.servicios.UserWizard;

public class Usuario {
	
	private int idusuario;
	private String usuario;
	private String clave;
	private String nombres;
	private String apellidos;
	private int par_tipo_doc;
	private String per_nro_doc;
	private String dir;
	private String ciudad;
	private String email;
	private String celular;
	private String idrol;
	private boolean existe;
	private UserWizard userWizard = new UserWizard();
	private boolean entrevistador=false;
	private boolean entrevistado=false;
	
	private boolean informix=false;
	
	
	public boolean isInformix() {
		return informix;
	}
	public void setInformix(boolean informix) {
		this.informix = informix;
	}
	public boolean isEntrevistador() {
		return entrevistador;
	}
	public void setEntrevistador(boolean entrevistador) {
		this.entrevistador = entrevistador;
	}
	public boolean isEntrevistado() {
		return entrevistado;
	}
	public void setEntrevistado(boolean entrevistado) {
		this.entrevistado = entrevistado;
	}
	public String getDir() {
		return dir;
	}
	public void setDir(String dir) {
		this.dir = dir;
	}
	
	public int getIdusuario() {
		return idusuario;
	}
	public void setIdusuario(int idusuario) {
		this.idusuario = idusuario;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public boolean isExiste() {
		return existe;
	}
	public void setExiste(boolean existe) {
		this.existe = existe;
	}
	public String getNombres() {
		return nombres;
	}
	public void setNombres(String nombres) {
		this.nombres = nombres;
	}
	public String getApellidos() {
		return apellidos;
	}
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	public int getPar_tipo_doc() {
		return par_tipo_doc;
	}
	public void setPar_tipo_doc(int par_tipo_doc) {
		this.par_tipo_doc = par_tipo_doc;
	}
	public String getPer_nro_doc() {
		return per_nro_doc;
	}
	public void setPer_nro_doc(String per_nro_doc) {
		this.per_nro_doc = per_nro_doc;
	}
	public String getCiudad() {
		return ciudad;
	}
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCelular() {
		return celular;
	}
	public void setCelular(String celular) {
		this.celular = celular;
	}
	public String getIdrol() {
		return idrol;
	}
	public void setIdrol(String idrol) {
		this.idrol = idrol;
	}
	
	public UserWizard getUserWizard() {
		return userWizard;
	}
	public void setUserWizard(UserWizard userWizard) {
		this.userWizard = userWizard;
	}
	public void guardar() {
		Consultas c = new Consultas();
		boolean ok = false; 
		ok = c.insertarUsuario(this); 
		if (ok){
			FacesMessage msg = new FacesMessage("Successful", "Welcome :" + getNombres());
	        FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		else{
			FacesMessage msg = new FacesMessage("Fallo", "Umm? algo está mal? :" + getNombres());
	        FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		
		
		
	}
}

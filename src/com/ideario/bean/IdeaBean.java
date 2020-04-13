package com.ideario.bean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.html.HtmlDataTable;

import com.ideario.vo.ResultadoBusqueda;
import com.ideario.vo.TiposUsuario;
import com.ideario.vo.Usuario;


public class IdeaBean {
	
	private String usuario;
	private String clave;
	private String nombre;
	private String correo;
	private String mensaje;
	private String tipoUsuario;
	private Busqueda search;
	private ArrayList<ResultadoBusqueda> ideaList;
	private List<TiposUsuario> tiposUsuario;
	private Usuario usu;
	private String error;
	private ResultadoBusqueda selectedIdea;
	private HtmlDataTable ideahtmlDataTable;
	
	
	public HtmlDataTable getIdeahtmlDataTable() {
		return ideahtmlDataTable;
	}
	public void setIdeahtmlDataTable(HtmlDataTable ideahtmlDataTable) {
		this.ideahtmlDataTable = ideahtmlDataTable;
	}
	public ResultadoBusqueda getSelectedIdea() {
		return selectedIdea;
	}
	public void setSelectedIdea(ResultadoBusqueda selectedIdea) {
		this.selectedIdea = selectedIdea;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public Usuario getUsu() {
		return usu;
	}
	public void setUsu(Usuario usu) {
		this.usu = usu;
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
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public List<TiposUsuario> getTiposUsuario() {
		return tiposUsuario;
	}
	public void setTiposUsuario(List<TiposUsuario> tiposUsuario) {
		this.tiposUsuario = tiposUsuario;
	}
	public String getTipoUsuario() {
		return tipoUsuario;
	}
	public void setTipoUsuario(String tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}
	public Busqueda getSearch() {
		return search;
	}
	public void setSearch(Busqueda search) {
		this.search = search;
	}
	public ArrayList<ResultadoBusqueda> getIdeaList() {
		return ideaList;
	}
	public void setIdeaList(ArrayList<ResultadoBusqueda> ideaList) {
		this.ideaList = ideaList;
	}

	/*
	 * los procesos
	 * */
	public String ingresar() {
		error = "";
		Consultas c = new Consultas();
		usu = c.getIngreso(usuario, clave); 
		if (usu.isExiste()){
			return "productos";
			}
		else{
			error = "Usuario o Clave Errado";
			return "ideario";
			}
	}
	
	public String regresarProd(){
		
		return "productos";
	}
	
	public String registrar() {
		usu = new Usuario(); 
		//usu.setTipo(new TiposUsuario());
		Consultas c = new Consultas();
		tiposUsuario = c.getTiposUsuario();
		
		
		
		return "registro";
	}
	
	public String saveRegistrar() {
		Consultas c = new Consultas();
		c.insertarUsuario(usu);
		
		
		
		return "ideario";
	}
	
	public String contactar() {
		
		return "contacto";
	}
	
	public String enviar() {
		
		return "gracias";
	}
	
	public String iniciar() {
		
		return "ideario";
	}
	
	public String consultar() {
		ideaList = new ArrayList<ResultadoBusqueda>();
		search = new Busqueda();
		search.setPalabra("");
		
		return "consulta";
	}
	
	public String buscarIdea() {
		Consultas c = new Consultas();
		ideaList = c.buscarIdeas(search.getPalabra());
		return "consulta";
	}
	
	public String buscarVigila() {
		Consultas c = new Consultas();
		ideaList = c.buscarIdeas(search.getPalabra());
		return "vigila";
	}
	
	public String vigilar(){
		ideaList = new ArrayList<ResultadoBusqueda>();
		search = new Busqueda();
		search.setPalabra("");
		return "vigila";
	}
	
	public String incidentarOld(){
		
		return "";
	}
	
	public String incidentar(){
		if (getIdeahtmlDataTable() == null ){
			return "";
		}
		else{
		
			this.selectedIdea = (ResultadoBusqueda)getIdeahtmlDataTable().getRowData();
			
			System.out.println("showchampion "+selectedIdea.getTitulo());
		}
		
		return "showchampion";
	}
	
	public String incidentarPrime(){
		
		//this.selectedIdea = new Torneo(this.torneo.getIdTorneo());
		System.out.println("showchampion "+selectedIdea.getTitulo());
	
	return "showchampion";
	}
}

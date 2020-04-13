package com.ideario.bean;

import java.util.ArrayList;

import com.ideario.vo.ResultadoBusqueda;

public class Busqueda {
	private String palabra;
	private ArrayList<ResultadoBusqueda> result;
	public String getPalabra() {
		return palabra;
	}
	public void setPalabra(String palabra) {
		this.palabra = palabra;
	}
	public ArrayList<ResultadoBusqueda> getResult() {
		return result;
	}
	public void setResult(ArrayList<ResultadoBusqueda> result) {
		this.result = result;
	}
	
	

}

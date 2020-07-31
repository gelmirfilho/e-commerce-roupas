package br.com.roupas.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import br.com.roupas.dao.RoupaDAO;
import br.com.roupas.model.Roupa;

@Named
@ViewScoped
public class HomeController implements Serializable {

	private static final long serialVersionUID = 3729594592505505678L;

	private int tipoFiltro = 1;
	private String filtro;
	private RoupaDAO dao = new RoupaDAO();
	private List<Roupa> listaRoupa = dao.findByDescricao("");
	
	public void pesquisar() {
		RoupaDAO dao = new RoupaDAO();
		if (tipoFiltro == 1)
			listaRoupa = dao.findByDescricao(getFiltro());
		else 
			listaRoupa = dao.findByTamanho(getFiltro());
	}
		
	public List<Roupa> getListaLivro() {
		if (listaRoupa == null) {
			listaRoupa = new ArrayList<Roupa>();
		}
		return listaRoupa;
	}

	public String getFiltro() {
		return filtro;
	}

	public void setFiltro(String filtro) {
		this.filtro = filtro;
	}

	public int getTipoFiltro() {
		return tipoFiltro;
	}

	public void setTipoFiltro(int tipoFiltro) {
		this.tipoFiltro = tipoFiltro;
	}

	public List<Roupa> getListaRoupa() {
		return listaRoupa;
	}

	public void setListaRoupa(List<Roupa> listaRoupa) {
		this.listaRoupa = listaRoupa;
	}
	
}

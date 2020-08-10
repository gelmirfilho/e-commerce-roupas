package br.com.roupas.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import br.com.roupas.dao.RoupaDAO;
import br.com.roupas.model.Roupa;

@Named
@ViewScoped
public class ConsultaRoupaController implements Serializable {

	private static final long serialVersionUID = -2961941624727415020L;

	private int tipoFiltro = 1;
	private String filtro;
	private List<Roupa> listaRoupa;
	
	public void pesquisar() {
		RoupaDAO dao = new RoupaDAO();
		if (tipoFiltro == 1)
			listaRoupa = dao.findByDescricao(getFiltro());
		else 
			listaRoupa = dao.findByTamanho(getFiltro());
	}
	
	public String novaRoupa() {
		return "roupa.xhtml?faces-redirect=true";
	}
	
	public String editar(Roupa roupa) {
		RoupaDAO dao = new RoupaDAO();
		roupa = dao.findById(roupa.getId());
		
		Flash flash = FacesContext.getCurrentInstance().
						getExternalContext().getFlash();
		
		flash.put("flashRoupa", roupa);
		return "roupa.xhtml?faces-redirect=true";
	}

	public List<Roupa> getListaRoupa() {
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
}

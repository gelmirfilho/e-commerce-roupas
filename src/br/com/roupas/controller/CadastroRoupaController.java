package br.com.roupas.controller;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import br.com.roupas.application.Util;
import br.com.roupas.dao.RoupaDAO;
import br.com.roupas.model.Roupa;

@Named
@ViewScoped
public class CadastroRoupaController extends Controller<Roupa>  implements Serializable {

	private static final long serialVersionUID = 9206801041506192806L;

	public CadastroRoupaController() {
		super(new RoupaDAO());
		Flash flash = FacesContext.getCurrentInstance().
				getExternalContext().getFlash();
		flash.keep("flashRoupa");
		entity = (Roupa) flash.get("flashRoupa");
	}
	
	@Override
	public Roupa getEntity() {
		if (entity == null)
			entity = new Roupa();
		return entity;
	}
	
	@Override
	public void alterar(){
		if (validarDados()) {
			if (dao.update(getEntity())) {
				limpar();
				Util.addInfoMessage("Alteração realizada com sucesso.");
			} else {
				Util.addInfoMessage("Erro ao alterar no banco de dados.");
			}
		}
	}
		
	public void reload() throws IOException {
	    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	    ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
	}
}

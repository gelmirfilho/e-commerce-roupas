package br.com.roupas.controller;

import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import br.com.roupas.dao.RoupaDAO;
import br.com.roupas.model.Roupa;

@Named
@ViewScoped
public class CadastroRoupaController extends Controller<Roupa> {

	private static final long serialVersionUID = 333428363094667187L;

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
}

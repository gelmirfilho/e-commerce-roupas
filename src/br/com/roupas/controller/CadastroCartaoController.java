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
import br.com.roupas.dao.CartaoDAO;
import br.com.roupas.model.Cartao;

@Named
@ViewScoped
public class CadastroCartaoController extends Controller<Cartao>  implements Serializable {

	private static final long serialVersionUID = -1851780184787736894L;

	public CadastroCartaoController() {
		super(new CartaoDAO());
		Flash flash = FacesContext.getCurrentInstance().
				getExternalContext().getFlash();
		flash.keep("flashCartao");
		entity = (Cartao) flash.get("flashCartao");
	}
	
	@Override
	public Cartao getEntity() {
		if (entity == null)
			entity = new Cartao();
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

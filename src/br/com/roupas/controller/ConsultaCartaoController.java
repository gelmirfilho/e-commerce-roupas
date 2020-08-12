package br.com.roupas.controller;

import java.io.Serializable;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import br.com.roupas.application.Session;
import br.com.roupas.dao.CartaoDAO;
import br.com.roupas.model.Cartao;
import br.com.roupas.model.Usuario;

@Named
@ViewScoped
public class ConsultaCartaoController implements Serializable {

	private static final long serialVersionUID = -2590628192704264800L;

	private Cartao cartao = null;

	public String novoCartao() {
		return "cartao.xhtml?faces-redirect=true";
	}

	public String editar(Cartao cartao) {
		//CartaoDAO dao = new CartaoDAO();
		//cartao = dao.findById(cartao.getId());

		Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();

		flash.put("flashCartao", cartao);
		return "cartao.xhtml?faces-redirect=true";
	}

	public Cartao getCartao() {
		if (cartao == null) {
			CartaoDAO dao = new CartaoDAO();
			Usuario usuario = (Usuario) Session.getInstance().getAttribute("usuarioLogado");
			cartao = dao.findByUsuario(usuario.getId());
			//if (cartao == null)
			//	cartao = new Cartao();
		}
		return cartao;
	}

	public void setCartao(Cartao cartao) {
		this.cartao = cartao;
	}

}

package br.com.roupas.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

	private int tipoFiltro = 1;
	private String filtro;
	private List<Cartao> listaCartao = null;

	public String novoCartao() {
		return "cartao.xhtml?faces-redirect=true";
	}

	public String editar(Cartao cartao) {
		CartaoDAO dao = new CartaoDAO();
		cartao = dao.findById(cartao.getId());

		Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();

		flash.put("flashCartao", cartao);
		return "cartao.xhtml?faces-redirect=true";
	}

	public List<Cartao> getListaCartao() {
		if (listaCartao == null) {
			CartaoDAO dao = new CartaoDAO();
			Usuario usuario = (Usuario) Session.getInstance().getAttribute("usuarioLogado");
			listaCartao = dao.findByUsuario(usuario.getId());
			if (listaCartao == null)
				listaCartao = new ArrayList<Cartao>();
		}
		return listaCartao;
	}

	public void setListaCartao(List<Cartao> listaCartao) {
		this.listaCartao = listaCartao;
	}

	public int getTipoFiltro() {
		return tipoFiltro;
	}

	public void setTipoFiltro(int tipoFiltro) {
		this.tipoFiltro = tipoFiltro;
	}

	public String getFiltro() {
		return filtro;
	}

	public void setFiltro(String filtro) {
		this.filtro = filtro;
	}

}

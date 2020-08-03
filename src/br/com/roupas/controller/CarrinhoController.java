package br.com.roupas.controller;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import br.com.roupas.application.Session;
import br.com.roupas.application.Util;
import br.com.roupas.dao.VendaDAO;
import br.com.roupas.model.ItemVenda;
import br.com.roupas.model.Usuario;
import br.com.roupas.model.Venda;

@Named
@ViewScoped
public class CarrinhoController implements Serializable{

	private static final long serialVersionUID = 8986784088497060178L;

	private Venda venda;

	public Venda getVenda() {
		if (venda == null) 
			venda = new Venda();
		
		// obtendo o carrinho da sessao
		List<ItemVenda> carrinho = 
				(ArrayList<ItemVenda>)Session.getInstance().getAttribute("carrinho");
		
		// adicionando os itens do carrinho na venda
		if (carrinho == null)
			carrinho = new ArrayList<ItemVenda>();
		venda.setListaItemVenda(carrinho);
		
		return venda;
	}
	
	public void remover(int idProduto) {
		// alunos vao implementar
	}
	
	public void finalizar() throws IOException {
		Usuario usuario = (Usuario)Session.getInstance().getAttribute("usuarioLogado");
		if (usuario == null) {
			Util.addWarningMessage("Eh preciso estar logado para realizar uma venda. Faca o Login!!");
			return;
		}
		// montar a venda
		Venda venda = new Venda();
		venda.setData(LocalDate.now());
		venda.setUsuario(usuario);
		List<ItemVenda> carrinho = (ArrayList<ItemVenda>)  Session.getInstance().getAttribute("carrinho");
		venda.setListaItemVenda(carrinho);
		// salvar no banco
		VendaDAO dao = new VendaDAO();
		if (dao.create(venda)) {
			Util.addInfoMessage("Venda realizada com sucesso.");
			// limpando o carrinho
			Session.getInstance().setAttribute("carrinho", null);
		} else {
			Util.addErrorMessage("Erro ao finalizar a Venda.");
		}
		reload();
	}

	public void setVenda(Venda venda) {
		
		this.venda = venda;
	}
	
	public void reload() throws IOException {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
	}
}

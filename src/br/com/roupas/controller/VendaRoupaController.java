package br.com.roupas.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import br.com.roupas.application.Session;
import br.com.roupas.application.Util;
import br.com.roupas.dao.RoupaDAO;
import br.com.roupas.model.ItemVenda;
import br.com.roupas.model.Roupa;

@Named
@ViewScoped
public class VendaRoupaController implements Serializable {

	private static final long serialVersionUID = -737334689228845174L;

	private String descricao;
	private List<Roupa> listaRoupa = null;
	
	public void pesquisar() {
		listaRoupa = null;
	}
	
	public void adicionar(int idLivro) {
		RoupaDAO dao = new RoupaDAO();
		Roupa Livro = dao.findById(idLivro);
		// verifica se existe um carrinho na sessao
		if (Session.getInstance().getAttribute("carrinho") == null) {
			// adiciona um carrinho (de itens de venda) na sessao
			Session.getInstance().setAttribute("carrinho", 
					new ArrayList<ItemVenda>());
		}
		
		// obtendo o carrinho da sessao
		List<ItemVenda> carrinho = 
				(ArrayList<ItemVenda>) Session.getInstance().getAttribute("carrinho");
		
		// criando um item de venda para adicionar no carrinho
		ItemVenda item = new ItemVenda();
		item.setRoupa(Livro);
		item.setValor(Livro.getPreco());
		
		// adicionando o item no carrinho (variavel local) 
		carrinho.add(item);
		
		// atualizando o carrinho na sessao
		Session.getInstance().setAttribute("carrinho", carrinho);
		
		Util.addInfoMessage("Roupa adicionada no carrinho. "
				+ "Quantidade de Itens: " + carrinho.size());
		
	}

	public List<Roupa> getListaRoupa() {
		if (listaRoupa == null) {
			RoupaDAO dao = new RoupaDAO();
			listaRoupa = dao.findByDescricao(getDescricao());
			if (listaRoupa == null)
				listaRoupa = new ArrayList<Roupa>();
		}
		return listaRoupa;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}

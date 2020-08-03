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

	public void adicionar(int idLivro) {
		RoupaDAO dao = new RoupaDAO();
		Roupa Livro = dao.findById(idLivro);
		// verifica se existe um carrinho na sessao
		if (Session.getInstance().getAttribute("carrinho") == null) {
			// adiciona um carrinho (de itens de venda) na sessao
			Session.getInstance().setAttribute("carrinho", new ArrayList<ItemVenda>());
		}

		// obtendo o carrinho da sessao
		List<ItemVenda> carrinho = (ArrayList<ItemVenda>) Session.getInstance().getAttribute("carrinho");

		// criando um item de venda para adicionar no carrinho
		ItemVenda item = new ItemVenda();
		item.setRoupa(Livro);
		item.setValor(Livro.getPreco());

		// adicionando o item no carrinho (variavel local)
		carrinho.add(item);

		// atualizando o carrinho na sessao
		Session.getInstance().setAttribute("carrinho", carrinho);

		Util.addInfoMessage("Roupa adicionada no carrinho. " + "Quantidade de Itens: " + carrinho.size());

	}

	public int tamanhoCarrinho() {
		// verifica se existe um carrinho na sessao
		if (Session.getInstance().getAttribute("carrinho") == null) {
			// adiciona um carrinho (de itens de venda) na sessao
			Session.getInstance().setAttribute("carrinho", new ArrayList<ItemVenda>());
		}

		// obtendo o carrinho da sessao
		List<ItemVenda> carrinho = (ArrayList<ItemVenda>) Session.getInstance().getAttribute("carrinho");
		
		return carrinho.size();
	}

}

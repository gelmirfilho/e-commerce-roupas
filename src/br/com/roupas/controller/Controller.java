package br.com.roupas.controller;

import java.io.IOException;
import java.io.Serializable;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import br.com.roupas.application.Util;
import br.com.roupas.dao.DAO;
import br.com.roupas.model.Entity;

public abstract class Controller<T extends Entity<T>> implements Serializable {

	private static final long serialVersionUID = -2022582742025295921L;

	protected T entity = null;
	protected DAO<T> dao = null;

	public Controller(DAO<T> dao) {
		super();
		this.dao = dao;
	}

	public abstract T getEntity();

	public void setEntity(T entity) {
		this.entity = entity;
	}

	public void incluir() throws IOException {
		if (validarDados()) {
			if (dao.create(getEntity())) {
				limpar();
				reload();
				Util.addInfoMessage("Inclusão realizada com sucesso.");
			} else {
				Util.addInfoMessage("Erro ao incluir no banco de dados.");
			}
		}
	}

	public void alterar() throws IOException {
		if (validarDados()) {
			if (dao.update(getEntity())) {
				limpar();
				reload();
				Util.addInfoMessage("Alteração realizada com sucesso.");
			} else {
				Util.addInfoMessage("Erro ao alterar no banco de dados.");
			}
		}
	}

	public void remover() throws IOException {
		if (dao.delete(getEntity().getId())) {
			limpar();
			reload();
			Util.addInfoMessage("Remoção realizada com sucesso.");
		} else {
			Util.addInfoMessage("Erro ao remover no banco de dados.");
		}
	}

	public void editar(T entity) {
		entity = dao.findById(entity.getId());
		setEntity(entity);
	}

	public boolean validarDados() {
		return true;
	}

	public void limpar() {
		entity = null;
	}

	public void reload() throws IOException {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
	}

}
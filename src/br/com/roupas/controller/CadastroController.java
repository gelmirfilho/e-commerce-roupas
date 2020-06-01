package br.com.roupas.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import br.com.roupas.application.Util;
import br.com.roupas.dao.UsuarioDAO;
import br.com.roupas.model.TipoUsuario;
import br.com.roupas.model.Usuario;

@Named
@ViewScoped
public class CadastroController extends Controller<Usuario> implements Serializable {

	private static final long serialVersionUID = -563227102506849534L;

	private List<Usuario> listaUsuario;
	
	public CadastroController() {
		super(new UsuarioDAO());
	}
	
	@Override
	public Usuario getEntity() {
		if (entity == null)
			entity = new Usuario();
		return entity;
	}
	
	@Override
	public void limpar() {
		super.limpar();
		listaUsuario = null;
	}

	public List<Usuario> getListaUsuario() {
		if (listaUsuario == null) {
			UsuarioDAO dao = new UsuarioDAO();
			listaUsuario = dao.findAll();
		}
		return listaUsuario;
	}

	
	@Override
	public boolean validarDados() {
		if (getEntity().getNome().isBlank()) {
			Util.addErrorMessage("O campo nome deve ser informado.");
			return false;
		}
		
		// gerando o hash da senha
		String senha = Util.hashSHA256(getEntity().getSenha());
		getEntity().setSenha(senha);
		
		return true;
	}

	public TipoUsuario[] getListaTipoUsuario() {
		return TipoUsuario.values();
	}

}

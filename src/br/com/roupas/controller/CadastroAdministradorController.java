package br.com.roupas.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import br.com.roupas.application.Util;
import br.com.roupas.dao.UsuarioDAO;
import br.com.roupas.model.TipoUsuario;
import br.com.roupas.model.Usuario;

@Named
@ViewScoped
public class CadastroAdministradorController extends Controller<Usuario> implements Serializable {

	private static final long serialVersionUID = -563227102506849534L;

	private List<Usuario> listaUsuario;
	
	public CadastroAdministradorController() {
		super(new UsuarioDAO());
		Flash flash = FacesContext.getCurrentInstance().
				getExternalContext().getFlash();
		flash.keep("flashRoupa");
		entity = (Usuario) flash.get("flashUsuario");
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
	
	public void alterar(){
		if (validarDados()) {
			if (dao.update(getEntity())) {
				limpar();
				Util.addInfoMessage("Alteração realizada com sucesso.");
				try {
					reload();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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

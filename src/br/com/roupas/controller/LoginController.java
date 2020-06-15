package br.com.roupas.controller;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import br.com.roupas.application.Session;
import br.com.roupas.application.Util;
import br.com.roupas.dao.UsuarioDAO;
import br.com.roupas.model.Usuario;

@Named
@RequestScoped
public class LoginController {

	private Usuario usuario;
	
	public String logar() {
		UsuarioDAO dao = new UsuarioDAO();
		Usuario usuario = dao.verificarLoginSenha(getUsuario().getEmail(),
				Util.hashSHA256(getUsuario().getSenha()));
		
		if (usuario != null) {
			// adicionando um ussuario na sessao
			Session.getInstance().setAttribute("usuarioLogado", usuario);
			// redirecionando para o template
			return "home.xhtml?faces-redirect=true";
		}
		Util.addErrorMessage("Login ou Senha inválido.");
		return "";
	}
	
	public void limpar() {
		usuario = null;
	}

	public Usuario getUsuario() {
		if (usuario == null)
			usuario = new Usuario();
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}

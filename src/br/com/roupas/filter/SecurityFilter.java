package br.com.roupas.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.com.roupas.model.TipoUsuario;
import br.com.roupas.model.Usuario;

@WebFilter(filterName = "SecurityFilter", urlPatterns = { "/faces/*" })
public class SecurityFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

// 	 	Para desabilitar o filter, descomente as duas proximas linhas e comente o restante		
//		 chain.doFilter(request, response);
//		 return;

		HttpServletRequest servletRequest = (HttpServletRequest) request;
		// imprime o endereco da pagina
		String endereco = servletRequest.getRequestURI();
		System.out.println(endereco);
		
		if (endereco.equals("/roupas/faces/login.xhtml") || endereco.equals("/roupas/faces/cadastroCliente.xhtml")
				|| endereco.equals("/roupas/faces/home.xhtml")) {
			chain.doFilter(request, response);
			return;
		}

		// retorna a sessao corrente (false - para nao criar uma nova sessao)
		HttpSession session = servletRequest.getSession(false);

		Usuario usuario = null;
		if (session != null) {
			usuario = (Usuario) session.getAttribute("usuarioLogado");
		}
		
		if (usuario == null) {
			((HttpServletResponse) response).sendRedirect("/roupas/faces/login.xhtml");
		} else {
			// nesse local podemos trabalhar as permissoes por pagina
			if (endereco.equals("/roupas/faces/template.xhtml")) {
				((HttpServletResponse) response).sendRedirect("/roupas/faces/home.xhtml");
			}

			if (usuario.getTipoUsuario() == TipoUsuario.CLIENTE) {
				if (endereco.equals("/roupas/faces/cadastroAdministrador.xhtml")) {
					((HttpServletResponse) response).sendRedirect("/roupas/faces/home.xhtml");
				}
			}

			if (usuario.getTipoUsuario() != TipoUsuario.NAO_DEFINIDO) {
				if (endereco.equals("/roupas/faces/cadastroCliente.xhtml")) {
					((HttpServletResponse) response).sendRedirect("/roupas/faces/home.xhtml");
				}
			}
			
			// segue o fluxo
			chain.doFilter(request, response);
			return;
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		System.out.println("SecurityFilter Iniciado.");
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
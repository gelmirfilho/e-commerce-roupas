package br.com.roupas.model;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;

import br.com.roupas.application.Session;

public class Cartao extends Entity<Cartao> {

	private static final long serialVersionUID = 8937705806231172929L;

	private Usuario usuario;

	private LocalDate validade;

	@NotBlank
	private String numero;

	@NotBlank
	private String cvv;

	public Usuario getUsuario() {
		return usuario = (Usuario) Session.getInstance().getAttribute("usuarioLogado");
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public LocalDate getValidade() {
		return validade;
	}

	public void setValidade(LocalDate validade) {
		this.validade = validade;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getCvv() {
		return cvv;
	}

	public void setCvv(String cvv) {
		this.cvv = cvv;
	}

}

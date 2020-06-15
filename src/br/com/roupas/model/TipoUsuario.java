package br.com.roupas.model;

public enum TipoUsuario {
	
	NAO_DEFINIDO(0, "Selecione um tipo..."),
	CLIENTE(1, "Cliente"),
	FUNCIONARIO(2, "Funcionário"),
	ADMINISTRADOR(3, "Adminstrador");
	
	private int id;
	private String label;
	
	private TipoUsuario(int id, String label) {
		this.id = id;
		this.label = label;
	}

	public int getId() {
		return id;
	}
	
	public String getLabel() {
		return label;
	}
	
	public static TipoUsuario valueOf(int valor) {
		for (TipoUsuario tipoUsuario : TipoUsuario.values()) {
			if (valor == tipoUsuario.getId())
				return tipoUsuario;
		} 
		return null;
	}
	
}

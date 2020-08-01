package br.com.roupas.model;

public class ItemVenda extends Entity<ItemVenda>{

	private static final long serialVersionUID = 5903319328363079314L;

	private Roupa roupa;
	private Float valor;
	private Venda venda;
	
	public Roupa getRoupa() {
		return roupa;
	}
	public void setRoupa(Roupa roupa) {
		this.roupa = roupa;
	}
	public Float getValor() {
		return valor;
	}
	public void setValor(Float valor) {
		this.valor = valor;
	}
	public Venda getVenda() {
		return venda;
	}
	public void setVenda(Venda venda) {
		this.venda = venda;
	}
	
	
}

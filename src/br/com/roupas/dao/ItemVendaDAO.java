package br.com.roupas.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.roupas.model.ItemVenda;
import br.com.roupas.model.Roupa;
import br.com.roupas.model.Venda;

public class ItemVendaDAO extends DAO<ItemVenda>{

	@Override
public boolean create(ItemVenda itemVenda) {
		
		boolean retorno = false;
		Connection conn = getConnection();
		
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO public.itemVenda ");
		sql.append("	(valor, idvenda, idroupa) ");
		sql.append("VALUES ");
		sql.append("	(?, ?, ?) ");
		
		PreparedStatement stat = null;
		
		try {
			stat = conn.prepareStatement(sql.toString());
			
			stat.setFloat(1, itemVenda.getValor());
			stat.setInt(2, itemVenda.getVenda().getId());
			stat.setInt(3, itemVenda.getRoupa().getId());
			stat.execute();
			
			conn.commit();
			
			retorno = true;
		} catch (SQLException e) {
			e.printStackTrace();
			rollback(conn);
		} finally {
			closeStatement(stat);
			closeConnection(conn);
		}
		return retorno;	
		
	}

	@Override
	public boolean update(ItemVenda entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<ItemVenda> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemVenda findById(int id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<ItemVenda> findByVenda(Venda venda) {
		List<ItemVenda> listaItemVenda = new ArrayList<ItemVenda>();
		Connection conn = getConnection();
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append("  v.id, ");
		sql.append("  v.valor, ");
		sql.append("  v.idroupa, ");
		sql.append("  r.descricao, ");
		sql.append("  r.tamanho, ");
		sql.append("  r.preco, ");
		sql.append("  r.estoque ");
		sql.append("FROM ");
		sql.append("  public.itemvenda v, ");
		sql.append("  public.roupa r ");
		sql.append("WHERE ");
		sql.append("  v.idroupa = r.id AND ");
		sql.append("  v.idvenda = ? ");
		
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(sql.toString());
			stat.setInt(1, venda.getId());
			
			ResultSet rs = stat.executeQuery();
			
			while(rs.next()) {
				ItemVenda item = new ItemVenda();
				item.setId(rs.getInt("id"));
				item.setValor(rs.getFloat("valor"));
				
				Roupa roupa = new Roupa();
				roupa.setId(rs.getInt("id"));
				roupa.setDescricao(rs.getString("descricao"));
				roupa.setTamanho(rs.getString("tamanho"));
				roupa.setPreco(rs.getFloat("preco"));
				roupa.setEstoque(rs.getInt("estoque"));
				
				item.setRoupa(roupa);
				
				item.setVenda(venda);
				
				listaItemVenda.add(item);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeStatement(stat);
			closeConnection(conn);
		}
		return listaItemVenda;
	}
	
	public float findPrecoVenda(Venda venda) {
		Connection conn = getConnection();
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append("  v.id, ");
		sql.append("  v.valor, ");
		sql.append("  v.idroupa, ");
		sql.append("  r.descricao, ");
		sql.append("  r.tamanho, ");
		sql.append("  r.preco, ");
		sql.append("  r.estoque ");
		sql.append("FROM ");
		sql.append("  public.itemvenda v, ");
		sql.append("  public.roupa r ");
		sql.append("WHERE ");
		sql.append("  v.idroupa = r.id AND ");
		sql.append("  v.idvenda = ? ");
		
		PreparedStatement stat = null;
		float total = 0;
		try {
			stat = conn.prepareStatement(sql.toString());
			stat.setInt(1, venda.getId());
			
			ResultSet rs = stat.executeQuery();			
			
			while(rs.next()) {
				ItemVenda item = new ItemVenda();
				item.setId(rs.getInt("id"));
				total = total + (rs.getFloat("valor"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeStatement(stat);
			closeConnection(conn);
		}
		return total;
	}

}

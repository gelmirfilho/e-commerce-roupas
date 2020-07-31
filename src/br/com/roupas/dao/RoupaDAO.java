package br.com.roupas.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.roupas.model.Roupa;

public class RoupaDAO extends DAO<Roupa> {

	@Override
	public boolean create(Roupa roupa) {

		boolean retorno = false;
		Connection conn = getConnection();

		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO roupa ");
		sql.append("	(descricao, tamanho, preco, estoque) ");
		sql.append("VALUES ");
		sql.append("	( ? , ? , ? , ? ) ");

		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(sql.toString());
			stat.setString(1, roupa.getDescricao());
			stat.setString(2, roupa.getTamanho());
			if (roupa.getPreco() != null)
				stat.setFloat(3, roupa.getPreco());
			else
				stat.setNull(3, java.sql.Types.FLOAT);

			if (roupa.getEstoque() != null)
				stat.setInt(4, roupa.getEstoque());
			else
				stat.setNull(4, java.sql.Types.INTEGER);

			stat.execute();

			conn.commit();

			System.out.println("Inclusão realizada com sucesso.");

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
	public boolean update(Roupa roupa) {
		boolean retorno = false;
		Connection conn = getConnection();
		
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE roupa ");
		sql.append("	SET descricao=?, tamanho=?, preco=?, estoque=? ");
		sql.append("WHERE ");
		sql.append("	id = ? ");
		
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(sql.toString());
			stat.setString(1, roupa.getDescricao());
			stat.setString(2, roupa.getTamanho());
			stat.setFloat(3, roupa.getPreco());
			stat.setInt(4, roupa.getEstoque());
			stat.setInt(5, roupa.getId());
			
			stat.execute();
			
			conn.commit();

			System.out.println("Alteração realizada com sucesso.");
			
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
	public boolean delete(int id) {
		boolean retorno = false;
		Connection conn = getConnection();
		
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE FROM roupa ");
		sql.append("WHERE ");
		sql.append("	id = ? ");
		
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(sql.toString());
			stat.setInt(1, id);
			
			stat.execute();
			
			conn.commit();

			System.out.println("Remoção realizada com sucesso.");
			
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
	public List<Roupa> findAll() {
		List<Roupa> listaRoupa = new ArrayList<Roupa>();
		Connection conn = getConnection();
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append(" 	id, descricao, tamanho, preco, estoque ");
		sql.append("FROM ");
		sql.append("	roupa ");
		
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(sql.toString());
			
			ResultSet rs = stat.executeQuery();
			
			Roupa roupa = null;
			
			while(rs.next()) {
				roupa = new Roupa();
				roupa.setId(rs.getInt("id"));
				roupa.setDescricao(rs.getString("descricao"));
				roupa.setTamanho(rs.getString("tamanho"));
				roupa.setPreco(rs.getFloat("preco"));
				roupa.setEstoque(rs.getInt("estoque"));
				listaRoupa.add(roupa);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			rollback(conn);
		} finally {
			closeStatement(stat);
			closeConnection(conn);
		}
		return listaRoupa;
	}

	@Override
	public Roupa findById(int id) {
		Roupa roupa = null;
		Connection conn = getConnection();
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append(" 	id, descricao, tamanho, preco, estoque ");
		sql.append("FROM ");
		sql.append("	roupa ");
		sql.append("WHERE ");
		sql.append("	id = ? ");

		
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(sql.toString());
			stat.setInt(1, id);
			
			ResultSet rs = stat.executeQuery();
			
			while(rs.next()) {
				roupa = new Roupa();
				roupa.setId(rs.getInt("id"));
				roupa.setDescricao(rs.getString("descricao"));
				roupa.setTamanho(rs.getString("tamanho"));
				roupa.setPreco(rs.getFloat("preco"));
				roupa.setEstoque(rs.getInt("estoque"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			rollback(conn);
		} finally {
			closeStatement(stat);
			closeConnection(conn);
		}
		return roupa;
	}

	public List<Roupa> findByDescricao(String descricao) {
		List<Roupa> listaRoupa = new ArrayList<Roupa>();
		Connection conn = getConnection();
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append(" 	id, descricao, tamanho, preco, estoque ");
		sql.append("FROM ");
		sql.append("	roupa ");
		sql.append("WHERE ");
		sql.append("	descricao ilike ? ");
		sql.append("ORDER BY descricao ");
		
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(sql.toString());
			stat.setString(1, "%" + descricao  + "%");
			
			ResultSet rs = stat.executeQuery();
			
			Roupa roupa = null;
			
			while(rs.next()) {
				roupa = new Roupa();
				roupa.setId(rs.getInt("id"));
				roupa.setDescricao(rs.getString("descricao"));
				roupa.setTamanho(rs.getString("tamanho"));
				roupa.setPreco(rs.getFloat("preco"));
				roupa.setEstoque(rs.getInt("estoque"));
				listaRoupa.add(roupa);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			rollback(conn);
		} finally {
			closeStatement(stat);
			closeConnection(conn);
		}
		return listaRoupa;
	}	
	
	public List<Roupa> findByTamanho(String tamanho) {
		List<Roupa> listaRoupa = new ArrayList<Roupa>();
		Connection conn = getConnection();
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append(" 	id, descricao, tamanho, preco, estoque ");
		sql.append("FROM ");
		sql.append("	roupa ");
		sql.append("WHERE ");
		sql.append("	tamanho ilike ? ");
		sql.append("ORDER BY tamanho ");
		
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(sql.toString());
			stat.setString(1, "%" + tamanho  + "%");
			
			ResultSet rs = stat.executeQuery();
			
			Roupa roupa = null;
			
			while(rs.next()) {
				roupa = new Roupa();
				roupa.setId(rs.getInt("id"));
				roupa.setDescricao(rs.getString("descricao"));
				roupa.setTamanho(rs.getString("tamanho"));
				roupa.setPreco(rs.getFloat("preco"));
				roupa.setEstoque(rs.getInt("estoque"));
				listaRoupa.add(roupa);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			rollback(conn);
		} finally {
			closeStatement(stat);
			closeConnection(conn);
		}
		return listaRoupa;
	}	
}

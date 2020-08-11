package br.com.roupas.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.roupas.application.Util;
import br.com.roupas.model.Cartao;
import br.com.roupas.model.TipoUsuario;
import br.com.roupas.model.Usuario;
import br.com.roupas.model.Venda;

public class CartaoDAO extends DAO<Cartao> {

	@Override
	public boolean create(Cartao cartao) {

		boolean retorno = false;
		Connection conn = getConnection();

		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO public.cartaocredito");
		sql.append(" (idusuario, validade, numero, cvv) ");
		sql.append("VALUES ");
		sql.append(" (?, ?, ?, ?) ");

		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(sql.toString());
			stat.setInt(1, cartao.getUsuario().getId());
			stat.setDate(2, Date.valueOf(cartao.getValidade()));
			stat.setString(3, cartao.getNumero());
			stat.setString(4, cartao.getCvv());

			stat.execute();

			conn.commit();

			System.out.println("Inclusão realizada com sucesso.");

			retorno = true;

		} catch (SQLException e) {
			e.printStackTrace();
			rollback(conn);
		} catch (Exception e) {
			e.printStackTrace();
			rollback(conn);
		} finally {
			closeStatement(stat);
			closeConnection(conn);
		}
		return retorno;

	}

	@Override
	public boolean update(Cartao cartao) {
		boolean retorno = false;
		Connection conn = getConnection();
		
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE cartaocredito ");
		sql.append("	SET validade=?, numero=?, cvv=? ");
		sql.append("WHERE ");
		sql.append("	id = ? ");
		
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(sql.toString());
			stat.setDate(1, Date.valueOf(cartao.getValidade()));
			stat.setString(2, cartao.getNumero());
			stat.setString(3, cartao.getCvv());
			stat.setInt(4, cartao.getId());
			
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
		sql.append("DELETE FROM cartaocredito ");
		sql.append("WHERE ");
		sql.append("	id = ? ");
		
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(sql.toString());
			stat.setInt(1, id);
			
			stat.execute();
			
			conn.commit();

			System.out.println("Remoção realizada com sucesso.");
			Util.addInfoMessage("Cartão apagado com sucesso");
			
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
	public List<Cartao> findAll() {
		List<Cartao> listaCartao = new ArrayList<Cartao>();
		Connection conn = getConnection();

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append("  c.id, ");
		sql.append("  c.validade, ");
		sql.append("  c.numero, ");
		sql.append("  c.cvv, ");
		sql.append("  l.idusuario, ");
		sql.append("  l.email, ");
		sql.append("  l.cpf, ");
		sql.append("  l.nome, ");
		sql.append("  l.sobrenome, ");
		sql.append("  l.datadenascimento, ");
		sql.append("  l.senha, ");
		sql.append("  l.telefone, ");
		sql.append("  l.tipodeusuario ");
		sql.append("FROM ");
		sql.append("  public.cartaocredito c, ");
		sql.append("  public.login l ");
		sql.append("WHERE ");
		sql.append("  c.idusuario = l.id ");

		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(sql.toString());

			ResultSet rs = stat.executeQuery();

			while (rs.next()) {
				Cartao cartao = new Cartao();
				cartao.setId(rs.getInt("id"));
				cartao.setValidade(rs.getDate("validade").toLocalDate());
				cartao.setNumero(rs.getString("numero"));
				cartao.setCvv(rs.getString("cvv"));
				cartao.setUsuario(new Usuario());
				cartao.getUsuario().setId(rs.getInt("idusuario"));
				cartao.getUsuario().setEmail(rs.getString("email"));
				cartao.getUsuario().setCpf(rs.getString("cpf"));
				cartao.getUsuario().setNome(rs.getString("nome"));
				cartao.getUsuario().setSobrenome(rs.getString("sobrenome"));
				Date data = rs.getDate("datadenascimento");
				cartao.getUsuario().setDataNascimento(data == null ? null : data.toLocalDate());
				cartao.getUsuario().setSenha(rs.getString("senha"));
				cartao.getUsuario().setTelefone(rs.getString("telefone"));
				cartao.getUsuario().setTipoUsuario(TipoUsuario.valueOf(rs.getInt("tipodeusuario")));

				listaCartao.add(cartao);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			rollback(conn);
		} finally {
			closeStatement(stat);
			closeConnection(conn);
		}
		return listaCartao;
	}

	@Override
	public Cartao findById(int id) {
		Cartao cartao = null;
		Connection conn = getConnection();

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append("  v.id, ");
		sql.append("  v.validade, ");
		sql.append("  v.numero, ");
		sql.append("  v.cvv, ");
		sql.append("  u.id as idusuario, ");
		sql.append("  u.email, ");
		sql.append("  u.cpf, ");
		sql.append("  u.nome, ");
		sql.append("  u.sobrenome, ");
		sql.append("  u.datadenascimento, ");
		sql.append("  u.senha, ");
		sql.append("  u.telefone, ");
		sql.append("  u.tipodeusuario ");
		sql.append("FROM ");
		sql.append("  public.cartaocredito v, ");
		sql.append("  public.login u ");
		sql.append("WHERE ");
		sql.append("  v.idusuario = u.id AND ");
		sql.append("  u.id = ? ");

		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(sql.toString());
			stat.setInt(1, id);

			ResultSet rs = stat.executeQuery();
			while (rs.next()) {
				cartao = new Cartao();
				cartao.setId(rs.getInt("id"));
				cartao.setValidade(rs.getDate("validade").toLocalDate());
				cartao.setNumero(rs.getString("numero"));
				cartao.setCvv(rs.getString("cvv"));
				cartao.setUsuario(new Usuario());
				cartao.getUsuario().setId(rs.getInt("idusuario"));
				cartao.getUsuario().setEmail(rs.getString("email"));
				cartao.getUsuario().setCpf(rs.getString("cpf"));
				cartao.getUsuario().setNome(rs.getString("nome"));
				cartao.getUsuario().setSobrenome(rs.getString("sobrenome"));
				Date data = rs.getDate("datadenascimento");
				cartao.getUsuario().setDataNascimento(data == null ? null : data.toLocalDate());
				cartao.getUsuario().setSenha(rs.getString("senha"));
				cartao.getUsuario().setTelefone(rs.getString("telefone"));
				cartao.getUsuario().setTipoUsuario(TipoUsuario.valueOf(rs.getInt("tipodeusuario")));

				cartao.getUsuario().setDataNascimento(data == null ? null : data.toLocalDate());
			}

		} catch (SQLException e) {
			e.printStackTrace();
			rollback(conn);
		} finally {
			closeStatement(stat);
			closeConnection(conn);
		}
		return cartao;
	}

	public List<Cartao> findByUsuario(int idUsuario) {
		List<Cartao> listaCartao = new ArrayList<Cartao>();
		Connection conn = getConnection();

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append("  v.id, ");
		sql.append("  v.validade, ");
		sql.append("  v.numero, ");
		sql.append("  v.cvv, ");
		sql.append("  u.id as idusuario, ");
		sql.append("  u.email, ");
		sql.append("  u.cpf, ");
		sql.append("  u.nome, ");
		sql.append("  u.sobrenome, ");
		sql.append("  u.datadenascimento, ");
		sql.append("  u.senha, ");
		sql.append("  u.telefone, ");
		sql.append("  u.tipodeusuario ");
		sql.append("FROM ");
		sql.append("  public.cartaocredito v, ");
		sql.append("  public.login u ");
		sql.append("WHERE ");
		sql.append("  v.idusuario = u.id AND ");
		sql.append("  u.id = ? ");

		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(sql.toString());

			stat.setInt(1, idUsuario);

			ResultSet rs = stat.executeQuery();

			while (rs.next()) {
				Cartao cartao = new Cartao();
				cartao.setId(rs.getInt("id"));
				cartao.setValidade(rs.getDate("validade").toLocalDate());
				cartao.setNumero(rs.getString("numero"));
				cartao.setCvv(rs.getString("cvv"));
				cartao.setUsuario(new Usuario());
				cartao.getUsuario().setId(rs.getInt("idusuario"));
				cartao.getUsuario().setEmail(rs.getString("email"));
				cartao.getUsuario().setCpf(rs.getString("cpf"));
				cartao.getUsuario().setNome(rs.getString("nome"));
				cartao.getUsuario().setSobrenome(rs.getString("sobrenome"));
				Date data = rs.getDate("datadenascimento");
				cartao.getUsuario().setDataNascimento(data == null ? null : data.toLocalDate());
				cartao.getUsuario().setSenha(rs.getString("senha"));
				cartao.getUsuario().setTelefone(rs.getString("telefone"));
				cartao.getUsuario().setTipoUsuario(TipoUsuario.valueOf(rs.getInt("tipodeusuario")));

				listaCartao.add(cartao);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			rollback(conn);
		} finally {
			closeStatement(stat);
			closeConnection(conn);
		}
		return listaCartao;
	}

}

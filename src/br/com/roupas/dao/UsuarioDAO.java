package br.com.roupas.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.roupas.application.Util;
import br.com.roupas.model.TipoUsuario;
import br.com.roupas.model.Usuario;

public class UsuarioDAO extends DAO<Usuario> {

	public boolean create(Usuario usuario) {

		boolean retorno = false;
		Connection conn = getConnection();

		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO login ");
		sql.append("	(email, cpf, nome, sobrenome, datadenascimento, senha, telefone, tipodeusuario) ");
		sql.append("VALUES ");
		sql.append("	( ? , ? , ? , ? , ? , ? , ? , ? ) ");

		PreparedStatement stat = null;
		try {
			
			//valida email e cpf se já possui no banco de dados
			if(verificarLogin(usuario.getEmail())) {
				Util.addErrorMessage("Email já cadastrado");
				return false;
			}
			if(verificarCpf(usuario.getCpf())) {
				Util.addErrorMessage("Cpf já cadastrado");
				return false;
			}
			
			stat = conn.prepareStatement(sql.toString());
			stat.setString(1, usuario.getEmail());		
			stat.setString(2, usuario.getCpf().replaceAll("\\.", "").replaceAll("-", ""));			
			stat.setString(3, usuario.getNome());
			stat.setString(4, usuario.getSobrenome());
			stat.setDate(5, java.sql.Date.valueOf(usuario.getDataNascimento()));
			stat.setString(6, usuario.getSenha());
			stat.setString(7, usuario.getTelefone());
			stat.setInt(8, usuario.getTipoUsuario().getId());

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

	public boolean update(Usuario usuario) {
		boolean retorno = false;
		Connection conn = getConnection();

		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE login ");
		sql.append(
				"	SET email=?, cpf=?, nome=?, sobrenome=?, datadenascimento=?, senha=?, telefone=?, tipodeusuario=? ");
		sql.append("WHERE ");
		sql.append("	id = ? ");

		PreparedStatement stat = null;
		try {

			stat = conn.prepareStatement(sql.toString());
			stat.setString(1, usuario.getEmail());
			stat.setString(2, usuario.getCpf().replaceAll("\\.", "").replaceAll("-", ""));
			stat.setString(3, usuario.getNome());
			stat.setString(4, usuario.getSobrenome());
			stat.setDate(5, java.sql.Date.valueOf(usuario.getDataNascimento()));
			stat.setString(6, usuario.getSenha());
			stat.setString(7, usuario.getTelefone());
			stat.setInt(8, usuario.getTipoUsuario().getId());
			stat.setInt(9, usuario.getId());

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

	public boolean delete(int id) {
		boolean retorno = false;
		Connection conn = getConnection();

		StringBuffer sql = new StringBuffer();
		sql.append("DELETE FROM login ");
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

	public List<Usuario> findAll() {
		List<Usuario> listaUsuario = new ArrayList<Usuario>();
		Connection conn = getConnection();

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append(" 	id, email, cpf, nome, sobrenome, datadenascimento, senha, telefone, tipodeusuario ");
		sql.append("FROM ");
		sql.append("	login ");

		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(sql.toString());

			ResultSet rs = stat.executeQuery();

			Usuario usuario = null;

			while (rs.next()) {
				usuario = new Usuario();
				usuario.setId(rs.getInt("id"));
				usuario.setEmail(rs.getString("email"));
				usuario.setCpf(rs.getString("cpf"));
				usuario.setNome(rs.getString("nome"));
				usuario.setSobrenome(rs.getString("sobrenome"));
				usuario.setDataNascimento(rs.getDate("datadenascimento").toLocalDate());
				usuario.setSenha(rs.getString("senha"));
				usuario.setTelefone(rs.getString("telefone"));
				usuario.setTipoUsuario(TipoUsuario.valueOf(rs.getInt("tipodeusuario")));

				listaUsuario.add(usuario);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			rollback(conn);
		} finally {
			closeStatement(stat);
			closeConnection(conn);
		}
		return listaUsuario;
	}

	public Usuario findById(int id) {
		Usuario usuario = null;
		Connection conn = getConnection();

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append(" 	id, email, cpf, nome, sobrenome, datadenascimento, senha, telefone, tipodeusuario ");
		sql.append("FROM ");
		sql.append("	login ");
		sql.append("WHERE ");
		sql.append("	id = ? ");

		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(sql.toString());
			stat.setInt(1, id);

			ResultSet rs = stat.executeQuery();

			while (rs.next()) {
				usuario = new Usuario();
				usuario.setId(rs.getInt("id"));
				usuario.setEmail(rs.getString("email"));
				usuario.setCpf(rs.getString("cpf"));
				usuario.setNome(rs.getString("nome"));
				usuario.setSobrenome(rs.getString("sobrenome"));
				usuario.setDataNascimento(rs.getDate("datadenascimento").toLocalDate());
				usuario.setSenha(rs.getString("senha"));
				usuario.setTelefone(rs.getString("telefone"));
				usuario.setTipoUsuario(TipoUsuario.valueOf(rs.getInt("tipodeusuario")));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			rollback(conn);
		} finally {
			closeStatement(stat);
			closeConnection(conn);
		}
		return usuario;
	}
	
	public List<Usuario> findByNome(String nome) {
		List<Usuario> listaUsuario = new ArrayList<Usuario>();
		Connection conn = getConnection();
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append(" 	id, email, cpf, nome, sobrenome, datadenascimento, senha, telefone, tipodeusuario ");
		sql.append("FROM ");
		sql.append("	login ");
		sql.append("WHERE ");
		sql.append("	nome ilike ? ");
		sql.append("ORDER BY nome ");
		
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(sql.toString());
			stat.setString(1, "%" + nome  + "%");
			
			ResultSet rs = stat.executeQuery();
			
			Usuario usuario = null;
			
			while(rs.next()) {
				usuario = new Usuario();
				usuario.setId(rs.getInt("id"));
				usuario.setEmail(rs.getString("email"));
				usuario.setCpf(rs.getString("cpf"));
				usuario.setNome(rs.getString("nome"));
				usuario.setSobrenome(rs.getString("sobrenome"));
				usuario.setDataNascimento(rs.getDate("datadenascimento").toLocalDate());
				usuario.setSenha(rs.getString("senha"));
				usuario.setTelefone(rs.getString("telefone"));
				usuario.setTipoUsuario(TipoUsuario.valueOf(rs.getInt("tipodeusuario")));
				listaUsuario.add(usuario);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			rollback(conn);
		} finally {
			closeStatement(stat);
			closeConnection(conn);
		}
		return listaUsuario;
	}
	
	public List<Usuario> findByCpf(String nome) {
		List<Usuario> listaUsuario = new ArrayList<Usuario>();
		Connection conn = getConnection();
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append(" 	id, email, cpf, nome, sobrenome, datadenascimento, senha, telefone, tipodeusuario ");
		sql.append("FROM ");
		sql.append("	login ");
		sql.append("WHERE ");
		sql.append("	cpf ilike ? ");
		sql.append("ORDER BY cpf ");
		
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(sql.toString());
			stat.setString(1, "%" + nome  + "%");
			
			ResultSet rs = stat.executeQuery();
			
			Usuario usuario = null;
			
			while(rs.next()) {
				usuario = new Usuario();
				usuario.setId(rs.getInt("id"));
				usuario.setEmail(rs.getString("email"));
				usuario.setCpf(rs.getString("cpf"));
				usuario.setNome(rs.getString("nome"));
				usuario.setSobrenome(rs.getString("sobrenome"));
				usuario.setDataNascimento(rs.getDate("datadenascimento").toLocalDate());
				usuario.setSenha(rs.getString("senha"));
				usuario.setTelefone(rs.getString("telefone"));
				usuario.setTipoUsuario(TipoUsuario.valueOf(rs.getInt("tipodeusuario")));
				listaUsuario.add(usuario);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			rollback(conn);
		} finally {
			closeStatement(stat);
			closeConnection(conn);
		}
		return listaUsuario;
	}	
	
	public Usuario verificarLoginSenha(String email, String senha) {
		Usuario usuario = null;
		Connection conn = getConnection();

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append(" 	id, email, cpf, nome, sobrenome, datadenascimento, senha, telefone, tipodeusuario ");
		sql.append("FROM ");
		sql.append("	login ");
		sql.append("WHERE ");
		sql.append("	email = ? ");
		sql.append("	AND senha = ? ");

		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(sql.toString());
			stat.setString(1, email);
			stat.setString(2, senha);

			ResultSet rs = stat.executeQuery();

			while (rs.next()) {
				usuario = new Usuario();
				usuario.setId(rs.getInt("id"));
				usuario.setEmail(rs.getString("email"));
				usuario.setCpf(rs.getString("cpf"));
				usuario.setNome(rs.getString("nome"));
				usuario.setSobrenome(rs.getString("sobrenome"));
				usuario.setDataNascimento(rs.getDate("datadenascimento").toLocalDate());
				usuario.setSenha(rs.getString("senha"));
				usuario.setTelefone(rs.getString("telefone"));
				usuario.setTipoUsuario(TipoUsuario.valueOf(rs.getInt("tipodeusuario")));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			rollback(conn);
		} finally {
			closeStatement(stat);
			closeConnection(conn);
		}
		return usuario;
	}
	
	public boolean verificarLogin(String email) {
		Usuario usuario = null;
		Connection conn = getConnection();

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append(" 	id, email, cpf, nome, sobrenome, datadenascimento, senha, telefone, tipodeusuario ");
		sql.append("FROM ");
		sql.append("	login ");
		sql.append("WHERE ");
		sql.append("	email = ? ");

		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(sql.toString());
			stat.setString(1, email);

			ResultSet rs = stat.executeQuery();

			while (rs.next()) {
				usuario = new Usuario();
				usuario.setId(rs.getInt("id"));
				usuario.setEmail(rs.getString("email"));
				usuario.setCpf(rs.getString("cpf"));
				usuario.setNome(rs.getString("nome"));
				usuario.setSobrenome(rs.getString("sobrenome"));
				usuario.setDataNascimento(rs.getDate("datadenascimento").toLocalDate());
				usuario.setSenha(rs.getString("senha"));
				usuario.setTelefone(rs.getString("telefone"));
				usuario.setTipoUsuario(TipoUsuario.valueOf(rs.getInt("tipodeusuario")));
				
				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			rollback(conn);
		} finally {
			closeStatement(stat);
			closeConnection(conn);
		}
		return false;
	}
	
	public boolean verificarCpf(String cpf) {
		Usuario usuario = null;
		Connection conn = getConnection();

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append(" 	id, email, cpf, nome, sobrenome, datadenascimento, senha, telefone, tipodeusuario ");
		sql.append("FROM ");
		sql.append("	login ");
		sql.append("WHERE ");
		sql.append("	cpf = ? ");

		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(sql.toString());
			stat.setString(1, cpf);

			ResultSet rs = stat.executeQuery();

			while (rs.next()) {
				usuario = new Usuario();
				usuario.setId(rs.getInt("id"));
				usuario.setEmail(rs.getString("email"));
				usuario.setCpf(rs.getString("cpf"));
				usuario.setNome(rs.getString("nome"));
				usuario.setSobrenome(rs.getString("sobrenome"));
				usuario.setDataNascimento(rs.getDate("datadenascimento").toLocalDate());
				usuario.setSenha(rs.getString("senha"));
				usuario.setTelefone(rs.getString("telefone"));
				usuario.setTipoUsuario(TipoUsuario.valueOf(rs.getInt("tipodeusuario")));
				
				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			rollback(conn);
		} finally {
			closeStatement(stat);
			closeConnection(conn);
		}
		return false;
	}

}

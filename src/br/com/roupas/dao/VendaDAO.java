package br.com.roupas.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.roupas.model.ItemVenda;
import br.com.roupas.model.TipoUsuario;
import br.com.roupas.model.Usuario;
import br.com.roupas.model.Venda;

public class VendaDAO extends DAO<Venda> {

	@Override
	public boolean create(Venda venda) {

		boolean retorno = false;
		Connection conn = getConnection();

		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO public.venda");
		sql.append(" (data, idusuario) ");
		sql.append("VALUES ");
		sql.append(" (?, ?) ");

		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
			stat.setDate(1, Date.valueOf(venda.getData()));
			stat.setInt(2, venda.getUsuario().getId());

			stat.execute();

			// obtendo o id gerado pela tabela do banco de dados
			ResultSet rs = stat.getGeneratedKeys();
			rs.next();
			venda.setId(rs.getInt("id"));
			// inserindo os itens de venda

			ItemVendaDAO dao = new ItemVendaDAO();
			for (ItemVenda itemVenda : venda.getListaItemVenda()) {
				// informando quem eh o pai da crianca
				itemVenda.setVenda(venda);
				// salvando no banco de dados
//				if (dao.create(itemVenda) == false) {
//					throw new Exception("Erro ao incluir um item de venda");
//				}
				if (createItemVenda(itemVenda, conn) == false) {
					throw new Exception("Erro ao incluir um item de venda");
				}

			}

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
	public boolean update(Venda entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Venda> findAll() {
		List<Venda> listaVenda = new ArrayList<Venda>();
		Connection conn = getConnection();

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append("  v.id, ");
		sql.append("  v.data, ");
		sql.append("  u.idlogin, ");
		sql.append("  u.email, ");
		sql.append("  u.cpf, ");
		sql.append("  u.nome, ");
		sql.append("  u.sobrenome, ");
		sql.append("  u.datadenascimento, ");
		sql.append("  u.senha, ");
		sql.append("  u.telefone, ");
		sql.append("  u.tipodeusuario ");
		sql.append("FROM ");
		sql.append("  public.venda v, ");
		sql.append("  public.login u ");
		sql.append("WHERE ");
		sql.append("  v.idusuario = u.id ");

		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(sql.toString());

			ResultSet rs = stat.executeQuery();

			while (rs.next()) {
				Venda venda = new Venda();
				venda.setId(rs.getInt("id"));
				venda.setData(rs.getDate("data").toLocalDate());
				venda.setUsuario(new Usuario());
				venda.getUsuario().setId(rs.getInt("idusuario"));
				venda.getUsuario().setEmail(rs.getString("email"));
				venda.getUsuario().setCpf(rs.getString("cpf"));
				venda.getUsuario().setNome(rs.getString("nome"));
				venda.getUsuario().setSobrenome(rs.getString("sobrenome"));
				Date data = rs.getDate("datadenascimento");
				venda.getUsuario().setDataNascimento(data == null ? null : data.toLocalDate());
				venda.getUsuario().setSenha(rs.getString("senha"));
				venda.getUsuario().setTelefone(rs.getString("telefone"));
				venda.getUsuario().setTipoUsuario(TipoUsuario.valueOf(rs.getInt("tipodeusuario")));
				// e os itens de venda?!!?
				// venda.setListaItemVenda(listaItemVenda);

				listaVenda.add(venda);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			rollback(conn);
		} finally {
			closeStatement(stat);
			closeConnection(conn);
		}
		return listaVenda;
	}

	@Override
	public Venda findById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	private boolean createItemVenda(ItemVenda itemVenda, Connection conn) {

		boolean retorno = false;

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

			retorno = true;
		} catch (SQLException e) {
			e.printStackTrace();
			rollback(conn);
		} finally {
			closeStatement(stat);
		}
		return retorno;
	}

	public List<Venda> findByUsuario(int idUsuario) {
		List<Venda> listaVenda = new ArrayList<Venda>();
		Connection conn = getConnection();

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append("  v.id, ");
		sql.append("  v.data, ");
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
		sql.append("  public.venda v, ");
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
				Venda venda = new Venda();
				venda.setId(rs.getInt("id"));
				venda.setData(rs.getDate("data").toLocalDate());
				venda.setUsuario(new Usuario());
				venda.getUsuario().setId(rs.getInt("idusuario"));
				venda.getUsuario().setEmail(rs.getString("email"));
				venda.getUsuario().setCpf(rs.getString("cpf"));
				venda.getUsuario().setNome(rs.getString("nome"));
				venda.getUsuario().setSobrenome(rs.getString("sobrenome"));
				Date data = rs.getDate("datadenascimento");
				venda.getUsuario().setDataNascimento(data == null ? null : data.toLocalDate());
				venda.getUsuario().setSenha(rs.getString("senha"));
				venda.getUsuario().setTelefone(rs.getString("telefone"));
				venda.getUsuario().setTipoUsuario(TipoUsuario.valueOf(rs.getInt("tipodeusuario")));

				// e os itens de venda?!!?
				ItemVendaDAO dao = new ItemVendaDAO();
				venda.setListaItemVenda(dao.findByVenda(venda));
				listaVenda.add(venda);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			rollback(conn);
		} finally {
			closeStatement(stat);
			closeConnection(conn);
		}
		return listaVenda;
	}
}

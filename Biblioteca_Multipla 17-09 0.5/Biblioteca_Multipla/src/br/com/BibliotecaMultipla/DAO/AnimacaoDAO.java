package br.com.BibliotecaMultipla.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import br.com.BibliotecaMultipla.AudioVisual.Animacao;
import br.com.BibliotecaMultipla.AudioVisual.Animacao_Assistido;
import br.com.BibliotecaMultipla.AudioVisual.Animacao_N_Assistido;

/**
 * Classe que representa o controle da tabela de animacoes, animacoes nao
 * assistidas e animacoes assistidas do banco de dados, a partir do programa.
 * 
 * @author Matheus
 * @version 0.2
 */
public class AnimacaoDAO {

	private Connection connection;

	/**
	 * Construtor do objeto AnimacaoDAO a partir de uma conexao.
	 */
	public AnimacaoDAO(Connection connection) {
		this.connection = connection;
	}

	/**
	 * @return uma lista de animacoes em geral do Banco de Dados. A lista ser� uma
	 *         UnmodifiableCollection.
	 */
	public Collection<Animacao> listarAnimacoes() {
		try {
			Collection<Animacao> animacoes = new ArrayList<>();
			String sql = "SELECT A.ID, A.NOME, A.ANO_DE_LANCAMENTO, A.GENERO, A.DATA_DE_REGISTRO, B.NOME, A.NUMERO_EPISODIOS FROM ANIMACAO A INNER JOIN CATEGORIA B ON A.CATEGORIA_ID = B.ID";
			try (PreparedStatement pstm = this.connection.prepareStatement(sql)) {
				pstm.execute();

				try (ResultSet rst = pstm.getResultSet()) {
					while (rst.next()) {
						Animacao animacao = new Animacao(rst.getInt(1), rst.getString(2), rst.getInt(3),
								rst.getString(4), rst.getTimestamp(5), rst.getString(6), rst.getInt(7));
						animacoes.add(animacao);
					}
				}
			}
			return Collections.unmodifiableCollection(animacoes);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @return uma lista de animacoes nao assistidas do Banco de Dados. A lista ser�
	 *         uma UnmodifiableCollection.
	 */
	public Collection<Animacao_N_Assistido> listarNaoAssistido() {
		try {
			Collection<Animacao_N_Assistido> animacoesNaoAssistidas = new ArrayList<>();
			String sql = "SELECT A.ID, A.NOME, A.ANO_DE_LANCAMENTO, A.GENERO, A.DATA_DE_REGISTRO, B.NOME AS CATEGORIA, A.NUMERO_EPISODIOS, C.PRIORIDADE, C.RELEVANCIA FROM ANIMACAO A INNER JOIN CATEGORIA B ON A.CATEGORIA_ID = B.ID INNER JOIN ANIMACAO_N_ASSISTIDO C ON C.ANIMACAO_ID = A.ID  WHERE A.ASSISTIDO = 0";

			try (PreparedStatement pstm = this.connection.prepareStatement(sql)) {
				pstm.execute();

				try (ResultSet rst = pstm.getResultSet()) {
					while (rst.next()) {
						Animacao_N_Assistido animacaoNaoAssistido = new Animacao_N_Assistido(rst.getInt(1),
								rst.getString(2), rst.getInt(3), rst.getString(4), rst.getTimestamp(5),
								rst.getString(6), rst.getInt(7), rst.getString(8), rst.getString(9));

						animacoesNaoAssistidas.add(animacaoNaoAssistido);
					}
				}
			}
			return Collections.unmodifiableCollection(animacoesNaoAssistidas);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @return uma lista de animacoes assistidas do Banco de Dados. A lista ser� uma
	 *         UnmodifiableCollection.
	 */
	public Collection<Animacao_Assistido> listarAssistido() {
		try {
			Collection<Animacao_Assistido> AnimacoesAssistidas = new ArrayList<>();
			String sql = "SELECT A.ID, A.NOME, A.ANO_DE_LANCAMENTO, A.GENERO, A.DATA_DE_REGISTRO, B.NOME AS CATEGORIA, A.NUMERO_EPISODIOS, C.DATA_DE_FINALIZACAO, C.NOTA FROM ANIMACAO A  INNER JOIN CATEGORIA B ON A.CATEGORIA_ID = B.ID INNER JOIN ANIMACAO_ASSISTIDO C ON C.ANIMACAO_ID = A.ID  WHERE A.ASSISTIDO = 1";
			try (PreparedStatement pstm = this.connection.prepareStatement(sql)) {
				pstm.execute();

				try (ResultSet rst = pstm.getResultSet()) {
					while (rst.next()) {
						Animacao_Assistido animacaoAssistido = new Animacao_Assistido(rst.getInt(1), rst.getString(2),
								rst.getInt(3), rst.getString(4), rst.getTimestamp(5), rst.getString(6), rst.getInt(7),
								rst.getTimestamp(8), rst.getDouble(9));
						AnimacoesAssistidas.add(animacaoAssistido);
					}
				}
			}
			return Collections.unmodifiableCollection(AnimacoesAssistidas);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Registra uma animacao nao assistida pelo usuario no Banco de dados.
	 * 
	 * @param animacao_N_Assistido
	 */
	public void registrarAnimacaoNAssistido(Animacao_N_Assistido animacao_N_Assistido) {
		try {
			String sql = "CALL ANIMACAO_N_ASSISTIDO_INSERCAO(?, ?, ?, ?, ?, ?, ?);";
			try (PreparedStatement pstm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
				pstm.setString(1, animacao_N_Assistido.getNome());
				pstm.setInt(2, animacao_N_Assistido.getAno_de_lancamento());
				pstm.setString(3, animacao_N_Assistido.getGenero());
				pstm.setInt(4, animacao_N_Assistido.getNumeroEpisodios());
				pstm.setInt(5, animacao_N_Assistido.getCategoria());
				pstm.setString(6, animacao_N_Assistido.getRelevancia());
				pstm.setString(7, animacao_N_Assistido.getPrioridade());

				pstm.execute();

				try (ResultSet rst = pstm.getGeneratedKeys()) {
					while (rst.next()) {
						animacao_N_Assistido.setID(rst.getInt(1));
					}
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Registra uma animacao assistida pelo usuario no Banco de dados.
	 * 
	 * @param animacao_N_Assistido
	 */
	public void registrarAnimacaoAssistido(Animacao_Assistido animacao_Assistido) {
		try {
			String sql = "CALL ANIMACAO_ASSISTIDO_INSERCAO(?, ?, ?, ?, ?, ?);";
			try (PreparedStatement pstm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
				pstm.setString(1, animacao_Assistido.getNome());
				pstm.setInt(2, animacao_Assistido.getAno_de_lancamento());
				pstm.setString(3, animacao_Assistido.getGenero());
				pstm.setInt(4, animacao_Assistido.getNumeroEpisodios());
				pstm.setInt(5, animacao_Assistido.getCategoria());
				pstm.setDouble(6, animacao_Assistido.getNota());

				pstm.execute();

				try (ResultSet rst = pstm.getGeneratedKeys()) {
					while (rst.next()) {
						animacao_Assistido.setID(rst.getInt(1));
					}
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Metodo com a funcao de mudar uma animacao da classificacao de "nao assistido"
	 * para "assisto". Ao ser mudada, o usuario deve inserir o id da animacao que
	 * quer mudar, e qual nota ele quer dar para ela.
	 * 
	 * @param id
	 * @param nota
	 */
	public void finalizaAnimacao(Integer id, Double nota) {
		try {
			String sql = "call TERMINA_ANIMACAO(?, ?);";

			try (PreparedStatement pstm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
				pstm.setInt(1, id);
				pstm.setDouble(2, nota);

				pstm.execute();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}

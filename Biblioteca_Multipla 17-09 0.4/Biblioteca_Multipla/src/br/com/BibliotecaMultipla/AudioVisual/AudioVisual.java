package br.com.BibliotecaMultipla.AudioVisual;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.BibliotecaMultipla.Categoria.CategoriaEnum;
import br.com.BibliotecaMultipla.Identificador.Identificacao;
import br.com.BibliotecaMultipla.Identificador.Identificador;

/**
 * Representa o corpo de um item �udio visual
 * 
 * @author Matheus
 * @version 0.2
 */

public abstract class AudioVisual implements Identificacao {

	private Identificador identificador;
	private Integer ano_de_lancamento;
	private String genero;
	private Timestamp data_de_registro;
	private Integer categoria;
	
	
	public AudioVisual(Integer id, String nome, Integer ano_de_lancamento, String genero, Timestamp data_de_registro, String categoria) {
		Identificador identificador = new Identificador(id, nome);
		this.identificador = identificador;
		this.ano_de_lancamento = ano_de_lancamento;
		this.genero = genero;
		this.data_de_registro = data_de_registro;
		this.categoria = this.retornaCategoriaID(categoria);
	}

	@Override
	public Integer getId() {
		return this.identificador.getId();
	}

	@Override
	public String getNome() {
		return this.identificador.getNome();
	}
	
	@Override
	public void setNome(String nome) {
		this.identificador.setNome(nome);
	}
	
	@Override
	public void setID(Integer id) {
		this.identificador.setId(id);
	}

	public int getAno_de_lancamento() {
		return ano_de_lancamento;
	}

	public String getGenero() {
		return genero;
	}

	public Timestamp getData_de_registro() {
		return data_de_registro;
	}
	
	public Integer getCategoria() {
		return categoria;
	}

	@Override
	public String toString() {
		return "ID: " + this.getId() + ", nome: " + this.getNome() + ", Ano de Lan�amento: " + this.ano_de_lancamento + ", Generos: " + this.genero + ", Data de Registro: " + this.data_de_registro + ", Categoria: " + this.categoria;
	}
	
	public Integer retornaCategoriaID (String categoriaNome) {
		List<CategoriaEnum> enums = new ArrayList<>(Arrays.asList(CategoriaEnum.values()));
		Integer dado = 0;
		for (CategoriaEnum categoriaEnum : enums) {
			if (categoriaNome == categoriaEnum.categoriaNome()) dado = categoriaEnum.categoriaId();
		}
		return dado;
	}


}

package model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "cidade")
public class ForecastCity implements Information {

	private String nome;
	private String uf;
	private String atualizacao;
	private List<Forecast> previsao;

	public String getNome() {
		return nome;
	}

	@XmlElement
	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getUf() {
		return uf;
	}

	@XmlElement
	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getAtualizacao() {
		return atualizacao;
	}

	@XmlElement
	public void setAtualizacao(String atualizacao) {
		this.atualizacao = atualizacao;
	}

	public List<Forecast> getPrevisao() {
		return previsao;
	}

	public Forecast first() {
		return previsao.get(0);
	}

	@XmlElement
	public void setPrevisao(List<Forecast> previsao) {
		this.previsao = previsao;
	}
}

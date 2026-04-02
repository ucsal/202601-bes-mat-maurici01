package br.com.ucsal.olimpiadas.model;

public abstract class Questao {

	private long id;
	private long provaId;
	private String enunciado;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getProvaId() {
		return provaId;
	}

	public void setProvaId(long provaId) {
		this.provaId = provaId;
	}

	public String getEnunciado() {
		return enunciado;
	}

	public void setEnunciado(String enunciado) {
		this.enunciado = enunciado;
	}

	public abstract void exibirParaAluno();
	public abstract boolean verificaResposta(String entrada);

}

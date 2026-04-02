package br.com.ucsal.olimpiadas.controller;

import br.com.ucsal.olimpiadas.model.Questao;

public interface CadastroQuestao {
    Questao cadastrar(Long provaId);
}

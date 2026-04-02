package br.com.ucsal.olimpiadas.controller;

import br.com.ucsal.olimpiadas.model.Questao;
import br.com.ucsal.olimpiadas.repository.BancoDeDadosTemp;
import br.com.ucsal.olimpiadas.view.ConsoleView;
import br.com.ucsal.olimpiadas.view.QuestaoTabuleiro;

public class QuestaoController {

    private final ProvaController provaController;
    private final CadastroQuestao cadastroQuestao;

    public QuestaoController(ProvaController provaController) {
        this.provaController = provaController;
        this.cadastroQuestao = new CadastroQuestaoTabuleiro();
    }

    public void cadastrarQuestao() {
        if (BancoDeDadosTemp.provas.isEmpty()) {
            System.out.println("não há provas cadastradas");
            return;
        }

        var provaId = provaController.escolherProva();
        if (provaId == null)
            return;

        Questao q = cadastroQuestao.cadastrar(provaId);
        if (q == null){
            BancoDeDadosTemp.questoes.add(q);
            System.out.println("Questão cadastrada: " + q.getId() + " (na prova " + provaId + ")");
        }



    }
}

package br.com.ucsal.olimpiadas.controller;

import br.com.ucsal.olimpiadas.repository.BancoDeDadosTemp;
import br.com.ucsal.olimpiadas.view.ConsoleView;
import br.com.ucsal.olimpiadas.view.QuestaoTabuleiro;

public class QuestaoController {

    private final ProvaController provaController;

    public QuestaoController(ProvaController provaController) {
        this.provaController = provaController;
    }

    public void cadastrarQuestao() {
        if (BancoDeDadosTemp.provas.isEmpty()) {
            System.out.println("não há provas cadastradas");
            return;
        }

        var provaId = provaController.escolherProva();
        if (provaId == null)
            return;

        System.out.println("Enunciado:");
        var enunciado = ConsoleView.pegaInput("Enunciado: ");

        var alternativas = new String[5];
        for (int i = 0; i < 5; i++) {
            char letra = (char) ('A' + i);
            alternativas[i] = letra + ") " + ConsoleView.pegaInput("Alternativa " + letra + ": ");
        }

        System.out.print("Alternativa correta (A–E): ");
        char correta;
        try {
            correta = QuestaoTabuleiro.normalizar( ConsoleView.pegaInput("").trim().charAt(0));
        } catch (Exception e) {
            System.out.println("alternativa inválida");
            return;
        }

        var q = new QuestaoTabuleiro();
        q.setId(BancoDeDadosTemp.proximaQuestaoId++);
        q.setProvaId(provaId);
        q.setEnunciado(enunciado);
        q.setAlternativas(alternativas);
        q.setAlternativaCorreta(correta);

        BancoDeDadosTemp.questoes.add(q);

        System.out.println("Questão cadastrada: " + q.getId() + " (na prova " + provaId + ")");
    }
}

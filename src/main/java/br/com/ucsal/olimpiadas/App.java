package br.com.ucsal.olimpiadas;

import br.com.ucsal.olimpiadas.controller.ParticipanteController;
import br.com.ucsal.olimpiadas.controller.ProvaController;
import br.com.ucsal.olimpiadas.controller.QuestaoController;
import br.com.ucsal.olimpiadas.controller.TentativaController;
import br.com.ucsal.olimpiadas.model.*;
import br.com.ucsal.olimpiadas.repository.BancoDeDadosTemp;
import br.com.ucsal.olimpiadas.view.ConsoleView;
import br.com.ucsal.olimpiadas.view.QuestaoTabuleiro;


public class App {

	private static final ParticipanteController participanteController = new ParticipanteController();
	private static final ProvaController provaController = new ProvaController();
	private static final QuestaoController questaoController = new QuestaoController(provaController);
	private static final TentativaController tentativaController = new TentativaController(participanteController, provaController);

	static void main() {
		seed();

		while (true) {
			ConsoleView.exibirMenu();
			switch ( ConsoleView.pegaInput("")) {
			case "1" -> participanteController.cadastrarParticipante();
			case "2" -> provaController.cadastrarProva();
			case "3" -> questaoController.cadastrarQuestao();
			case "4" -> tentativaController.aplicarProva();
			case "5" -> tentativaController.listarTentativas();
			case "0" -> {
				System.out.println("tchau");
				return;
			}
			default -> System.out.println("opção inválida");
			}
		}
	}

	static void seed() {

		var prova = new Prova();
		prova.setId(BancoDeDadosTemp.proximaProvaId++);
		prova.setTitulo("Olimpíada 2026 • Nível 1 • Prova A");
		BancoDeDadosTemp.provas.add(prova);

		var q1 = new QuestaoTabuleiro();
		q1.setId(BancoDeDadosTemp.proximaQuestaoId++);
		q1.setProvaId(prova.getId());

		q1.setEnunciado("""
				Questão 1 — Mate em 1.
				É a vez das brancas.
				Encontre o lance que dá mate imediatamente.
				""");

		q1.setFenInicial("6k1/5ppp/8/8/8/7Q/6PP/6K1 w - - 0 1");

		q1.setAlternativas(new String[] { "A) Qh7#", "B) Qf5#", "C) Qc8#", "D) Qh8#", "E) Qe6#" });

		q1.setAlternativaCorreta('C');

		BancoDeDadosTemp.questoes.add(q1);
	}
}
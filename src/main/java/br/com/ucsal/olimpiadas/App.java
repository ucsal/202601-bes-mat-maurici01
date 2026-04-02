package br.com.ucsal.olimpiadas;

import br.com.ucsal.olimpiadas.controller.ParticipanteController;
import br.com.ucsal.olimpiadas.controller.ProvaController;
import br.com.ucsal.olimpiadas.controller.QuestaoController;
import br.com.ucsal.olimpiadas.model.*;
import br.com.ucsal.olimpiadas.repository.BancoDeDadosTemp;
import br.com.ucsal.olimpiadas.view.ConsoleView;
import br.com.ucsal.olimpiadas.view.QuestaoTabuleiro;


public class App {

	private static final ParticipanteController participanteController = new ParticipanteController();
	private static final ProvaController provaController = new ProvaController();
	private static final QuestaoController questaoController = new QuestaoController(provaController);

	static void main() {
		seed();

		while (true) {
			ConsoleView.exibirMenu();
			switch ( ConsoleView.pegaInput("")) {
			case "1" -> participanteController.cadastrarParticipante();
			case "2" -> provaController.cadastrarProva();
			case "3" -> questaoController.cadastrarQuestao();
			case "4" -> aplicarProva();
			case "5" -> listarTentativas();
			case "0" -> {
				System.out.println("tchau");
				return;
			}
			default -> System.out.println("opção inválida");
			}
		}
	}

	static void aplicarProva() {
		if (BancoDeDadosTemp.participantes.isEmpty()) {
			System.out.println("cadastre participantes primeiro");
			return;
		}
		if (BancoDeDadosTemp.provas.isEmpty()) {
			System.out.println("cadastre provas primeiro");
			return;
		}

		var participanteId = participanteController.escolherParticipante();
		if (participanteId == null)
			return;

		var provaId = provaController.escolherProva();
		if (provaId == null)
			return;

		var questoesDaProva = BancoDeDadosTemp.questoes.stream().filter(q -> q.getProvaId() == provaId).toList();

		if (questoesDaProva.isEmpty()) {
			System.out.println("esta prova não possui questões cadastradas");
			return;
		}

		var tentativa = new Tentativa();
		tentativa.setId(BancoDeDadosTemp.proximaTentativaId++);
		tentativa.setParticipanteId(participanteId);
		tentativa.setProvaId(provaId);

		System.out.println("\n--- Início da Prova ---");

		for (var q : questoesDaProva) {
			System.out.println("\nQuestão #" + q.getId());
			q.exibirParaAluno();

			String entrada = ConsoleView.pegaInput("Sua resposta (A–E): ");
			boolean acertou = q.verificaResposta(entrada);


			var r = new Resposta();
			r.setQuestaoId(q.getId());
			r.setCorreta(acertou);
			if(!entrada.isBlank()){
				r.setAlternativaMarcada(Character.toUpperCase(entrada.charAt(0)));
			}
			tentativa.getRespostas().add(r);
		}

		BancoDeDadosTemp.tentativas.add(tentativa);

		int nota = tentativa.calcularNota();
		System.out.println("\n--- Fim da Prova ---");
		System.out.println("Nota (acertos): " + nota + " / " + tentativa.getRespostas().size());
	}


	static void listarTentativas() {
		System.out.println("\n--- Tentativas ---");
		for (var t : BancoDeDadosTemp.tentativas) {
			System.out.printf("#%d | participante=%d | prova=%d | nota=%d/%d%n", t.getId(), t.getParticipanteId(),
					t.getProvaId(), t.calcularNota(), t.getRespostas().size());
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
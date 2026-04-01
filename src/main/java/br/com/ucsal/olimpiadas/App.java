package br.com.ucsal.olimpiadas;

import br.com.ucsal.olimpiadas.controller.ParticipanteController;
import br.com.ucsal.olimpiadas.model.*;
import br.com.ucsal.olimpiadas.repository.BancoDeDadosTemp;
import br.com.ucsal.olimpiadas.view.ConsoleView;
import br.com.ucsal.olimpiadas.view.QuestaoTabuleiro;


public class App {

	private static final ParticipanteController participanteController = new ParticipanteController();

	static void main() {
		seed();

		while (true) {
			ConsoleView.exibirMenu();
			switch ( ConsoleView.pegaInput("")) {
			case "1" -> participanteController.cadastrarParticipante();
			case "2" -> cadastrarProva();
			case "3" -> cadastrarQuestao();
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

	static void cadastrarProva() {
		var titulo = ConsoleView.pegaInput("Título da prova: ");

		if (titulo == null || titulo.isBlank()) {
			System.out.println("título inválido");
			return;
		}

		var prova = new Prova();
		prova.setId(BancoDeDadosTemp.proximaProvaId++);
		prova.setTitulo(titulo);

		BancoDeDadosTemp.provas.add(prova);
		System.out.println("Prova criada: " + prova.getId());
	}

	static void cadastrarQuestao() {
		if (BancoDeDadosTemp.provas.isEmpty()) {
			System.out.println("não há provas cadastradas");
			return;
		}

		var provaId = escolherProva();
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

	static void aplicarProva() {
		if (BancoDeDadosTemp.participantes.isEmpty()) {
			System.out.println("cadastre participantes primeiro");
			return;
		}
		if (BancoDeDadosTemp.provas.isEmpty()) {
			System.out.println("cadastre provas primeiro");
			return;
		}

		var participanteId = escolherParticipante();
		if (participanteId == null)
			return;

		var provaId = escolherProva();
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

	static Long escolherParticipante() {
		System.out.println("\nParticipantes:");
		for (var p : BancoDeDadosTemp.participantes) {
			System.out.printf("  %d) %s%n", p.getId(), p.getNome());
		}
		System.out.print("Escolha o id do participante: ");

		try {
			long id = Long.parseLong( ConsoleView.pegaInput(""));
			boolean existe = BancoDeDadosTemp.participantes.stream().anyMatch(p -> p.getId() == id);
			if (!existe) {
				System.out.println("id inválido");
				return null;
			}
			return id;
		} catch (Exception e) {
			System.out.println("entrada inválida");
			return null;
		}
	}

	static Long escolherProva() {
		System.out.println("\nProvas:");
		for (var p : BancoDeDadosTemp.provas) {
			System.out.printf("  %d) %s%n", p.getId(), p.getTitulo());
		}
		System.out.print("Escolha o id da prova: ");

		try {
			long id = Long.parseLong( ConsoleView.pegaInput(""));
			boolean existe = BancoDeDadosTemp.provas.stream().anyMatch(p -> p.getId() == id);
			if (!existe) {
				System.out.println("id inválido");
				return null;
			}
			return id;
		} catch (Exception e) {
			System.out.println("entrada inválida");
			return null;
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
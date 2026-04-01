package br.com.ucsal.olimpiadas;

import br.com.ucsal.olimpiadas.model.*;
import br.com.ucsal.olimpiadas.repository.BancoDeDadosTemp;
import br.com.ucsal.olimpiadas.view.TabuleiroView;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {

	/*
	* S – Single Responsibility Principle (SRP)
	* O – Open/Closed Principle (OCP)
	* L – Liskov Substitution Principle (LSP)
	* I – Interface Segregation Principle (ISP)
	* D – Dependency Inversion Principle (DIP)
	*/

	private static final Scanner in = new Scanner(System.in);

	public static void main(String[] args) {
		seed();


		// isso vai virar uma outra classe com um metodo chamado executar() 1
		while (true) {
			System.out.println("\n=== OLIMPÍADA DE QUESTÕES (V1) ===");
			System.out.println("1) Cadastrar participante");
			System.out.println("2) Cadastrar prova");
			System.out.println("3) Cadastrar questão (A–E) em uma prova");
			System.out.println("4) Aplicar prova (selecionar participante + prova)");
			System.out.println("5) Listar tentativas (resumo)");
			System.out.println("0) Sair");
			System.out.print("> ");

			switch (in.nextLine()) {
			case "1" -> cadastrarParticipante();
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

	//tem que sumir daqui, pode ser o mesmo nome porem em outro lugar algo do participante 2
	static void cadastrarParticipante() {
		System.out.print("Nome: ");
		var nome = in.nextLine();

		System.out.print("Email (opcional): ");
		var email = in.nextLine();

		if (nome == null || nome.isBlank()) {
			System.out.println("nome inválido");
			return;
		}

		var p = new Participante();
		p.setId(BancoDeDadosTemp.proximoParticipanteId++);
		p.setNome(nome);
		p.setEmail(email);

		BancoDeDadosTemp.participantes.add(p);
		System.out.println("Participante cadastrado: " + p.getId());
	}
	// no 3 eu preciso tambem ter algo relacionado a exibição
	//relacionado a criar novaProva 3
	static void cadastrarProva() {
		System.out.print("Título da prova: ");
		var titulo = in.nextLine();

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
	// relação com o 3
	static void cadastrarQuestao() {
		if (BancoDeDadosTemp.provas.isEmpty()) {
			System.out.println("não há provas cadastradas");
			return;
		}

		var provaId = escolherProva();
		if (provaId == null)
			return;

		System.out.println("Enunciado:");
		var enunciado = in.nextLine();

		var alternativas = new String[5];
		for (int i = 0; i < 5; i++) {
			char letra = (char) ('A' + i);
			System.out.print("Alternativa " + letra + ": ");
			alternativas[i] = letra + ") " + in.nextLine();
		}

		System.out.print("Alternativa correta (A–E): ");
		char correta;
		try {
			correta = Questao.normalizar(in.nextLine().trim().charAt(0));
		} catch (Exception e) {
			System.out.println("alternativa inválida");
			return;
		}

		var q = new Questao();
		q.setId(BancoDeDadosTemp.proximaQuestaoId++);
		q.setProvaId(provaId);
		q.setEnunciado(enunciado);
		q.setAlternativas(alternativas);
		q.setAlternativaCorreta(correta);

		BancoDeDadosTemp.questoes.add(q);

		System.out.println("Questão cadastrada: " + q.getId() + " (na prova " + provaId + ")");
	}


	// não sei onde fica, se vai ser mantido no 3 ou em outro
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
			System.out.println(q.getEnunciado());

			System.out.println("Posição inicial:");
			TabuleiroView.imprimirTabuleiroFen(q.getFenInicial());

			for (var alt : q.getAlternativas()) {
			    System.out.println(alt);
			}

			System.out.print("Sua resposta (A–E): ");
			char marcada;
			try {
				marcada = Questao.normalizar(in.nextLine().trim().charAt(0));
			} catch (Exception e) {
				System.out.println("resposta inválida (marcando como errada)");
				marcada = 'X';
			}

			var r = new Resposta();
			r.setQuestaoId(q.getId());
			r.setAlternativaMarcada(marcada);
			r.setCorreta(q.isRespostaCorreta(marcada));

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
			long id = Long.parseLong(in.nextLine());
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
			long id = Long.parseLong(in.nextLine());
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

	//uma classe para imprimit o tabuleiro


	//IniciarProva ou exibir prova
	static void seed() {

		var prova = new Prova();
		prova.setId(BancoDeDadosTemp.proximaProvaId++);
		prova.setTitulo("Olimpíada 2026 • Nível 1 • Prova A");
		BancoDeDadosTemp.provas.add(prova);

		var q1 = new Questao();
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
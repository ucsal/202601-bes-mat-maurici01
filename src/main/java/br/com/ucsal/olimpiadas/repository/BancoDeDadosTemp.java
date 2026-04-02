package br.com.ucsal.olimpiadas.repository;

import br.com.ucsal.olimpiadas.model.Participante;
import br.com.ucsal.olimpiadas.model.Prova;
import br.com.ucsal.olimpiadas.model.Questao;
import br.com.ucsal.olimpiadas.model.Tentativa;
import br.com.ucsal.olimpiadas.view.QuestaoTabuleiro;

import java.util.ArrayList;
import java.util.List;

public class BancoDeDadosTemp {
    public static long proximoParticipanteId = 1;
    public static long proximaProvaId = 1;
    public static long proximaQuestaoId = 1;
    public static long proximaTentativaId = 1;

    public static final List<Participante> participantes = new ArrayList<>();
    public static final List<Prova> provas = new ArrayList<>();
    public static final List<Questao> questoes = new ArrayList<>();
    public static final List<Tentativa> tentativas = new ArrayList<>();

    public static void seed() {

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

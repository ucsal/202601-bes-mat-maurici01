package br.com.ucsal.olimpiadas.view;

import br.com.ucsal.olimpiadas.model.Questao;

import java.util.Arrays;

public class QuestaoTabuleiro  extends Questao {

    private String[] alternativas = new String[5];
    private char alternativaCorreta;
    private String fenInicial;


    public String[] getAlternativas() {
        return alternativas;
    }

    public char getAlternativaCorreta() {
        return alternativaCorreta;
    }

    public void setAlternativaCorreta(char alternativaCorreta) {
        this.alternativaCorreta = normalizar(alternativaCorreta);
    }


    public String getFenInicial() {
        return fenInicial;
    }


    public void setFenInicial(String fenInicial) {
        this.fenInicial = fenInicial;
    }

    public void setAlternativas(String[] alternativas) {
        if (alternativas == null || alternativas.length != 5) {
            throw new IllegalArgumentException("A questão deve possuir exatamente 5 alternativas.");
        }
        this.alternativas = Arrays.copyOf(alternativas, 5);
    }

    public static char normalizar(char c) {
        char up = Character.toUpperCase(c);
        if (up < 'A' || up > 'E') {
            throw new IllegalArgumentException("Alternativa deve estar entre A e E.");
        }
        return up;
    }

    public boolean isRespostaCorreta(char marcada) {
        return normalizar(marcada) == alternativaCorreta;
    }

    @Override
    public void exibirParaAluno() {
        System.out.println("Enunciado: " + getEnunciado());
        System.out.println("Tabuleiro inicial (FEN): " + getFenInicial());
        TabuleiroView.imprimirTabuleiroFen(fenInicial);
        for(String alt: alternativas){
            System.out.println(alt);
        }

    }

    @Override
    public boolean verificaResposta(String entrada) {
        try {
            char marcado = Character.toUpperCase(entrada.trim().charAt(0));
            return marcado == alternativaCorreta;
        } catch (Exception e) {
            return false;
        }
    }
}

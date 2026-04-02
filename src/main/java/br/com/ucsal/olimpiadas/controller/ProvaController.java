package br.com.ucsal.olimpiadas.controller;

import br.com.ucsal.olimpiadas.model.Prova;
import br.com.ucsal.olimpiadas.repository.BancoDeDadosTemp;
import br.com.ucsal.olimpiadas.view.ConsoleView;

public class ProvaController {

    public void cadastrarProva() {
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

    public Long escolherProva() {
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
}

package br.com.ucsal.olimpiadas.controller;

import br.com.ucsal.olimpiadas.model.Participante;
import br.com.ucsal.olimpiadas.repository.BancoDeDadosTemp;
import br.com.ucsal.olimpiadas.view.ConsoleView;

public class ParticipanteController {

    public  void cadastrarParticipante() {
        var nome = ConsoleView.pegaInput("Nome: ");

        var email = ConsoleView.pegaInput("Email (opcional): ");

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

    public Long escolherParticipante() {
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
}

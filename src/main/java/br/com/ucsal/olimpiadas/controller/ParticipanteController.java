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
}

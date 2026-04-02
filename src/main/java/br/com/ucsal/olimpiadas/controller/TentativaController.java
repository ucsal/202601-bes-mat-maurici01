package br.com.ucsal.olimpiadas.controller;

import br.com.ucsal.olimpiadas.model.Resposta;
import br.com.ucsal.olimpiadas.model.Tentativa;
import br.com.ucsal.olimpiadas.repository.BancoDeDadosTemp;
import br.com.ucsal.olimpiadas.view.ConsoleView;

public class TentativaController {

    private final ParticipanteController participanteController;
    private final ProvaController provaController;

    public TentativaController(ParticipanteController participanteController, ProvaController provaController) {
        this.participanteController = participanteController;
        this.provaController = provaController;
    }

    public void aplicarProva() {
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

    public void listarTentativas() {
        System.out.println("\n--- Tentativas ---");
        for (var t : BancoDeDadosTemp.tentativas) {
            System.out.printf("#%d | participante=%d | prova=%d | nota=%d/%d%n", t.getId(), t.getParticipanteId(),
                    t.getProvaId(), t.calcularNota(), t.getRespostas().size());
        }
    }
}

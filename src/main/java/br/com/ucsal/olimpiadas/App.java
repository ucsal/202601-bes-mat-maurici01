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
		BancoDeDadosTemp.seed();

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

}
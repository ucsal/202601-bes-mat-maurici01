Aplicando o primeiro principio SOLID (Princípio da Responsabilidade Única). <p>
Alterado o imprimirTabuleiroFen, que antes estava no App, agora foi colocado em uma View, repeitando o primeiro principio do SOLID. <p>
Alterado o calcularNota para a classe Tentativa.
Alterado a parte que imprime o tabuleiro, retirado do main e colocado no ConsoleView. <p>
Alterado o cadastrarParticipante para o participanteController<p>
Alterado tambem o App e removido o cadastrarParticipante, agora eu tenho uma instancia do meu participanteController<p>
Alterado os metodos cadastrarProva e escolherProva para o provaController<p>
Alterado escolherParticipante() para o participanteController<p>
Alterado o cadastrarQuestao() para o questaoController<p>
Alterado o listarTentativas e aplicarProva para o TentativaController<p>
------------------------------------------------------------<p>
Aplicando o segundo princípio SOLID (Princípio Aberto/Fechado). <p>
Alterado a classe Questao para abstract e criado os metodos exibirAluno e verificarResposta. <p>
Criada a classe QuestaoTabuleiro que herda de Questao e implementa exibirAluno() e verificaResposta e Refatorado o metodo aplicarProva() <p>
movido o seed() para o BancoDeDadosTemp, para atender ao principio aberto fechado<p>
Implementado a inteface para trazer uma generalização e uma especificação melhor, exemplo se no futuro tivermos questões de verdadeiro ou falso, ou questão discursiva ela pode ser implementada.<p>
-----------------------------------------<p>
Aplicando o terceiro princípio SOLID (Princípio da Substituição de Liskov). <p>
Como já veio questões genericas eu acredito que ao implementar o resto esse principio já foi atendido.<p>
-----------------------------------------<p>
Aplicando o quarto princípio SOLID (Princípio da Segregação de Interface). <
A interface CadastroQuestao atende o principio da segregação de interface, pois ela é específica para o cadastro de questões <p>
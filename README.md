# 			INÍCIO RELATÓRIO FINAL 			#

# RESUMO: #
	No compilador construído, todas as fases propostas ao longo da disciplina foram implementadas com sucesso, isto é, utilizando 
	os oito exemplos disponibilizados na página do livro texto para testar o código desenvolvido, em todos eles o compilador 
	implementado funcionou. Além disso, criamos um novo teste para complementar os testes disponibilizados. Com isso, em todas as
	fases foram realizados 9 testes e, no final, todas as fases funcionaram em todos os testes. A classe Main do pacote 
	devel.main_test centraliza os testes disponíveis e a chamada para cada uma das fases implementadas.

# FASES DETALHADAS: #

# • • • ORGANIZAÇÃO DO FRAMEWORK • • • #

# O que foi feito: #
	A partir do site fornecido pelo professor, foi realizado o download de todos os arquivos que estão disponíveis no site. 
	Posteriormente, criamos o projeto e tentamos retirar os erros que apareceram. Além disso, organizamos os arquivos em pacotes,
	tentamos melhorar uma parte do código também e adicionamos os testes disponíveis.

# Como foi feito: #
	Aqui não tem muito o que falar. Utilizamos a IDE Eclipse para realizar a programação. Os dois principais pacotes criados 
	foram o core e develop. Basicamente, o core serão os arquivos do framework e, portanto, tentaremos não modificar o core e
	utilizaremos o develop para centralizar as nossas programações.

# Dificuldades e aprendizado: #
	A organização do nome das classes apresentava um erro em sistemas  operacionais que não eram sensíveis à capitalização, 
	logo tivemos de realizar algumas leves mudanças no nome das classes.


# • • • ANÁLISE LÉXICA • • • #

# O que foi feito: #
	A fase de análise léxica foi totalmente implementada utilizando javaCC.

# Como foi feito: #
	Baseado nos tokens da linguagem MiniJava, foram criadas expressões regulares para reconhecer cada um dos tokens. 
	Para testar essa e outras funcionalidades, criamos a classe Main no pacote devel.main_test com todos os casos de testes.

# Dificuldades e aprendizado: #
	Entender o funcionamento e a sintaxe do javaCC.


# • • • ANÁLISE SINTÁTICA • • • #

# O que foi feito: #
	A fase de análise sintática foi totalmente implementada no javaCC.

# Como foi feito: #
	A partir da gramática da linguagem MiniJava apresentada no livro texto, realizamos a implementação de métodos/funções que
	são responsáveis por realizar a análise sintática no javaCC (é um parser LL(k)). Utilizando-se das ações semânticas, 
	implementamos a criação da árvore sintática abstrata disponibilizada no framework enquanto que a análise sintática é 
	realizada. Ademais, realizamos a eliminação de recursão à esquerda e ambiguidades que a gramática apresentava.

# Dificuldades e aprendizado: #
	Organização foi um ponto importante nessa etapa, assim como o bom uso das classes fornecidas a nós pelo framework para 
	criarmos a árvore sintática abstrata. Tivemos de ter cuidado ao manejar a criação dos objetos a partir das informações 
	que passavam pela gramática. Além disso, encontramos algumas dificuldades em entender como funciona o LOOKAHEAD do javaCC. 
	Foi dificultoso também retirar as ambiguidades e recursão à esquerda da gramática.

# • • • ANÁLISE SEMÂNTICA • • • #

# O que foi feito: #
	Implementamos a análise semântica como apresentada no livro texto, onde a mesma é dividida em dois passos: o primeiro é 
	responsável por construir a tabela de símbolos e o segundo por verificar se os tipos/declarações conferem.

# Como foi feito: #
	Primeiro, fizemos a implementação de uma classe Table para representar uma tabela de símbolos genérica e as classes
	ProgramTable, ClassTable, MethodTable para representar as tabelas de programa, classes e métodos, respectivamente. 
	Posteriormente, foram criados dois visitors para percorrer a árvore sintática abstrata criada na fase anterior, sendo
	que um visitor para criar e preencher a tabela de símbolos enquanto percorre a árvore e outro que percorre novamente 
	toda a árvore afim de checar se uma variável é declarada uma única vez, se o seu tipo está correto quando a variável 
	é utilizada e se uma variável foi declarada anteriormente quando a mesma é utilizada. Se algum desses pontos não for 
	verdade, um erro semântico será lançado. Nesta fase optamos por criar um novo visitor, diferente dos dois 
	disponibilizados pelo framework, pois achamos que seria mais intuitivo criarmos a tabela de símbolos utilizando uma 
	abordagem recursiva, ao invés de utilizarmos o visitor que retorna void, como seria esperado.

# Dificuldades e aprendizado: #
	A dificuldade encontrada nessa fase foi entender como criar os visitors para percorrer o programa e criar as tabelas 
	e posteriormente utilizar essas tabelas no outro visitor para realizar a verificação. No final, preferimos utilizar 
	o caminho da recursão que facilitou o nosso entendimento.


# • • • TRADUÇÃO PARA IR • • • #

# O que foi feito: #
	Utilizando-se do padrão Visitor, percorremos a árvore sintática abstrata criada na análise sintática e realizamos a
	tradução para a representação intermediária presente no framework disponibilizado. Além disso, acoplamos ao nosso
	projeto o MipsFrame que também foi disponibilizado.

# Como foi feito: #
	Um novo padrão Visitor foi criado para implementar esta fase. Em cada construção da árvore sintática abstrata foi
	criado um método para realizar a tradução, sendo que nem todas as construções da árvore sintática abstrata precisavam
	realmente serem traduzidas e nas construções que envolviam métodos a classe Frame se fez necessária 
	(método procEntryExit1). Para esses casos, realizamos algumas modificações no MipsFrame para que os erros fossem 
	consertados e pudéssemos utilizá-lo. Realizado as traduções necessárias, no final dessa etapa, tínhamos uma lista
	de Fragmentos, sendo que cada Fragmento é composto do frame e a árvore IR correspondente a cada um dos métodos do
	programa fonte.

# Dificuldades e aprendizado: #
	Nessa etapa tivemos dificuldades em entender o papel e a funcionalidade do MipsFame e em qual momento iríamos utilizar
	o Frame no Visitor criado. Além disso, o MipsFrame estava totalmente diferente da estrutura apresentada no framework.
	Por exemplo, no framework listas são representadas por head e tail, enquanto que no MipsFrame era utilizado listas do
	pacote java.util. Assim, um esforço foi necessário para acoplar o MipsFrame com sucesso no nosso projeto. Ademais,
	outras dificuldades também nos fizeram aprender, por exemplo traduzir comandos de alto nível em comandos de 
	médio/baixo nível.


# • • • GERAÇÃO DA IR CANÔNICA • • • #

# O que foi feito: #
	Foi realizado a etapa de tradução da árvore IR para árvore IR canônica.

# Como foi feito: #
	A partir da lista de Fragmentos criados na fase anterior, aplicamos o método linearize presente na classe Canon
	disponibilizada no framework. Este método gera a árvore IR canônica a partir de uma árvore IR.

# Dificuldades e aprendizado: #
	Nenhuma dificuldade, desde que toda a transformação estava encapsulada no método linearize disponibilizado pelo framework.

# • • • GERAÇÃO DOS BLOCOS BÁSICOS E TRAÇOS • • • #

# O que foi feito: #
	A partir da árvore IR canônica, geramos os blocos básicos e os traços do programa fonte.

# Como foi feito: #
	Como na fase anterior, todas as implementações necessárias para essa fase já se encontrava no framework, só fizemos utilizar.
	Para tanto, utilizamos a classe BasicBlocks para criar os blocos básicos a partir da árvore IR canônica. Posteriormente, 
	aplicamos os blocos básicos na classe TraceSchedule para gerarmos os traços.

# Dificuldades e aprendizado: #
	Novamente, nenhuma dificuldade desde que o necessário para esta fase já estava implementado no framework.


# • • • SELEÇÃO DE INSTRUÇÕES/LADRILHAMENTO • • • #

# O que foi feito: #
	Foi implementado o método Maximal Munch de acordo com o apresentado no livro texto para as instruções da arquitetura Mips.

# Como foi feito: #
	Primeiro, precisamos entender bem o funcionamento do algoritmo Maximal Munch. Após esse entendimento, tivemos que pesquisar
	e entender quais eram as instruções do Mips, como elas funcionavam. Posteriormente, foi realizado o entendimento da 
	estrutura disponibilizada pelo framework para esta fase e depois começamos a implementar o algoritmo Maximal Munch e, de 
	acordo com a necessidade, procurávamos as instruções correspondentes em Mips. A estrutura aqui utilizada foi a mesma 
	apresentada no livro e que também estava presente na classe Print disponibilizada no framework. Ademais, fizemos uso dos 
	métodos procEntryExit2 e procEntryExit3 disponibilizados no Frame de acordo com o exposto no livro texto.

# Dificuldades e aprendizado: #
	Nesta fase, encontramos grande dificuldade devido ao fato de lidar com instruções de baixo nível e realmente lidar com 
	registradores, moves, etc. Ademais, o primeiro contato com a arquitetura Mips também foi bastante dificultosa, pois esta
	arquitetura é bastante ampla e possui variadas versões. Todavia, o MipsFrame nos ajudou a entender algumas partes, como 
	exemplo quais registradores existem no Mips, etc.


# • • • GERAÇÃO DO GRAFO DE FLUXO DE CONTROLE • • • #

# O que foi feito: #
	Criamos o grafo de fluxo, a partir de uma lista de instruções, de modo que cada instrução tornou-se um nó do grafo e sempre
	que existe a possibilidade de chegar na instrução b a partir da a, criamos uma aresta ab. 

# Como foi feito: #
	Inicialmente criamos uma classe AssemFlowGraph, como o livro sugere, que extends da classe abstrata FlowGraph, que,
	por sua vez, extends da classe abstrata Graph. Essa implementação da classe FlowGraph foi interessante pois dessa forma
	não tivemos que nos preocupar com os detalhes da estrutura de dados grafo, pois a classe Graph já nos dava o suporte
	necessário, com métodos para adicionar um novo nó e uma nova aresta. 

# Dificuldades e aprendizado: #
	Uma dificuldade nessa etapa foi quanto ao label de uma instrução para podermos adicionar uma aresta ab quando a instrução
	a possui um jump para a instrução b. Inicialmente não tínhamos percebido que existia uma instrução especial Label e que o
	jump é sempre feito para esse tipo de instrução. 

# • • • GERAÇÃO DO GRAFO DE INTERFERÊNCIA/LONGEVIDADE • • • #

# O que foi feito: #
	Utilizamos o grafo de fluxo de controle para fazermos a etapa de análise de longevidade e, com isso, construir o grafo de
	interferência do programa.

# Como foi feito: #
	Nesta etapa optamos por uma abordagem alternativa à proposta pelo livro. Ao invés de utilizarmos a estrutura pré-definida
	do grafo, optamos por construir nossa própria abstração pois, dessa forma, achamos mais fácil de implementar o que era proposto. Primeiramente, utilizamos o algoritmo proposto pelo livro para calcular os LiveIn e LiveOut de cada nó do grafo, em seguida, adicionamos uma interferência entre um par de nós  a,b sempre que a,b ocorrerem juntos em uma mesma aresta de LiveIn ou de LiveOut.

# Dificuldades e aprendizado: #
	Quanto a representação do grafo tivemos dificuldade em como representar os nós de maneira compatível com aglutinação,
	uma vez que um nó gerado por aglutinação não é um temporário. Para resolver utilizamos a representação em string e,
	em aglutinações, utilizamos um caractere especial para separar os temporários em um mesmo nó. Outra dificuldade foi a 
	persistência das interferências e dos moves no grafo após cada inserção e remoção de nós pois, uma vez que o grafo não
	é direcionado, tivemos de guardar cada aresta 2 vezes, uma em cada sentido. Não foi difícil de ser resolvido, apenas 
	um detalhe laborioso.


# • • • SELEÇÃO DE REGISTRADORES/COLORAÇÃO • • • #

# O que foi feito: #
	Utilizamos o grafo de interferência para, a partir de sua análise, escolher quais temporários serão armazenados em
	cada registrador.

# Como foi feito: #
	Optamos por uma abordagem semelhante a do livro, utilizando pilhas para guardar os nós removidos do grafo. 
	Iniciamos o código pré-colorindo os registradores com suas respectivas cores. Em seguida ficaremos manipulando uma cópia
	o grafo de interferência (pois precisaremos do original para a etapa de coloração) até que sobrem apenas nós que representam
	registradores (em casos de aglutinação, nós que contenham registradores). Primeiramente, para cada nó, tentamos simplificá-lo.
	Caso não seja possível, tentamos aglutiná-lo. Não havendo nenhuma dessas operações ocorrido, obteremos o nó move-related de
	menor grau para aplicar freeze. Em último caso, quando não há um nó move-related, removemos como um transbordamento em
	potencial o nó de maior grau. Uma vez terminada essa análise, desempilhamos os nós e escolhemos a menor cor disponível para ele. Em caso de transbordamento real, armazenamos os nós transbordantes em uma pilha e, no final, reescrevemos a lista de instruções adicionando instruções de SAVE e LOAD da memória antes das definições e usos e, em seguida, refazemos toda a etapa da seleção de registradores.

# Dificuldades e aprendizado: #
	A maior dificuldade foi o tratamento do transbordamento real. Reescrever o código não era algo tão simples de se implementar,
	mas depois de analisar toda a teoria e as dicas do livro, conseguimos fazê-lo e de uma maneira não tão custosa. 
	Outra dificuldade foi implementar o pipeline de execução das operações sobre o grafo de interferência (simplificação, 
	aglutinação, freeze e transbordamento em potencial). Uma vez montado, pensamos nas heurísticas descritas para seleção de nó 
	para aplicar freeze e para transbordar, de maneira que a chance de haver uma simplificação na etapa seguinte fosse maior.
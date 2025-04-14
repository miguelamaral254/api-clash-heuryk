API para analise de clashroyale.

Feita em Java com Springboot.

Ultilizamos MongoDB para o armazenamento de dados.
________________________________________________________________________

Documentação da API de Batalhas

POST /api/battles

Cria uma nova batalha.

Requisição:
•	Método: POST
•	Endpoint: {{base_url}}/api/battles

Payload:

{
"timestamp": "2025-04-11T16:30:00Z",
"player1": {
"nickname": "JogadorTres",
"trophies": 3100,
"level": 12,
"towersDestroyed": 1,
"deck": [
"Cannon",
"Electro Wizard",
"Fireball",
"Golem",
"Lightning",
"Mega Minion",
"P.E.K.K.A",
"Tornado"
]
},
"player2": {
"nickname": "JogadorQuatro",
"trophies": 2900,
"level": 11,
"towersDestroyed": 2,
"deck": [
"Baby Dragon",
"Electro Wizard",
"Fireball",
"Golem",
"Tesla",
"Zap",
"Tornado",
"Mega Minion"
]
},
"winner": "player1"
}



⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻


GET /api/battles

Busca as batalhas no banco de dados, com paginação.

Requisição:
•	Método: GET
•	Endpoint: {{base_url}}/api/battles?page=0&size=17&sort=timestamp,desc

Parâmetros:
•	page: (opcional) Número da página para a paginação (default: 0)
•	size: (opcional) Número de itens por página (default: 17)
•	sort: (opcional) Critério de ordenação, por exemplo, timestamp,desc para ordenar pela data de criação em ordem decrescente.

⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻


GET /api/battles/stats

Calcula a porcentagem de vitórias e derrotas utilizando uma carta específica, ocorridas em um intervalo de timestamps.

Requisição:
•	Método: GET
•	Endpoint: {{base_url}}/api/battles/stats?start=2025-04-11T00:00:00Z&end=2025-04-11T23:59:59Z&cardName=Baby%20Dragon

Parâmetros:
•	start: (obrigatório) Data de início do intervalo (ex: 2025-04-11T00:00:00Z)
•	end: (obrigatório) Data de fim do intervalo (ex: 2025-04-11T23:59:59Z)
•	cardName: (obrigatório) Nome da carta a ser analisada (ex: Baby Dragon)

Resposta (Payload):

{
"totalMatches": 10,
"totalWins": 6,
"totalLosses": 4,
"winPercentage": 60.0,
"lossPercentage": 40.0
}



⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻


GET /api/battles/decks/winrates

Lista decks que produziram mais de X% de vitórias em um intervalo de timestamps.

Requisição:
•	Método: GET
•	Endpoint: {{base_url}}/api/battles/decks/winrates?start=2025-04-01T00:00:00Z&end=2025-04-30T23:59:59Z&minWinRate=10&page=0&size=10&sort=timestamp,desc

Parâmetros:
•	start: (obrigatório) Data de início do intervalo (ex: 2025-04-01T00:00:00Z)
•	end: (obrigatório) Data de fim do intervalo (ex: 2025-04-30T23:59:59Z)
•	minWinRate: (obrigatório) A porcentagem mínima de vitórias para o deck (ex: 10)
•	page: (opcional) Número da página para a paginação (default: 0)
•	size: (opcional) Número de itens por página (default: 10)
•	sort: (opcional) Critério de ordenação, por exemplo, timestamp,desc para ordenar pela data de criação em ordem decrescente.

Resposta (Payload):

{
"content": [
{
"deck": [
"Baby Dragon",
"Electro Dragon",
"Elixir Collector",
"Golem",
"Inferno Tower",
"Mini P.E.K.K.A",
"Tornado",
"Zap"
],
"totalMatches": 1,
"totalWins": 1,
"winPercentage": 100.0
},
{
"deck": [
"Cannon",
"Electro Wizard",
"Fireball",
"Golem",
"Mega Minion",
"P.E.K.K.A",
"Tornado",
"Zap"
],
"totalMatches": 1,
"totalWins": 1,
"winPercentage": 100.0
}
]
}



⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻


GET /api/battles/defeats

Calcula a quantidade de derrotas utilizando um combo de cartas específico, ocorridas em um intervalo de timestamps.

Requisição:
•	Método: GET
•	Endpoint: {{base_url}}/api/battles/defeats?start=2025-04-10T00:00:00Z&end=2025-04-11T23:59:59Z&cardCombo=Baby%20Dragon,Golem,Lightning,Fireball,Tornado,P.E.K.K.A,Elixir%20Collector,Mega%20Minion

Parâmetros:
•	start: (obrigatório) Data de início do intervalo (ex: 2025-04-10T00:00:00Z)
•	end: (obrigatório) Data de fim do intervalo (ex: 2025-04-11T23:59:59Z)
•	cardCombo: (obrigatório) Lista das cartas que fazem parte do combo (ex: Baby Dragon,Golem,Lightning,Fireball,Tornado,P.E.K.K.A,Elixir Collector,Mega Minion)

Resposta (Payload):

2



⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻


GET /api/battles/victories

Calcula a quantidade de vitórias envolvendo uma carta específica, onde o vencedor possui menos troféus que o perdedor, a partida durou menos de 2 minutos, e o perdedor derrubou pelo menos duas torres.

Requisição:
•	Método: GET
•	Endpoint: {{base_url}}/api/battles/victories?start=2025-04-10T00:00:00Z&end=2025-04-11T23:59:59Z&cardCombo=Mega%20Minion&trophyPercentage=10

Parâmetros:
•	start: (obrigatório) Data de início do intervalo (ex: 2025-04-10T00:00:00Z)
•	end: (obrigatório) Data de fim do intervalo (ex: 2025-04-11T23:59:59Z)
•	cardCombo: (obrigatório) Nome da carta envolvida no combo (ex: Mega Minion)
•	trophyPercentage: (obrigatório) Percentual de diferença de troféus entre o vencedor e o perdedor (ex: 10)

Resposta (Payload):

2



⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻

GET /api/battles/combo-stats

Lista combos de cartas que produziram mais de X% de vitórias em um intervalo de timestamps.

Requisição:
•	Método: GET
•	Endpoint: {{base_url}}/api/battles/combo-stats?start=2025-04-11T00:00:00Z&end=2025-04-11T23:59:59Z&deckSize=8&comboSize=6&minWinPercentage=90

Parâmetros:
•	start: (obrigatório) Data de início do intervalo (ex: 2025-04-11T00:00:00Z)
•	end: (obrigatório) Data de fim do intervalo (ex: 2025-04-11T23:59:59Z)
•	deckSize: (obrigatório) Tamanho do deck (ex: 8)
•	comboSize: (obrigatório) Tamanho do combo de cartas (ex: 6)
•	minWinPercentage: (obrigatório) Percentual mínimo de vitórias para o combo (ex: 90)

Resposta (Payload):

{
"content": [
{
"combo": [
"Baby Dragon",
"Lightning",
"Fireball",
"Tornado",
"P.E.K.K.A",
"Elixir Collector"
],
"totalMatches": 1,
"totalWins": 1,
"totalLosses": 0,
"winPercentage": 100.0
},
{
"combo": [
"Golem",
"Baby Dragon",
"Lightning",
"Fireball",
"Tornado",
"P.E.K.K.A"
],
"totalMatches": 1,
"totalWins": 1,
"totalLosses": 0,
"winPercentage": 100.0
}
]
}



______________________________________________________


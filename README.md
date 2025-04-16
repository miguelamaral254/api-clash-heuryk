
# Clash Royale Battle Analysis API

Esta é uma API desenvolvida em **Java com Spring Boot** que realiza análises sobre batalhas do jogo **Clash Royale**, com dados armazenados em **MongoDB**.  
O objetivo é possibilitar consultas analíticas que ajudem a compreender o desempenho de cartas, decks e jogadores — contribuindo para o balanceamento do jogo.

---

## Tecnologias Utilizadas

- **Java 21**
- **Spring Boot**
- **MongoDB (com Spring Data)**
- **Postman** (para testes)
- **Excalidraw** (Modelagem)

---

## Estrutura do Projeto

Cada batalha armazenada no MongoDB contém:

- Dados dos dois jogadores (nickname, troféus, nível, deck, torres destruídas)
- Timestamp da partida

---

## Como Executar

Requisitos: Precisa ter instalado no computador o Java 21, MongoDB 

- Clone a API 
- Em application.properties coloque a URL do seu Banco de Dados MongoDB
- Execute a aplicação

---

## Como Utilizar

### Criar batalha

`POST /api/battles`

Cria uma nova batalha no banco de dados.

```json
{
  "timestamp": "2025-04-11T16:30:00Z",
  "player1": {
    "nickname": "JogadorTres",
    "trophies": 3100,
    "level": 12,
    "towersDestroyed": 1,
    "deck": ["Cannon", "Electro Wizard", ...]
  },
  "player2": {
    "nickname": "JogadorQuatro",
    "trophies": 2900,
    "level": 11,
    "towersDestroyed": 2,
    "deck": ["Baby Dragon", "Fireball", ...]
  }
}
```

---

### Listar batalhas com paginação

`GET /api/battles?page=0&size=17&sort=timestamp,desc`

---

### Estatísticas por carta

`GET /api/battles/stats?start=2025-04-11T00:00:00Z&end=2025-04-11T23:59:59Z&cardName=Baby%20Dragon`

Calcula vitórias e derrotas com uma carta específica em um intervalo de tempo.

---

### Decks com alta taxa de vitória

`GET /api/battles/decks/winrates?start=2025-04-01T00:00:00Z&end=2025-04-30T23:59:59Z&minWinRate=10&page=0&size=10&sort=timestamp,desc`

Retorna decks com taxa de vitória acima de X% no período.

---

### Derrotas com combo de cartas

`GET /api/battles/defeats?start=2025-04-10T00:00:00Z&end=2025-04-11T23:59:59Z&cardCombo=Baby%20Dragon,Golem,Lightning,Fireball,Tornado,P.E.K.K.A,Elixir%20Collector,Mega%20Minion`

Conta derrotas com um combo de cartas específico.

---

### Vitórias com restrições

`GET /api/battles/victories?start=2025-04-10T00:00:00Z&end=2025-04-11T23:59:59Z&cardCombo=Mega%20Minion&trophyPercentage=10`

Calcula vitórias usando uma carta quando:
- O vencedor tem menos troféus que o perdedor
- A partida durou menos de 2 minutos
- O perdedor destruiu 2 torres

---

### Combos com alta winrate

`GET /api/battles/combo-stats?start=2025-04-11T00:00:00Z&end=2025-04-11T23:59:59Z&deckSize=8&comboSize=6&minWinPercentage=90`

Lista combinações de cartas que tiveram alta taxa de vitória no período.

---

### Decks com winrate por Arena

`GET /api/battles/decks/winrates/arena?start=2025-04-01T00:00:00Z&end=2025-04-30T23:59:59Z&minWinRate=10&arena=ARENA_1&page=0&size=10&sort=winPercentage,desc`

Filtra decks com winrate acima de X%, segmentado por arena (baseado em troféus).

Exemplo de arena:
```
ARENA_1 (0 a 300 troféus)
ARENA_10 (3000 a 3400 troféus)
...
ARENA_23 (9000+ troféus)
```

---

### Cartas mais usadas em decks vitoriosos

`GET /api/battles/decks/winrates/mostFrequentCards?start=2025-04-01T00:00:00Z&end=2025-04-30T23:59:59Z&page=3&size=3`

Retorna as cartas mais frequentes em decks com vitórias no período analisado.

```json
{
  "card": "Lightning",
  "count": 7
}
```

---

## Contribuidores

- Desenvolvido por Miguel Amaral, Júlio Cesar, Weslley Santana, Lucas Eduardo, Carlos Rocha, Larissa, João Pedro.

---

## Licença

Este projeto é de uso acadêmico.


# Assessment para consultores e desenvolvedores Backend - IBM Brasil

### Como rodar o código
Baixe o código fonte, utilizando o Eclipse importe-o e execute a classe ReservaApplication.

### Como visualizar os endpoints e testá-los utilizando o SWAGGER:
Após executar o código, abra seu navegador na seguinte URL:
http://localhost:8080/swagger-ui.html

### Pré-requisitos:
Java

#### Banco utilizado em memória [H2](https://www.h2database.com/html/main.html)


___________________________________________________________________________________________________________________

## Descrição da Tarefa

### Visão geral

Esse documento é um exercício que tem como objetivo avaliar o nível de conhecimento e gaps a serem preenchidos para profissionais de Consultoria ou Desenvolvimento que trabalham ou desejam trabalhar com backend.
O output desse assessment é um repositório no git contendo o código que reflete os requisitos especificados.


### Exercício

O exercício consiste em criar o backend de um sistema de marcação e desmarcação de reservas em um clube de tênis.
Timebox esperado de resolução desse exercício: 6 a 12 horas.
Abaixo, seguem as orientações para o desenvolvimento. Lembrando que elas serão a base estrutural para a avaliação.
Por simplicidade, não é necessário conexão com base de dados, os dados podem ser salvos em arquivos ou em memória.

### Questão 1 – CRUD, modelo e verbos HTTP

Implementar um CRUD via requisições HTTP no padrão, seguindo uma interface pré-definida.

#### Criar reserva
Um serviço externo deve conseguir criar uma nova reserva passando como parâmetro a data de início e fim e o tipo de quadra desejada, seguindo a interface abaixo.

```
POST /reservas
Request:
body={
  “tipo”: “SAIBRO”,				(string-enum)
  “inicioEm”: “2018-05-30T18:00:00Z”,	(string-ISOdate)
  “fimEm”: “2018-05-30T19:00:00Z”,	(string-ISOdate)
}
Response:
statusCode=200
body={
  “tipo”: “SAIBRO”,				(string-enum)
  “inicioEm”: “2018-05-30T18:00:00Z”,	(string-ISOdate)
  “fimEm”: “2018-05-30T19:00:00Z”,	(string-ISOdate)
  “id”: “20180428924R1L10000”,		(string-ID)
  “status”: “ativa”,			(string-enum)
  “criadoEm”: “2018-05-29T16:00:00Z”	(string-ISOdate)
}
```

Os campos em roxo devem ser gerados pela API e retornados para o chamador, usando valor autogerado para o id, default ("ativa") para o status e dafault de data atual (new Date()) para o criadoEm, ignorando valores caso inputados pelo usuário.

#### Buscar reserva por identificador
Um serviço externo deve conseguir consultar os dados da sua reserva a partir do código de reserva.

```
GET /reservas/{id}
Response:
statusCode=200
body={
  “id”: “20180428924R1L10000”,		(string-ID)
  “tipo”: “SAIBRO”,				(string-enum)
  “status”: “ativa”,			(string-enum)
  “criadaEm”: “2018-05-29T18:01:10Z”,	(string-ISOdate)
  “inicioEm”: “2018-05-30T18:00:00Z”,	(string-ISOdate)
  “fimEm”: “2018-05-30T19:00:00Z”,	(string-ISOdate)
}
```

#### Alterar reserva
Um serviço externo deve conseguir alterar o tipo de quadra e horário da reserva.

```
PUT /reservas/{id}
Request:
body={
  “tipo”: “SAIBRO”,				(string-enum)
  “inicioEm”: “2018-05-30T19:00:00Z”,	(string-ISOdate)
  “fimEm”: “2018-05-30T20:00:00Z”,	(string-ISOdate)
}
Response:
statusCode=200
body={
  “id”: “20180428924R1L10000”,		(string-ID)
  “tipo”: “SAIBRO”,				(string-enum)
  “status”: “ativa”,			(string-enum)
  “criadaEm”: “2018-05-29T18:01:10Z”,	(string-ISOdate)
  “inicioEm”: “2018-05-30T18:00:00Z”,	(string-ISOdate)
  “fimEm”: “2018-05-30T19:00:00Z”,	(string-ISOdate)
}
```

#### Deletar reserva
Um serviço externo deve conseguir cancelar uma reserva a partir do código de reserva.

```
DELETE /reservas/{id}
Response:
statusCode=200
```

#### Buscar lista de reservas
Um serviço externo deve conseguir consultar todas as reservas.

```
GET /reservas
Response:
statusCode=200
body=[
{
  “id”: “20180428924R1L10000”,		(string-ID)
  “tipo”: “SAIBRO”,				(string-enum)
  “status”: “ativa”,			(string-enum)
  “criadaEm”: “2018-05-29T18:01:10Z”,	(string-ISOdate)
  “inicioEm”: “2018-05-30T18:00:00Z”,	(string-ISOdate)
  “fimEm”: “2018-05-30T19:00:00Z”,	(string-ISOdate)
}
]
```

### Questão 2 – Regras de negócio

#### Valores default e calculados
O cliente agora quer que você inclua já o valor e duração da reserva em minutos no email de confirmação, necessitamos adicionar esses campos no retorno da inclusão de reserva.
A duração deve ser o fimEm menos o inicioEm em minutos e o valor deve ser R$0.50 por minuto.

```
POST /reservas
Request:
body={
  “tipo”: “SAIBRO”,				(string-enum)
  “inicioEm”: “2018-05-30T18:00:00Z”,	(string-ISOdate)
  “fimEm”: “2018-05-30T19:00:00Z”,	(string-ISOdate)
}
Response:
statusCode=200
body={
  “id”: “20180428924R1L10000”,		(string-ID)
  “tipo”: “SAIBRO”,				(string-enum)
  “status”: “ativa”,			(string-enum)
  “criadaEm”: “2018-05-29T18:01:10Z”,	(string-ISOdate)
  “inicioEm”: “2018-05-30T18:00:00Z”,	(string-ISOdate)
  “fimEm”: “2018-05-30T19:00:00Z”,	(string-ISOdate)
  “duracao”: 60				(int),
  “valor”: 30.00				(float)
}
```

#### Deleção lógica e override
Não obstante, ele nos notificou que muitos clientes estão reclamando que suas reservas estão desaparecendo do sistema do nada e, por isso, precisamos adicionar uma forma para os usuários verem o histórico também dos cancelamentos.
Precisamos então fazer o cancelamento lógico das reservas ao invés de apagá-las da base de dados para sempre e adicionar a data de cancelamento na interface.

```
DELETE /reservas/{id}
Response:
statusCode=200
body={
  “id”: “20180428924R1L10000”,		(string-ID)
  “tipo”: “SAIBRO”,				(string-enum)
  “status”: “cancelada”,			(string-enum)
  “criadaEm”: “2018-05-29T18:01:10Z”,	(string-ISOdate)
  “inicioEm”: “2018-05-30T18:00:00Z”,	(string-ISOdate)
  “fimEm”: “2018-05-30T19:00:00Z”,	(string-ISOdate)
  “duracao”: 60,				(int)
  “valor”: 30.00,				(float)
  “canceladaEm”: “2018-05-30T19:00:00Z” (string-ISOdate)
}
```

### Questão 3 – Validação de formatos e regras simples

Percebemos que apareceram uns "lixos" no campo de "status" e algumas horas quebradas no campo "duracao" na base de dados porque não havia validação dos campos de entrada e desenvolvedor do front-end, que era muito junior e não fez a validação do lado de lá.

#### Validação enums
Precisamos validar a consistência dos dados antes de salvá-los para evitar problemas no futuro, respondendo com statusCode adequado quando a requisição for inválida.
Adicionar a regra para validar o campo status; este deve conter apenas os valores "ativo", "cancelado" e "pago".

```
PUT /reservas/{id}
Request:
body={
  “tipo”: “SAIBRO”,				(string-enum)
  “inicioEm”: “2018-05-30T19:00:00Z”,	(string-ISOdate)
  “fimEm”: “2018-05-30T20:00:00Z”,	(string-ISOdate)
  “status”: “deletado”,			(string-enum)
}
Response:
statusCode=400
```
#### Validação de regras de negócio simples
Precisamos validar se o "duracao" da reserva está sempre maior que 60 minutos, pois o clube decidiu que não é viável. Para facilitar a operação foi decidido que todas as reservas devem ter "duracao" múltiplos 60 minutos, sempre começando nos inícios de horas, não podendo uma reserva começar ou terminar em horas quebradas.

```
POST /reservas
Request:
body={
  “tipo”: “SAIBRO”,				(string-enum)
  “inicioEm”: “2018-05-30T18:30:00Z”,	(string-ISOdate)
  “fimEm”: “2018-05-30T19:00:00Z”,	(string-ISOdate)
}
Response:
statusCode=422
```

### Questão 4 -Regra de negócio

O sistema de reservas ficou popular e agora quase todo o clube está usando, mas infelizmente estamos reservando a mesma quadra para mais de uma pessoas e os clientes estão muito irritados com isso.
Não podemos deixar dois usuários reservarem a mesma quadra no mesmo horário, sendo assim, antes de criar uma reserva precisamos verificar se aquele "slot" já foi usado em uma reserva ativa.
O clube possui apenas 2 quadras, uma de SAIBRO e uma HARD e não tem pretenção de expansão no futuro próximo.

#### Validação de disponibilidade
Antes de gravar a nova reserva precisamos conferir se o range (inicio - fim) já não foi reservado anteriormente e em caso afirmativo, devemos informar ao usuário que não podemos fazer a reserva. 

```
POST /reservas
Request:
body={
  “tipo”: “SAIBRO”,				(string-enum)
  “inicioEm”: “2018-05-30T18:00:00Z”,	(string-ISOdate)
  “fimEm”: “2018-05-30T19:00:00Z”,	(string-ISOdate)
}
Response:
statusCode=422
body={
  "error": {
    "message": "O horário solicitado não está disponível, favor selecione um outro horário.",
    "code": "HORARIO_INDISPONIVEL"
  }
}
```

### Questão 5 - Rotas customizadas

Implementamos a regra para evitar overbooking, mas a usabilidade ficou muito ruim porque o usuário tem que fazer a reserva por tentativa e erro. Sendo assim, o nosso cliente pediu para que pudéssemos listar para o cliente os horários disponíveis para a quadra que ele deseja na data que ele deseja.

#### Consulta de disponibilidade

Vamos começar pela regra mais simples: Caso o horário/tipo solicitado esteja livre, retorna-o.

```
POST /disponibilidade
Request:
body={
  “tipo”: “SAIBRO”,				(string-enum)
  “inicioEm”: “2018-05-30T19:00:00Z”,	(string-ISOdate)
  “fimEm”: “2018-05-30T20:00:00Z”,	(string-ISOdate)
}
Response:
body=[{
  “tipo”: “SAIBRO”,				(string-enum)
  “duracao”: 60,				(int)
  “inicioEm”: “2018-05-30T19:00:00Z”,	(string-ISOdate)
  “fimEm”: “2018-05-30T20:00:00Z”,	(string-ISOdate)
}]
```

#### Consulta de disponibilidades similares
Caso o horário/tipo solicitado não esteja disponível, retorno uma lista de disponibilidades semelhantes ainda disponível. Seguindo a preferência:

	* Mesmo horário, outro tipo de quadra;
	* Mesmo tipo de quadra, uma hora antes e/ou depois;
	* Outro tipo de quadra, uma hora antes e/ou depois;
	* Mesmo tipo de quadra, duas hora antes e/ou depois;
	* Caso nenhuma das regras se encaixe o retorno é vazio.


```
GET /disponibilidade
Request:
body={
  “tipo”: “SAIBRO”,				(string-enum)
  “duracao”: 60,				(int)
  “inicioEm”: “2018-05-30T19:00:00Z”,	(string-ISOdate)
  “fimEm”: “2018-05-30T20:00:00Z”,	(string-ISOdate)
}
Response:
body=[{
  “tipo”: “HARD”,				(string-enum)
  “duracao”: 60,				(int)
  “inicioEm”: “2018-05-30T19:00:00Z”,	(string-ISOdate)
  “fimEm”: “2018-05-30T20:00:00Z”,	(string-ISOdate)
}, {
  “tipo”: “SAIBRO”,				(string-enum)
  “duracao”: 60,				(int)
  “inicioEm”: “2018-05-30T18:00:00Z”,	(string-ISOdate)
  “fimEm”: “2018-05-30T19:00:00Z”,	(string-ISOdate)
}, {
  “tipo”: “SAIBRO”,				(string-enum)
  “duracao”: 60,				(int)
  “inicioEm”: “2018-05-30T21:00:00Z”,	(string-ISOdate)
  “fimEm”: “2018-05-30T22:00:00Z”,	(string-ISOdate)
}, {
  “tipo”: “HARD”,				(string-enum)
  “duracao”: 60,				(int)
  “inicioEm”: “2018-05-30T21:00:00Z”,	(string-ISOdate)
  “fimEm”: “2018-05-30T22:00:00Z”,	(string-ISOdate)
}]
```

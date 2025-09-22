# Insurance Policy API

## Como rodar

```bash
docker-compose up --build
```

- API: http://localhost:8080
- Kafka UI: http://localhost:9000
- MockServer: http://localhost:1080

## Mensageria (Kafka)

- Tópico: `policy-events`
- Exemplo de envio de evento (CLI):

```bash
docker exec -it <kafka-container-id> \
  kafka-console-producer --broker-list kafka:9092 --topic policy-events
```
Cole o JSON do evento e pressione Enter.

- Exemplo de consumo:

```bash
docker exec -it <kafka-container-id> \
  kafka-console-consumer --bootstrap-server kafka:9092 --topic policy-events --from-beginning
```

Ou use o Kafdrop para visualizar mensagens e tópicos via UI.

## MockServer

- O mock da API de fraudes está definido em `mock-config/init.json`.
- Para alterar o comportamento, edite esse arquivo.

## Métricas/Observabilidade

- Endpoints Actuator: http://localhost:8080/actuator
- Logs: disponíveis no console e arquivos (ver configuração)

## Testes

- Execute os testes:
```bash
./mvnw test
```
- Relatório de cobertura: `target/site/jacoco/index.html`
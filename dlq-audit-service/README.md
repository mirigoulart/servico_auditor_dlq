# Justificativa da Arquitetura

## Arquitetura escolhida: Hexagonal

Para este projeto, escolhi utilizar a **Arquitetura Hexagonal**, também conhecida como **Ports and Adapters**.

A principal razão dessa escolha foi porque o serviço não possui interface visual e trabalha totalmente baseado em eventos recebidos da fila SQS. O foco principal da aplicação é consumir mensagens da DLQ, aplicar uma regra de negócio e persistir os dados no banco de dados de auditoria.

Diferente de um sistema tradicional MVC, aqui não existe uma “View” de fato. O serviço funciona como um processo de apoio responsável apenas pelo tratamento e armazenamento das mensagens que falharam no processamento principal.

---

## Por que não utilizei MVC?

O padrão MVC funciona muito bem em aplicações web com telas, formulários e interação direta com usuário.

Neste projeto, o ponto principal de entrada não é uma interface web, mas sim uma fila SQS. Por isso, utilizar MVC acabaria deixando a estrutura um pouco forçada e menos organizada para este tipo de problema.

O listener da fila faz mais sentido como um adaptador de entrada da aplicação do que como um “Controller” tradicional.

---

## Por que a Arquitetura Hexagonal fez sentido?

A Arquitetura Hexagonal ajuda a separar muito bem:

- regras de negócio;
- detalhes de infraestrutura;
- acesso ao banco;
- integração com AWS SQS.

Com isso, o domínio da aplicação fica mais limpo, desacoplado e fácil de manter.

Outra vantagem importante é a flexibilidade. Caso futuramente a fila deixe de ser SQS e passe a ser Kafka ou RabbitMQ, por exemplo, seria necessário alterar apenas o adaptador responsável pela mensageria, sem modificar as regras principais do sistema.

O mesmo vale para o banco de dados. Hoje o projeto utiliza H2 para desenvolvimento, mas seria possível trocar facilmente para PostgreSQL sem alterar a lógica de negócio.

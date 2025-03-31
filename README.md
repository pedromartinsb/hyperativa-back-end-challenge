# Visa Credit Card API

API para gerenciamento de cartões de crédito Visa desenvolvida com Spring Boot.

## 🚀 Tecnologias Utilizadas

- Java 17
- Spring Boot 3.4.4
- Spring Security com OAuth2
- Spring Data JPA
- MySQL
- Liquibase
- Swagger/OpenAPI
- JWT
- Lombok
- Gradle

## 📋 Pré-requisitos

- JDK 17
- MySQL 8.0
- Gradle 8.0+
- Docker (opcional)

## 🔧 Configuração do Ambiente

1. Clone o repositório:

```bash
git clone https://github.com/seu-usuario/hyperativa-back-end-challenge.git
cd hyperativa-back-end-challenge
```

2. Configure o banco de dados:

- Crie um banco de dados MySQL
- Configure as credenciais no arquivo `application.properties`

3. Execute a aplicação:

```bash
./gradlew bootRun
```

Ou usando Docker:

```bash
docker-compose up --build
```

## 📚 Documentação da API

A documentação da API está disponível através do Swagger UI:

- [Swagger UI](http://localhost:8080/swagger-ui.html)
- [OpenAPI Specification](http://localhost:8080/v3/api-docs)

## 🔒 Segurança

A API utiliza:

- OAuth2 Resource Server
- JWT para autenticação
- Rate limiting com Bucket4j
- Validação de dados com Spring Validation

## 🏗️ Arquitetura

O projeto segue os princípios da Clean Architecture:

- Domain: Entidades e regras de negócio
- Application: Casos de uso e serviços
- Adapter: Adaptadores para sistemas externos
- Infrastructure: Implementações técnicas

## 📝 Arquitetura Decision Records (ADRs)

Os ADRs estão localizados em `docs/adr/` e documentam as decisões arquiteturais importantes do projeto.

## 🧪 Testes

Para executar os testes:

```bash
./gradlew test
```

## 📦 Build

Para gerar o arquivo JAR:

```bash
./gradlew build
```

## 📄 Licença

Este projeto está sob a licença Apache 2.0. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## 👥 Autores

- **Hyperativa** - _Trabalho Inicial_ - [Hyperativa](https://github.com/hyperativa)

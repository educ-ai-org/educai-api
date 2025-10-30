# Educai API

API backend do sistema Educai - Uma plataforma educacional completa para gestão de salas de aula, atividades e interação entre professores e alunos.

## 📋 Sobre o Projeto

O Educai é uma API REST desenvolvida em Spring Boot que oferece uma solução completa para gestão educacional. A plataforma permite que professores criem e gerenciem salas de aula virtuais, publiquem atividades, compartilhem conteúdos e acompanhem o desempenho dos alunos através de relatórios detalhados e um sistema de gamificação com leaderboard. Os alunos podem participar de turmas, responder atividades, visualizar seu progresso e interagir com materiais didáticos.

**Público-alvo:** Instituições de ensino, professores e estudantes que buscam uma plataforma digital integrada para gerenciamento educacional.

## ✨ Funcionalidades

### 👤 Gerenciamento de Usuários
- ✅ Cadastro e autenticação de usuários (professores e alunos)
- ✅ Sistema de login com JWT (Access Token + Refresh Token)
- ✅ Atualização de perfil (nome, email)
- ✅ Upload e gerenciamento de foto de perfil
- ✅ Sistema de roles (TEACHER/STUDENT) com controle de acesso
- ✅ Logout com invalidação de tokens (blacklist)
- ✅ Sistema de pontuação gamificada para alunos

### 🏫 Gerenciamento de Salas de Aula
- ✅ Criação de salas de aula (apenas professores)
- ✅ Convite de alunos para participar das turmas
- ✅ Visualização de participantes e suas informações
- ✅ Atualização de título e curso da sala
- ✅ Remoção de alunos da sala (professores)
- ✅ Listagem de salas por usuário
- ✅ Obtenção de fotos de perfil dos participantes
- ✅ Sistema de leaderboard com ranking de pontuação
- ✅ Exclusão de salas de aula

### 📝 Gerenciamento de Atividades (Classworks)
- ✅ Criação de atividades com questões de múltipla escolha
- ✅ Definição de prazo de entrega
- ✅ Submissão de respostas pelos alunos
- ✅ Correção automática com cálculo de percentual de acertos
- ✅ Visualização de detalhes da atividade
- ✅ Consulta de respostas individuais (alunos e professores)
- ✅ Status de entrega das atividades por aluno
- ✅ Listagem de atividades por sala
- ✅ Exclusão de atividades

### 📌 Sistema de Posts
- ✅ Criação de posts com anexos (professores)
- ✅ Upload de arquivos para posts
- ✅ Visualização de posts por sala de aula
- ✅ Atualização de título e descrição de posts
- ✅ Geração de URL para download de arquivos anexos
- ✅ Exclusão de posts

### 📊 Relatórios e Análises
- ✅ Geração de relatórios em formato CSV
- ✅ Exportação de resultados de atividades
- ✅ Leaderboard com ranking de pontuação dos alunos
- ✅ Visualização de status de respostas por atividade

### 📖 Funcionalidade de Dicionário
- ✅ Consulta de definições de palavras em inglês
- ✅ Integração com Dictionary API
- ✅ Retorno de múltiplas definições e áudios de pronúncia
- ✅ Classificação gramatical das palavras

### 🔐 Segurança e Autenticação
- ✅ Autenticação JWT com Access Token e Refresh Token
- ✅ Tokens armazenados em cookies HTTP-only
- ✅ Blacklist de tokens para logout seguro
- ✅ Controle de acesso baseado em roles (RBAC)
- ✅ Senhas criptografadas com BCrypt
- ✅ CORS configurado para origens específicas
- ✅ Sessões stateless

### 📦 Integração e Armazenamento
- ✅ Armazenamento de arquivos no Azure Blob Storage
- ✅ Geração de URLs públicas para download
- ✅ Upload de imagens de perfil e arquivos de posts
- ✅ Persistência de dados no MongoDB

## 🛠️ Stack Tecnológica

### Linguagem e Framework
- **Java 17** - Linguagem de programação
- **Spring Boot 3.2.3** - Framework principal
- **Maven** - Gerenciador de dependências e build

### Frameworks e Bibliotecas Spring
- **Spring Web** - Desenvolvimento de APIs REST
- **Spring Data MongoDB** - Integração com MongoDB
- **Spring Security** - Autenticação e autorização
- **Spring Validation** - Validação de dados

### Banco de Dados
- **MongoDB 4.4.6** - Banco de dados NoSQL orientado a documentos

### Segurança
- **Spring Security** - Framework de segurança
- **java-jwt (Auth0) 4.4.0** - Geração e validação de tokens JWT
- **BCrypt** - Criptografia de senhas

### Armazenamento em Nuvem
- **Azure Blob Storage 12.25.1** - Armazenamento de arquivos

### Documentação
- **Springdoc OpenAPI 2.3.0** - Documentação Swagger/OpenAPI automática

### Utilitários
- **Lombok 1.18.32** - Redução de código boilerplate
- **ModelMapper 3.2.0** - Mapeamento de objetos (Entity ↔ DTO)

### DevOps e CI/CD
- **Docker Compose** - Containerização do MongoDB
- **GitHub Actions** - Pipeline de CI/CD
- **Maven Wrapper** - Build tool

### APIs Externas
- **Dictionary API** - Consulta de definições de palavras

## 🏗️ Arquitetura

O projeto segue uma arquitetura em camadas (Layered Architecture) com separação clara de responsabilidades:

```
src/main/java/api/educai/
│
├── config/              # Configurações da aplicação
│   ├── AzureBlobConfig      # Configuração Azure Storage
│   ├── ModelMapperConfig    # Configuração ModelMapper
│   └── SwaggerConfig        # Configuração Swagger/OpenAPI
│
├── controllers/         # Camada de apresentação (REST Controllers)
│   ├── UserController       # Endpoints de usuários
│   ├── ClassroomController  # Endpoints de salas de aula
│   ├── ClassworkController  # Endpoints de atividades
│   ├── PostController       # Endpoints de posts
│   └── DictionaryController # Endpoints de dicionário
│
├── services/            # Camada de lógica de negócio
│   ├── UserService          # Lógica de usuários
│   ├── ClassroomService     # Lógica de salas
│   ├── ClassworkService     # Lógica de atividades
│   ├── PostService          # Lógica de posts
│   ├── AuthenticationService # Lógica de autenticação
│   ├── AzureBlobService     # Integração Azure
│   ├── DictionaryService    # Integração Dictionary API
│   ├── QuestionService      # Lógica de questões
│   └── token/              # Gerenciamento de tokens
│
├── repositories/        # Camada de acesso a dados (MongoDB)
│   └── [Repositories]       # Interfaces Spring Data MongoDB
│
├── entities/            # Modelos de domínio (Documentos MongoDB)
│   ├── User                 # Entidade de usuário
│   ├── Classroom            # Entidade de sala de aula
│   ├── Classwork            # Entidade de atividade
│   ├── Question             # Entidade de questão
│   ├── Option               # Entidade de opção
│   ├── Answer               # Entidade de resposta
│   ├── Post                 # Entidade de post
│   └── TokenBlacklist       # Entidade de tokens invalidados
│
├── dto/                 # Data Transfer Objects
│   ├── user/               # DTOs de usuário
│   ├── classroom/          # DTOs de sala de aula
│   ├── classwork/          # DTOs de atividade
│   ├── answer/             # DTOs de resposta
│   ├── post/               # DTOs de post
│   ├── auth/               # DTOs de autenticação
│   └── dictionary/         # DTOs de dicionário
│
├── security/            # Configurações de segurança
│   ├── SecurityConfig       # Configuração Spring Security
│   └── AuthenticationProvider # Provider de autenticação customizado
│
├── filter/              # Filtros HTTP
│   └── TokenFilter          # Filtro de validação de tokens JWT
│
├── enums/               # Enumerações
│   └── Role                 # Enum de papéis (TEACHER/STUDENT)
│
└── utils/               # Classes utilitárias
    ├── CSVGenerator         # Geração de relatórios CSV
    ├── FilaObj              # Estrutura de fila
    ├── ListObj              # Estrutura de lista
    ├── PasswordGenerator    # Gerador de senhas
    └── email/              # Utilitários de email
```

### Fluxo de Dados

1. **Requisição HTTP** → Controller recebe a requisição
2. **Autenticação** → TokenFilter valida o JWT
3. **Autorização** → SecurityConfig verifica permissões (@Secured)
4. **Validação** → Spring Validation valida dados de entrada
5. **Processamento** → Service executa lógica de negócio
6. **Persistência** → Repository interage com MongoDB
7. **Mapeamento** → ModelMapper converte Entity ↔ DTO
8. **Resposta** → Controller retorna ResponseEntity com DTO

### Padrões de Design Utilizados

- **MVC (Model-View-Controller)** - Separação de camadas
- **Repository Pattern** - Abstração de acesso a dados
- **DTO Pattern** - Transferência de dados entre camadas
- **Dependency Injection** - Injeção de dependências via Spring
- **Builder Pattern** - Lombok para construção de objetos
- **Filter Chain** - Processamento de requisições HTTP

## 📋 Pré-requisitos

### Software Necessário

- **Java JDK 17** ou superior
- **Maven 3.6+** (ou usar o Maven Wrapper incluído)
- **MongoDB 4.4+** (pode usar Docker Compose)
- **Docker e Docker Compose** (opcional, para executar MongoDB)
- **Git** para clonar o repositório

### Contas e Credenciais Necessárias

- **Conta Azure** com serviço Blob Storage configurado
- **Connection String do Azure Blob Storage**
- **Instância MongoDB** (local ou remota)
- Credenciais do MongoDB

## 🚀 Instalação

### 1. Clonar o Repositório

```bash
git clone https://github.com/educ-ai-org/educai-api.git
cd educai-api
```

### 2. Configurar MongoDB com Docker (Opcional)

```bash
docker-compose up -d
```

Isso iniciará uma instância MongoDB na porta 27017.

### 3. Configurar Variáveis de Ambiente

Crie um arquivo `.env` ou configure as variáveis de ambiente diretamente:

```bash
# MongoDB
export MONGO_HOST=localhost
export MONGO_PORT=27017
export MONGO_DB=educai
export MONGO_USERNAME=admin
export MONGO_PASSWORD=senha_segura

# Azure Blob Storage
export AZURE_STORAGE_CONNECTION_STRING="sua_connection_string"
export AZURE_STORAGE_CONTAINER_NAME="nome_do_container"
export AZURE_STORAGE_URL="https://sua_conta.blob.core.windows.net/"
```

**Importante:** O arquivo `src/main/resources/application.properties` já contém as chaves JWT. Em produção, **substitua-as por chaves secretas seguras**.

### 4. Instalar Dependências

Com Maven instalado:

```bash
mvn clean install
```

Ou usando o Maven Wrapper:

```bash
./mvnw clean install
```

### 5. Executar a Aplicação

```bash
./mvnw spring-boot:run
```

A API estará disponível em: `http://localhost:8080`

## ⚙️ Configuração

### Variáveis de Ambiente

| Variável | Descrição | Exemplo |
|----------|-----------|---------|
| `MONGO_HOST` | Host do MongoDB | `localhost` |
| `MONGO_PORT` | Porta do MongoDB | `27017` |
| `MONGO_DB` | Nome do banco de dados | `educai` |
| `MONGO_USERNAME` | Usuário do MongoDB | `admin` |
| `MONGO_PASSWORD` | Senha do MongoDB | `senha123` |
| `AZURE_STORAGE_CONNECTION_STRING` | Connection string do Azure | `DefaultEndpointsProtocol=https;...` |
| `AZURE_STORAGE_CONTAINER_NAME` | Nome do container no Azure | `educai-files` |
| `AZURE_STORAGE_URL` | URL base do Azure Storage | `https://conta.blob.core.windows.net/` |

### Arquivo application.properties

O arquivo está localizado em `src/main/resources/application.properties` e contém:

- Configuração de conexão com MongoDB
- Configuração do Azure Blob Storage
- Limite de tamanho de upload de arquivos (10MB)
- Configuração de CORS
- Secrets do JWT (⚠️ **Alterar em produção**)

### Configuração de CORS

Por padrão, a API aceita requisições das seguintes origens:
- `http://localhost:5173`
- `http://localhost:80`
- `http://educai.eastus.cloudapp.azure.com`
- `https://educai.eastus.cloudapp.azure.com`
- `https://www.educai.xyz`

Para adicionar novas origens, edite o método `corsConfigurationSource()` em `SecurityConfig.java`.

### Limite de Upload de Arquivos

O tamanho máximo de arquivo permitido é **10MB**. Para alterar:

```properties
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
server.tomcat.max-http-form-post-size=10MB
```

## 📖 Uso

### Iniciar a Aplicação

```bash
./mvnw spring-boot:run
```

### Acessar Documentação Swagger

Após iniciar a aplicação, acesse:

```
http://localhost:8080/swagger-ui/index.html
```

A documentação interativa contém todos os endpoints disponíveis com exemplos de requisições e respostas.

### Exemplos de Uso

#### 1. Criar um Usuário

```bash
POST http://localhost:8080/user
Content-Type: application/json

{
  "name": "João Silva",
  "email": "joao@exemplo.com",
  "password": "senha12345",
  "role": "STUDENT"
}
```

#### 2. Fazer Login

```bash
POST http://localhost:8080/user/auth
Content-Type: application/json

{
  "email": "joao@exemplo.com",
  "password": "senha12345"
}
```

Resposta:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "refresh_token_aqui"
}
```

#### 3. Criar uma Sala de Aula (Professor)

```bash
POST http://localhost:8080/classroom
Authorization: Bearer {seu_token}
Content-Type: application/json

{
  "title": "Matemática Avançada",
  "course": "Engenharia"
}
```

#### 4. Criar uma Atividade (Professor)

```bash
POST http://localhost:8080/classwork
Authorization: Bearer {seu_token}
classroomId: {id_da_sala}
Content-Type: application/json

{
  "title": "Prova de Cálculo I",
  "description": "Prova sobre derivadas e integrais",
  "datePosting": "2024-01-15",
  "endDate": "2024-01-20",
  "questions": [...]
}
```

#### 5. Listar Salas do Usuário

```bash
GET http://localhost:8080/user/classrooms
Authorization: Bearer {seu_token}
```

## 💻 Desenvolvimento

### Executar em Modo de Desenvolvimento

```bash
./mvnw spring-boot:run
```

### Executar Testes

```bash
./mvnw test
```

### Compilar o Projeto

```bash
./mvnw clean package
```

O arquivo JAR será gerado em: `target/educai-0.0.1-SNAPSHOT.jar`

### Estrutura de Testes

Os testes estão localizados em `src/test/java/api/educai/`.

### Convenções de Código

- **Nomenclatura**: CamelCase para classes, camelCase para métodos e variáveis
- **DTOs**: Sempre usar DTOs para transferência de dados entre camadas
- **Validação**: Usar anotações Jakarta Validation nas entidades e DTOs
- **Documentação**: Documentar endpoints com `@Operation` do Swagger
- **Segurança**: Sempre usar `@Secured` para controle de acesso baseado em roles

### Configuração do IDE

Para IntelliJ IDEA ou Eclipse, importe o projeto como um projeto Maven existente. O Lombok deve ser habilitado no IDE.

## 🚢 Deployment

### Build da Aplicação

```bash
./mvnw clean package -DskipTests
```

### Executar o JAR

```bash
java -jar target/educai-0.0.1-SNAPSHOT.jar
```

### Docker (Opcional)

Embora não haja um Dockerfile incluído, você pode criar um:

```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/educai-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### CI/CD com GitHub Actions

O projeto possui um pipeline configurado em `.github/workflows/maven.yml`:

1. **Build e Testes** - Executa em cada push para `main`
2. **Upload de Artefatos** - Gera arquivo JAR
3. **Deploy Automático** - Deploy para VM AWS via SCP e SSH

### Configuração de Secrets no GitHub

Configure os seguintes secrets no repositório:

- `AZURE_STORAGE_CONNECTION_STRING`
- `AZURE_STORAGE_CONTAINER_NAME`
- `AZURE_STORAGE_URL`
- `MONGO_HOST`
- `MONGO_PORT`
- `MONGO_DB`
- `MONGO_USERNAME`
- `MONGO_PASSWORD`
- `AWS_VM_HOST`
- `AWS_VM_USERNAME`
- `AWS_SSH_PRIVATE_KEY`
- `AWS_VM_PORT`

### Considerações de Produção

- ✅ Alterar as chaves JWT em `application.properties`
- ✅ Usar HTTPS para todas as comunicações
- ✅ Configurar rate limiting
- ✅ Implementar logs estruturados
- ✅ Configurar backup do MongoDB
- ✅ Usar variáveis de ambiente para todas as credenciais
- ✅ Habilitar monitoramento e métricas (Spring Actuator)

## 🔧 Troubleshooting

### Erro de Conexão com MongoDB

**Problema:** `MongoSocketException: Connection refused`

**Solução:**
1. Verifique se o MongoDB está rodando: `docker ps`
2. Confirme as variáveis de ambiente `MONGO_HOST` e `MONGO_PORT`
3. Teste a conexão: `mongo mongodb://localhost:27017`

### Erro de Autenticação

**Problema:** `401 Unauthorized`

**Solução:**
1. Verifique se o token JWT está válido e não expirou
2. Confirme que o header `Authorization: Bearer {token}` está correto
3. Verifique se o usuário possui a role necessária para o endpoint

### Erro ao Fazer Upload de Arquivo

**Problema:** `MaxUploadSizeExceededException`

**Solução:**
1. Reduza o tamanho do arquivo (limite: 10MB)
2. Ou aumente o limite em `application.properties`:
   ```properties
   spring.servlet.multipart.max-file-size=20MB
   spring.servlet.multipart.max-request-size=20MB
   ```

### Erro de Conexão com Azure Blob Storage

**Problema:** `BlobStorageException: Connection failed`

**Solução:**
1. Verifique a `AZURE_STORAGE_CONNECTION_STRING`
2. Confirme que o container existe no Azure
3. Verifique as permissões da conta de armazenamento

### Maven Build Falha

**Problema:** Erros de dependências não resolvidas

**Solução:**
```bash
./mvnw clean install -U
```

### Modo Debug

Para executar a aplicação em modo debug:

```bash
./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
```

E conecte seu IDE na porta 5005.

### Verificar Logs

Logs da aplicação são exibidos no console. Para logs mais detalhados:

```properties
logging.level.api.educai=DEBUG
logging.level.org.springframework.security=DEBUG
```

## 📚 Referência da API

### Base URL

```
http://localhost:8080
```

### Autenticação

Todos os endpoints (exceto login, registro e refresh token) requerem autenticação via Bearer Token:

```
Authorization: Bearer {seu_token_jwt}
```

### Endpoints Principais

#### 👤 Usuários (`/user`)

| Método | Endpoint | Descrição | Autenticação | Role |
|--------|----------|-----------|--------------|------|
| POST | `/user` | Criar usuário | Não | - |
| POST | `/user/auth` | Login | Não | - |
| POST | `/user/refreshToken` | Renovar token | Não | - |
| GET | `/user` | Listar usuários | Sim | - |
| GET | `/user/classrooms` | Listar salas do usuário | Sim | - |
| PATCH | `/user` | Atualizar perfil | Sim | - |
| DELETE | `/user/{id}` | Deletar usuário | Sim | - |
| POST | `/user/picture` | Upload foto perfil | Sim | - |
| GET | `/user/picture` | Obter foto perfil | Sim | - |
| GET | `/user/picture-url` | URL da foto perfil | Sim | - |
| POST | `/user/logoff` | Logout | Sim | - |

#### 🏫 Salas de Aula (`/classroom`)

| Método | Endpoint | Descrição | Autenticação | Role |
|--------|----------|-----------|--------------|------|
| POST | `/classroom` | Criar sala | Sim | TEACHER |
| GET | `/classroom/{id}` | Obter sala | Sim | - |
| GET | `/classroom/{id}/participants` | Listar participantes | Sim | - |
| GET | `/classroom/{id}/classworks` | Listar atividades | Sim | - |
| GET | `/classroom/{id}/student-classworks` | Atividades (aluno) | Sim | STUDENT |
| GET | `/classroom/{id}/posts` | Listar posts | Sim | - |
| GET | `/classroom/{id}/report` | Gerar relatório CSV | Sim | - |
| GET | `/classroom/{id}/leaderboard` | Leaderboard | Sim | - |
| GET | `/classroom/{id}/profile-pictures` | Fotos participantes | Sim | - |
| POST | `/classroom/{id}/invite` | Convidar usuário | Sim | TEACHER |
| PATCH | `/classroom/{id}` | Atualizar sala | Sim | TEACHER |
| DELETE | `/classroom/{id}` | Deletar sala | Sim | TEACHER |
| DELETE | `/classroom/{classroomId}/user/{userId}` | Remover aluno | Sim | TEACHER |

#### 📝 Atividades (`/classwork`)

| Método | Endpoint | Descrição | Autenticação | Role |
|--------|----------|-----------|--------------|------|
| POST | `/classwork` | Criar atividade | Sim | TEACHER |
| GET | `/classwork/{id}` | Obter atividade | Sim | - |
| GET | `/classwork/{id}/answer` | Obter resposta (aluno) | Sim | - |
| GET | `/classwork/{id}/answer/{studentId}` | Resposta do aluno | Sim | TEACHER |
| GET | `/classwork/{id}/answers/status` | Status respostas | Sim | TEACHER |
| POST | `/classwork/answer` | Submeter resposta | Sim | STUDENT |
| DELETE | `/classwork/{id}` | Deletar atividade | Sim | - |

#### 📌 Posts (`/posts`)

| Método | Endpoint | Descrição | Autenticação | Role |
|--------|----------|-----------|--------------|------|
| POST | `/posts` | Criar post | Sim | TEACHER |
| GET | `/posts` | Listar todos posts | Sim | - |
| GET | `/posts/{id}` | Obter post | Sim | - |
| GET | `/posts/{id}/download` | URL download | Sim | - |
| PATCH | `/posts/{id}` | Atualizar post | Sim | TEACHER |
| DELETE | `/posts/{id}` | Deletar post | Sim | TEACHER |

#### 📖 Dicionário (`/dictionary`)

| Método | Endpoint | Descrição | Autenticação | Role |
|--------|----------|-----------|--------------|------|
| GET | `/dictionary/{word}/definition` | Definição da palavra | Sim | - |

### Códigos de Status HTTP

- `200 OK` - Requisição bem-sucedida
- `201 Created` - Recurso criado com sucesso
- `204 No Content` - Sucesso sem conteúdo de retorno
- `400 Bad Request` - Dados inválidos
- `401 Unauthorized` - Não autenticado
- `403 Forbidden` - Sem permissão
- `404 Not Found` - Recurso não encontrado
- `500 Internal Server Error` - Erro no servidor

### Exemplos de Requisições

#### Login e Obtenção de Token

```bash
curl -X POST http://localhost:8080/user/auth \
  -H "Content-Type: application/json" \
  -d '{
    "email": "professor@exemplo.com",
    "password": "senha123"
  }'
```

Resposta:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiI2NTg...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWI..."
}
```

#### Criar Sala de Aula

```bash
curl -X POST http://localhost:8080/classroom \
  -H "Authorization: Bearer SEU_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Introdução à Programação",
    "course": "Ciência da Computação"
  }'
```

#### Upload de Foto de Perfil

```bash
curl -X POST http://localhost:8080/user/picture \
  -H "Authorization: Bearer SEU_TOKEN" \
  -F "file=@/caminho/para/foto.jpg"
```

#### Criar Post com Arquivo

```bash
curl -X POST http://localhost:8080/posts \
  -H "Authorization: Bearer SEU_TOKEN" \
  -F "file=@/caminho/arquivo.pdf" \
  -F "title=Material de Estudo" \
  -F "description=Apostila completa" \
  -F "datePosting=2024-01-15" \
  -F "classroomId=65a1b2c3d4e5f6789abc"
```

## 📄 Licença

Este projeto está licenciado sob a **MIT License**.

```
MIT License

Copyright (c) 2024 educ.ai

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

## 👥 Contribuidores

Este projeto é mantido pela equipe **educ.ai**.

Para contribuir:

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/MinhaFeature`)
3. Commit suas mudanças (`git commit -m 'Adiciona MinhaFeature'`)
4. Push para a branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request

## 🙏 Agradecimentos

- **Spring Framework Team** - Framework robusto e completo
- **MongoDB** - Banco de dados flexível e escalável
- **Microsoft Azure** - Serviços de armazenamento em nuvem
- **Auth0** - Biblioteca JWT de alta qualidade
- **Dictionary API** - API gratuita de dicionário
- **Comunidade Open Source** - Ferramentas e bibliotecas incríveis

## 📞 Suporte

Para questões e suporte:

- **Issues**: [GitHub Issues](https://github.com/educ-ai-org/educai-api/issues)
- **Documentação**: Swagger UI em `/swagger-ui/index.html`
- **Organização**: [educ-ai-org](https://github.com/educ-ai-org)

---

**Desenvolvido com ❤️ pela equipe educ.ai**

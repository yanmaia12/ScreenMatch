# 🎬 ScreenMatch

O **ScreenMatch** é uma aplicação completa (Full-Stack) para gerir o seu acervo pessoal de séries. O sistema permite procurar séries diretamente na API do OMDB, traduzir a sinopse automaticamente para português e guardar todos os dados e episódios numa base de dados local.

## 🚀 Tecnologias Utilizadas

### Backend
* **Java 25** (conforme configurado no pom.xml)
* **Spring Boot 3+** (Web, Data JPA)
* **PostgreSQL** (Base de Dados)
* **Maven** (Gestão de dependências)
* Integração com **OMDB API** (Busca de séries) e **MyMemory API** (Tradução de sinopses)

### Frontend
* **HTML5 & CSS3** (Arquitetura CSS brutalista, design responsivo)
* **JavaScript (Vanilla/ES6)** (Consumo da API Rest, manipulação do DOM)

---

## ⚙️ Como Instalar e Rodar o Projeto

Para correr este projeto na sua máquina, precisará de configurar o Backend (Java/Spring) e o Frontend (HTML/JS) separadamente.

### 1. Pré-requisitos
* **Java JDK** instalado (compatível com a versão 25 do projeto).
* **PostgreSQL** instalado e em execução.
* **Visual Studio Code** com a extensão **Live Server** instalada.
* Chave de API do **OMDB** (pode ser obtida gratuitamente no site oficial).

### 2. Configurar e Correr o Backend (Spring Boot)

1. **Criar a Base de Dados:**
   Abra o terminal do PostgreSQL ou o pgAdmin e crie a base de dados:
   ```sql
   CREATE DATABASE alura_series;
   ```

2. **Configurar as Variáveis de Ambiente:**
   No diretório `src/main/resources/`, renomeie o ficheiro `application.properties.example` para `application.properties` e preencha com as suas informações:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost/NOME_DB
   spring.datasource.username=postgres
   spring.datasource.password=SUA_SENHA_AQUI
   spring.datasource.driver-class-name=org.postgresql.Driver

   hibernate.dialect=org.hibernate.dialect.HSQLDialect
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   spring.jpa.format-sql=true

   omdb.apikey=SUA_CHAVE_API_AQUI
   ```

3. **Iniciar o Servidor:**
   Abra o terminal na pasta raiz do backend e execute:
   ```bash
   ./mvnw spring-boot:run
   *O backend ficará ativo em `http://localhost:8080`.*

## 3. Configurar e Correr o Frontend

⚠️ **Importante (CORS):** O backend está configurado para aceitar apenas pedidos vindos de `http://127.0.0.1:5501`. O frontend **tem obrigatoriamente** de rodar nesta porta.

1. Abra a pasta `frontend` no VS Code.
2. Certifique-se de que o **Live Server** está configurado para a porta `5501`.
3. Clique com o botão direito em `index.html` e escolha **"Open with Live Server"**.
4. Aceda no browser: `http://127.0.0.1:5501/index.html`.

---

## 📡 Endpoints da API (Backend)

Todos os endpoints utilizam o prefixo base `http://localhost:8080`.

### 🎬 Séries
* **`GET /series`**: Lista todas as séries do banco de dados.
* **`GET /series/top5`**: Lista as 5 melhores séries (por avaliação).
* **`GET /series/lancamentos`**: Lista as 5 séries com lançamentos mais recentes.
* **`GET /series/{id}`**: Detalhes de uma série específica por ID.
* **`GET /series/categoria/{genero}`**: Filtra séries por categoria (ex: drama, comédia).
* **`POST /series/adicionar/{nomeSerie}`**: Procura no OMDB, traduz a sinopse e guarda a série e seus episódios no banco de dados.

### 📺 Temporadas e Episódios
* **`GET /series/{id}/temporadas/todas`**: Lista todos os episódios de todas as temporadas de uma série.
* **`GET /series/{id}/temporadas/{nTemp}`**: Lista episódios de uma temporada específica.
* **`GET /series/{id}/temporadas/top`**: Lista os 5 melhores episódios de uma série específica.
# 📱 Connections

**Connections** é um aplicativo moderno de **mídia social** desenvolvido para facilitar conexões entre pessoas.  
O app permite que usuários visualizem fotos de perfis, explorem listas de pessoas e naveguem por uma interface fluida construída com as tecnologias mais atuais do ecossistema Android.

## 🏛️ Arquitetura

O projeto **Connections** segue uma implementação sólida de **Clean Architecture**, garantindo um código organizado, escalável e de fácil manutenção.  
A aplicação é dividida em camadas bem definidas, cada uma com responsabilidades específicas:

### **📁 Data Layer**
Responsável pelo acesso a dados da aplicação.

- **local/**  
  Contém toda a implementação de persistência, incluindo:
    - Room Database
    - DAOs
    - Entidades locais

- **remote/**  
  Abriga toda a comunicação com APIs externas, como:
    - Retrofit
    - DTOs
    - Services

---

### **📁 DI (Dependency Injection)**
Gerencia todos os módulos de injeção de dependência usando **Koin**.

- **modules/**  
  Onde ficam:
    - Módulos de repositórios
    - Módulos de use cases
    - Módulos de viewmodels
    - Configuração do banco de dados
    - Configuração de rede

---

### **📁 Domain Layer**
Contém apenas regras de negócio e nada relacionado a frameworks.

- **model/**  
  Modelos puros da aplicação (entities do domínio).

- **repository/**  
  Interfaces que definem contratos para acesso a dados.
  Implementações concretas ficam na camada *data*.

- **usecase/**  
  Cada caso de uso da aplicação é isolado em uma classe própria.

---

### **📁 Presentation Layer**
Responsável pela UI e lógica de apresentação.

- **views/**  
  Telas construídas totalmente em **Jetpack Compose**.

- **viewmodel/**  
  Lógica de estado seguindo o padrão **MVI**, com:
    - StateFlow para estados
    - SharedFlow para efeitos one-shot

- **navigation/**  
  Implementação do Navigation Component com Compose para gerenciar rotas e argumentos.

---

### **📁 UI Layer**
Contém toda a definição visual do app.

- **colors/** — Paleta de cores da aplicação
- **theme/** — Temas claros/escuros e estilos padrão
- **typography/** — Fontes (ex: Mona Sans) e regras de tipografia

---

### **🧪 Testes**
O projeto possui dois ambientes de teste distintos:

#### **📦 test/** (Unit Tests + Roboletric)
- Testes unitários dos use cases
- Testes de viewmodel
- Testes de repository com mocks
- Testes de UI com **Robolectric** (sem precisar de dispositivo físico)

#### **📦 androidTest/** (Instrumented Tests)
- Testes instrumentados em dispositivos/emuladores Android
- Testes de navegação
- Testes de integração com banco de dados (Room)
- Testes de fluxo completo de UI com Compose Testing

### **📚 Princípios SOLID aplicados**

O projeto adota ativamente alguns dos principais princípios do **SOLID**, contribuindo para um código mais limpo e flexível:

- **SRP — Single Responsibility Principle**  
  Cada classe possui apenas uma responsabilidade:
    - UseCases fazem apenas 1 ação
    - ViewModels gerenciam apenas estados e eventos da UI
    - Repositórios apenas manipulam dados

- **ISP — Interface Segregation Principle**  
  Interfaces são pequenas e focadas:  
  Ex.: o `UserRepository` define somente contratos relacionados ao usuário, sem métodos desnecessários.

- **DIP — Dependency Inversion Principle**  
  A camada de domínio depende apenas de **abstrações**, não de implementações concretas.  
  Ex.: UseCases dependem do `Repository` (interface), e não de classes da camada *data*.  
  A injeção dessas dependências é feita via **Koin**.

---

### **🎨 Design Patterns utilizados**

O projeto também utiliza padrões de projeto importantes para garantir desacoplamento e testabilidade:

- **Observer Pattern**  
  Implementado com `StateFlow` e `SharedFlow` nas ViewModels.  
  A UI (Compose) observa mudanças automaticamente sem necessidade de callbacks manuais.

- **Adapter / Delegation Pattern**  
  Usado na lógica de listas dentro da UI, como:
    - Adaptação dos dados vindo da API para os modelos da UI
    - Adaptação das entidades do Room para os models de domínio

## 🖼️ Construção da View (UI Layer)

Toda a interface do **Connections** é desenvolvida em **Jetpack Compose**, utilizando uma abordagem declarativa, reativa e altamente escalável. A comunicação entre a View e a ViewModel segue um fluxo bem definido para garantir clareza, previsibilidade e manutenção simples.

### 🔌 Comunicação View ↔ ViewModel

A camada de apresentação utiliza um padrão baseado em três elementos fundamentais:

-   **UiState** — Representa o estado atual e imutável da tela.

-   **UiEvent** — Eventos disparados pela View para solicitar ações à ViewModel.

-   **UiEffect** — Efeitos únicos, como navegação, mensagens e ações pontuais.


### 🧭 Navegação Tipada

A navegação é construída com **Navigation Component**, utilizando rotas totalmente **tipadas**, o que garante:

-   Segurança em tempo de compilação

-   Redução de erros ao passar argumentos

-   Facilidade de expansão ao adicionar novas telas


### ✨ Recursos da Camada de UI

-   **Shimmer personalizado** para estados de carregamento.

-   **LazyListState** para controle avançado de listas (scroll, restauração e comportamento fino).

-   **SavedStateHandle** na ViewModel para restaurar e manter estados críticos após recriações.

-   **Cache de dados**, permitindo carregamento mais rápido e experiência mais consistente.

-   **Mapeamento completo de cenários de erro e loading**, oferecendo uma UX mais guiada e previsível.

## ♿ Acessibilidade

O **Connections** foi desenvolvido seguindo cuidados essenciais de acessibilidade para garantir que qualquer pessoa possa utilizá-lo com conforto, clareza e autonomia.

### ✔️ Boas práticas aplicadas

-   **Aprovado no scanner de acessibilidade do Google**

-   **Uso de `contentDescription`** em todos os componentes interativos e imagens

-   **Cores e contrastes fortes**, projetados para maximizar legibilidade

-   **Tamanho dos botões pensado para toque acessível**, respeitando zonas mínimas recomendadas

-   **Aproveitamento dos componentes do Material 3**, que já incluem padrões de acessibilidade incorporados, como foco visível, espaçamento adequado e hierarquia clara

## 🌐 Network

O **Connections** consome **4 APIs externas**, incluindo autenticação, conteúdo dinâmico e dados de usuários. Todas as chamadas são feitas utilizando **Retrofit**, com um `Service` dedicado para cada API e configuração de URLs via **BuildConfigField**, permitindo fácil gerenciamento de ambientes (Dev / Homolog / Prod) no futuro.

### 🔑 1. Firebase Auth

Responsável pela **autenticação de usuários**, gerenciamento de sessão e logout seguro.

### 🖼️ 2. API de Fotos (Picsum)

📍 `https://picsum.photos/`  
Usada para carregar imagens aleatórias exibidas no feed do aplicativo.  
Permite resultados rápidos e leves sem necessidade de autenticação.

### 👤 3. API de Pessoas (RandomUser)

📍 `https://randomuser.me/`  
Fornece dados fictícios como nome, idade, localização e avatar, utilizados para compor a lista de pessoas no feed.

### 🟩 4. API do Desafio (MockAPI - PicPay)

📍 `https://609a908e0f5a13001721b74e.mockapi.io/picpay/api/`  
Consumida para exibir a lista de seguidores, mantendo compatibilidade com o desafio proposto.

### ⚙️ Como está organizado

Cada API possui:

-   **Uma Interface Service próprio**

-   **Um repositório e data source específico**

-   **Uma interface separada para facilitar testes unitários e mocks**

-   **Base URL definida dentro de `build.gradle.kts` via `buildConfigField`**, permitindo:

    -   Alternância simples entre ambientes

    -   Adoção futura de flavors

    -   Melhor manutenção e escalabilidade

## 💾 Banco de Dados & Offline First

O **Connections** utiliza uma abordagem **Offline First**, garantindo que o app continue funcional mesmo sem conexão com a internet. Toda a estrutura foi desenvolvida com foco em **resiliência**, **baixa latência** e **experiência fluida**.

### 🧠 Como funciona a lógica Offline First

1.  **🔍 Mapeamento completo de erros**

    -   Todos os fluxos tratam cenários como valores _nulos_, _vazios_, _falhas de rede_ ou _timeouts_.

    -   Caso haja qualquer erro, a View recebe um estado claro que exibe:

        -   Uma tela de erro dedicada, **ou**

        -   Um aviso amigável informando que não foi possível carregar os dados.

2.  **📥 Primeiro busca no banco local (Room)**

    -   Ao iniciar qualquer fluxo, o app consulta **primeiro o banco local**.

    -   Isso garante:

        -   Carregamento instantâneo

        -   Experiência offline

        -   Menor dependência da rede

3.  **🔐 Dados permanecem armazenados até o usuário sair da sessão**

    -   Toda a base local (Room) permanece populada durante toda a sessão ativa do usuário.

    -   **A limpeza completa do banco só acontece no logout**, garantindo que:

        -   O usuário sempre veja seus dados atualizados e persistidos

        -   O app abra rapidamente mesmo após ser fechado

        -   As APIs sejam acessadas apenas quando realmente necessário

4.  **🌐 Se não houver dados no Room, busca da API e sincroniza**

    -   Caso determinado fluxo não tenha dados no banco:

        1.  O Repository chama o _remote service_

        2.  O resultado é salvo no **Room**

        3.  A View atualiza automaticamente via Flow/State

    -   Quando existe dado local, ele é exibido **imediatamente**, e a API é chamada em segundo plano para atualização.

5.  **🔄 Sincronização contínua**

    -   O usuário sempre visualiza primeiro os dados locais.

    -   Quando há internet, o app sincroniza silenciosamente:

        -   API → salva no Room → updates fluem para a UI através do Flow

## 🧪 Testes

O **Connections** possui uma cobertura sólida de testes, garantindo estabilidade, previsibilidade e segurança nas camadas mais críticas da aplicação. A estratégia inclui **testes unitários**, **testes integrados** e **testes instrumentados**, cobrindo desde o domínio até a interface.

### ✅ Tipos de testes implementados

### **1. Testes Unitários**

Realizados com **JUnit**, **Mockito/MockK**, **Coroutines Test** , cobrindo:

-   **Repositories**

    -   Mock de DAOs, services e Firebase

    -   Garantia de que tratam corretamente erros, fluxo offline e respostas das APIs

-   **UseCases**

    -   Teste isolado da regra de negócio

    -   Verificação de inputs/outputs e estrutura de estados (_Result_, _Flow_, etc.)

-   **DTOs & Mappers**

    -   Conversões entre camadas mapeadas corretamente

    -   Cenários com campos faltando, nulos, listas vazias, etc.


----------

### **2. Testes Instrumentados**

Simulam o comportamento real entre módulos da aplicação, garantindo que:

-   O database interage corretamente com os DAOs

-   Services Retrofit devolvem os dados como esperado

-   Repositories fazem a ponte correta entre Remote ↔ Local

-   UseCases carregam o fluxo completo corretamente

Usando **Robolectric**, **Compose UI Test** e **TestNavHostController**, cobrindo:

-   Testes funcionais de UI em Jetpack Compose

-   Testes de navegação com o Navigation Component tipado

-   Testes de acessibilidade (contentDescription, foco, clique, leitura)

## 🤖 CI (GitHub Actions)

Este projeto possui uma pipeline de **GitHub Actions** que realiza os seguintes passos:

1. **Checkout do código**
2. **Build do APK**
3. **Execução dos testes**
4. **Geração de cobertura de testes com Jacoco**
5. **Análise de código com Detekt**

O workflow garante que o código esteja sempre funcional e com boa qualidade antes de ser integrado.
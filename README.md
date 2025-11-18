# Social Media App

Aplicação Android escrita em **Kotlin** usando **Jetpack Compose**, estruturada como uma pequena **rede social de contatos**.  
O projeto foca em **arquitetura limpa**, **boas práticas modernas**, **testes**, **CI** e **experiência de usuário acessível**.

---

## 🧱 Arquitetura

O app segue uma abordagem inspirada em **Clean Architecture**, com separação clara de camadas e responsabilidades:

### Camadas principais

- **Presentation**
    - Telas em **Jetpack Compose**.
    - **ViewModels** usando **MVVM**.
    - Gerenciamento de:
        - `UiState` → estado atual da tela.
        - `UiEvent` → ações/intentos do usuário.
        - `UiEffect` → efeitos de “uma vez só” (navegação, mensagens, etc.).

- **Domain**
    - **Models de domínio** (ex.: `User`).
    - **UseCases** com `operator fun invoke()` para uma API mais limpa:
      ```kotlin
      class GetUsersUseCase(
          private val repository: UserRepository
      ) {
          operator fun invoke() = repository.getUsers()
      }
      ```
    - Regras de negócio desacopladas das camadas de dados e de UI.

- **Data**
    - Implementações de **repositórios**.
    - Comunicação com a **API (Retrofit)**.
    - Persistência local com **Room**.
    - Mapeamento entre DTOs, entidades locais e modelos de domínio.

### Boas práticas e padrões

- **Clean Code**: nomes claros, funções coesas, responsabilidades bem definidas.
- **SOLID**:
    - SRP (Single Responsibility Principle)
    - ISP (Interface Segregation Principle)
    - DIP (Dependency Inversion Principle)
- Uso de **Dependency Injection** com **Koin** para desacoplar dependências e facilitar testes.
- Design patterns aplicados de forma pontual (ex.: uso de Singletons via DI, não “na mão”).

---

## 🎨 Layout (UI)

A interface é construída inteiramente com **Jetpack Compose**:

- Uso de **Material 3** e componentes modernos.
- Telas reativas conectadas ao `UiState` do ViewModel.
- Estados contemplados:
    - Loading (incluindo shimmer customizado).
    - Lista de contatos carregada.
    - Estados de erro / vazio.

### Shimmer customizado

- Implementado um **shimmer customizado** para a tela de contatos:
    - Placeholders animados enquanto os dados são carregados.
    - Feedback visual mais agradável e responsivo para o usuário.

### Responsividade e ciclo de vida

- Integração com **SavedStateHandle** / manejo de **SavedState**:
    - Estado da tela é preservado em mudanças de configuração (ex.: rotação).
    - Comportamento consistente durante todo o ciclo de vida da Activity.

---

## ♿ Acessibilidade

O app foi pensado para ser **acessível**:

- Uso de `contentDescription` em imagens e ícones relevantes.
- Hierarquia de layout organizada, facilitando leitura por serviços de acessibilidade.
- Cores e contrastes pensados para melhor legibilidade.
- Componentes de UI do Compose/Material 3 que já trazem acessibilidade embutida como base.

---

## 🔗 Network (API / Requests)

### Stack de rede

- **Retrofit** (atualizado para versão compatível com Kotlin/Compose).
- **OkHttp** + Logging Interceptor.
- **Kotlinx Serialization** para JSON.

### Configuração de endpoints com BuildConfig

O endpoint da API é configurado via `BuildConfig`, variando por ambiente:

```kotlin
buildTypes {
    debug {
        buildConfigField(
            "String",
            "PICPAY_SERVICE_BASE_URL",
            "\"https://.../debug/api/\""
        )
    }
    release {
        buildConfigField(
            "String",
            "PICPAY_SERVICE_BASE_URL",
            "\"https://.../prod/api/\""
        )
    }
}
```
Na camada de dados, o app usa `BuildConfig.PICPAY_SERVICE_BASE_URL`, permitindo:

- Troca de endpoint por **build type**.
- Separação de ambientes (**dev**, **homologação**, **produção/mock**).

### Concor­rência & reatividade

- Uso de **Kotlin Coroutines** e **Flow**:
    - `suspend` functions para chamadas de rede em background.
    - Fluxos reativos para atualização da UI conforme novos dados chegam.

---

## 🗄️ Banco de Dados (Offline First)

A camada de persistência é implementada com **Room**:

- **Entidades** (`UserEntity`, etc.).
- **DAOs** para acesso aos dados.
- **Database** central (`RoomDatabase`).

### Estratégia Offline First

- Carrega primeiro os dados **locais** (Room).
- Tenta atualizar com dados da **API**:
    - Em caso de sucesso → atualiza banco + UI.
    - Em caso de falha → mantém dados locais (quando existentes).
- Acesso via **Flow**:
    - Sempre que o banco é atualizado, a UI reage automaticamente.

Isso garante:

- Melhor experiência em conexões instáveis.
- O app continua útil mesmo sem rede (quando há cache local).

---

## ✅ Testes

O projeto contempla testes com foco em **lógica de negócios** e **camada de dados**.

### Tipos de testes

- **Unit Tests**
    - Testes de UseCases.
    - Testes de Repositórios (com fakes de DAO e service).
    - Testes de models (ex.: `User`).

### Bibliotecas de teste

- **JUnit** (migrado para Maven Central, com versão mais recente).
- **kotlinx-coroutines-test**:
    - Testes de funções `suspend`.
    - Manipulação de `TestDispatcher`, `advanceUntilIdle`, etc.
- **MockK**:
    - Quando necessário, para mocks de dependências.
- **Koin Test** (se usado) para validar módulos de injeção de dependência.

---

## 📊 Cobertura de Testes (Jacoco)

O projeto integra **Jacoco** para gerar relatórios de cobertura:

### Configuração

- Plugin `jacoco` adicionado no módulo `app`.
- Task customizada `jacocoTestReport`, que:
    - Depende de `testDebugUnitTest`.
    - Gera relatórios **XML** e **HTML**.
    - Ignora classes geradas (R, BuildConfig, etc.).

### Como gerar o relatório

```bash
./gradlew clean testDebugUnitTest jacocoTestReport
```

## 🤖 CI (GitHub Actions)

O projeto possui um workflow de **CI** em `.github/workflows/` que:

1. Faz **checkout** do repositório.
2. Configura **JDK 17** (Temurin).
3. Configura **cache de Gradle**.
4. Executa:
    - `./gradlew assembleDebug` → build do APK de debug.
    - `./gradlew testDebugUnitTest` → testes unitários.
    - `./gradlew jacocoTestReport` → relatório de cobertura.
5. Faz upload de artefatos:
    - `app-debug.apk`
    - Relatório Jacoco (`app/build/reports/jacoco/jacocoTestReport`).

Isso garante:

- Feedback automatizado em pushes e pull requests.
- Artefatos prontos (APK + report) para download direto pela interface do GitHub.
- Verificação de qualidade contínua.

---

## 🧩 Outras Tecnologias & Decisões

### Kotlin DSL (`build.gradle.kts`)

- Projeto configurado usando arquivos `.kts`.
- Facilita uso de recursos do Kotlin na configuração de build.

### Atualização de bibliotecas

Koin, Retrofit, Coil, Room e demais libs foram atualizadas para versões mais recentes, compatíveis com:

- Kotlin moderno
- Jetpack Compose
- Gradle 8+

### Remoção do Kotlin Android Extensions

- Deixou de ser compatível com as versões atuais de Kotlin.
- Substituído por abordagens modernas:
    - **ViewBinding**, quando necessário.
    - Ou UI diretamente em **Jetpack Compose**.

---

## 🚀 Como rodar o projeto

1. Clone o repositório:

   ```bash
   git clone https://github.com/SEU-USUARIO/Social-Media-App.git
   cd Social-Media-App
   ```

2. Abra o projeto no Android Studio (versão recente com suporte a Kotlin, Jetpack Compose e Gradle 8+).

3. Aguarde o Gradle sync finalizar.

4. Rode o app:
    - Escolha um emulador ou dispositivo físico.
    - Clique em Run.

5. Rode os testes + cobertura (opcional):
   ```bash
    ./gradlew clean testDebugUnitTest jacocoTestReport
   ```

---

## 📌 Sobre o projeto

Este repositório foi pensado como:

- Um **projeto vitrine** para:
    - Arquitetura limpa em Android.
    - Uso de **Jetpack Compose** com estado bem modelado.
    - Integração de **testes** e **CI com Jacoco**.
- Um material para **processos seletivos** (como o do PicPay) e para **estudo de boas práticas**.

Sinta-se à vontade para explorar o código, abrir issues ou sugerir melhorias. 😊

---

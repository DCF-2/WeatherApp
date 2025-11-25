# 🌤️ WeatherApp

![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-purple?logo=kotlin)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-Material3-green?logo=android)
![Firebase](https://img.shields.io/badge/Firebase-Auth-orange?logo=firebase)
![Google Maps](https://img.shields.io/badge/Google%20Maps-SDK-blue?logo=google-maps)
![License](https://img.shields.io/badge/License-MIT-grey)

**Aplicativo Android desenvolvido com Jetpack Compose, integrando autenticação via Firebase e mapas interativos.**

O **WeatherApp** é um projeto moderno que serve como base para um sistema de monitoramento climático. Atualmente, ele gerencia autenticação de usuários, exibe localizações em um mapa interativo e permite o gerenciamento de uma lista de cidades favoritas.

---

## 📱 Funcionalidades Implementadas

### 🔐 Autenticação (Firebase Auth)
* **Login:** Tela de login segura com validação de campos.
* **Registro:** Criação de nova conta de usuário com e-mail e senha.
* **Logout:** Funcionalidade de sair da conta na barra superior.

### 🗺️ Mapas e Localização (Google Maps SDK)
* **Visualização de Mapa:** Integração completa com Google Maps via Compose.
* **Localização do Usuário:** Botão para centralizar na localização atual (requer permissão).
* **Marcadores Personalizados:** Exibe marcadores em cidades pré-definidas (Recife, Caruaru, João Pessoa) com cores distintas.
* **Adicionar no Clique:** Clique no mapa para adicionar uma nova "Cidade" nas coordenadas selecionadas.

### 📋 Gerenciamento de Cidades
* **Lista de Favoritos:** Exibição de cidades cadastradas em uma `LazyColumn`.
* **Adicionar Cidade:** Diálogo modal para inserir o nome de uma nova cidade.
* **Remover Cidade:** Botão para excluir itens da lista.

---

## 🛠️ Tecnologias e Bibliotecas

O projeto foi construído utilizando as práticas mais recentes do desenvolvimento Android (2025):

* **Linguagem:** Kotlin 2.0+
* **Interface (UI):** Jetpack Compose (Material 3)
* **Navegação:** Navigation Compose
* **Mapas:** Google Maps Compose & Play Services Location
* **Backend / Auth:** Firebase Authentication
* **Injeção de Segredos:** Secrets Gradle Plugin (para chaves de API)
* **Build:** Gradle Kotlin DSL (`.kts`) com Version Catalogs (`libs.versions.toml`)

---

## 📂 Estrutura do Projeto

```text
com.weatherapp
├── Model
│   └── City.kt              # Modelo de dados da cidade
├── ui
│   ├── nav                  # Configuração de navegação
│   │   ├── BottomNavItem.kt # Itens da barra inferior
│   │   └── MainNavHost.kt   # Rotas (Home, List, Map)
│   ├── theme                # Tema e cores (Material 3)
│   ├── CityDialog.kt        # Componente de diálogo
│   ├── Homepage.kt          # Tela inicial
│   ├── ListPage.kt          # Tela de lista de cidades
│   └── MapPage.kt           # Tela de mapas
├── MainViewModel.kt         # Gerenciamento de estado (Lista de cidades)
├── MainActivity.kt          # Activity principal (Scaffold e NavHost)
├── LoginActivity.kt         # Activity de Login
└── RegisterActivity.kt      # Activity de Registro
```
## 🔧 Configuração e Execução

Para rodar este projeto, você precisará configurar as chaves de API do Google Maps e o projeto do Firebase.

### 1. Pré-requisitos

- Android Studio **Ladybug** ou superior (suporte a Java 21)
- Conta no **Google Cloud Platform** (para Maps SDK)
- Conta no **Firebase Console**

---

### 2. Configurar Firebase

1. Crie um projeto no **Firebase Console**.
2. Ative o método de autenticação **Email/Password**.
3. Baixe o arquivo `google-services.json` do seu projeto.
4. Coloque o arquivo em:

```
WeatherApp/app/google-services.json
```

---

### 3. Configurar Google Maps

Obtenha uma API Key no **Google Cloud Platform** com **Maps SDK for Android** ativado.

Abra (ou crie) o arquivo `local.properties` na raiz do projeto e adicione:

```properties
MAPS_API_KEY=sua_chave_do_google_maps_aqui
```

> O projeto usa o **secrets-gradle-plugin** para injetar essa chave no `AndroidManifest.xml` de forma segura.

---

### 4. Executar

1. Abra o projeto no **Android Studio**
2. Sincronize o Gradle (**Sync Project with Gradle Files**)
3. Escolha um emulador ou dispositivo físico
4. Clique em **Run (▶)**

---

## 🧩 Melhorias Futuras (Roadmap)

- [ ] Integração real de clima com API (ex: OpenWeatherMap) usando Retrofit
- [ ] Persistência local com **Room Database** para salvar cidades favoritas
- [ ] Tela detalhada da cidade com previsão estendida
- [ ] Ícones de clima dinâmicos baseados na temperatura

---

## 🤝 Contribuição

Contribuições são bem-vindas!

1. Faça um **Fork**
2. Crie uma branch:
   ```bash
   git checkout -b feature/nova-feature
   ```
3. Commit:
   ```bash
   git commit -m "Adiciona nova feature"
   ```
4. Push:
   ```bash
   git push origin feature/nova-feature
   ```
5. Abra um **Pull Request**

---

## 📄 Licença

Este projeto está licenciado sob a licença **MIT**.  
Veja o arquivo `LICENSE` para mais detalhes.

**Copyright (c) 2025  
Davi Freitas**

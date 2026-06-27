# Real-Time Communication & Social Layer (DECYRA)

## Overview
This module of **DECYRA** enables real-time interaction between students through chat, forum discussions, and video conferencing. It supports collaborative decision-making for academic and career guidance.

## Features

### 1. P2P Chat
Enables direct communication between users for private discussions about academic paths and career options.

### 2. Community Forum
A shared space where students discuss Erasmus programs, MSc applications, and career opportunities.

### 3. Video Conferencing
Real-time meetings for mentorship sessions and group discussions.

## 4. Security & API Protection

Sensitive operations and external API keys are handled through a secure backend layer using AWS infrastructure. The OpenAI API key is the only actively managed secret deployed in AWS Secrets Manager, while other previously used keys and services have been removed from production deployment to minimize cost and reduce unnecessary external dependencies.

API keys are never exposed on the client side and are accessed exclusively through server-side functions.

This ensures:

- No direct exposure of LLM or external API keys in the mobile application
- Secure request routing through backend-controlled endpoints
- Centralized and controlled access to AI services
- Reduced attack surface by minimizing active external integrations
  
## Design Goals
- Low-latency communication
- Seamless user experience across features
- Support for both synchronous and asynchronous interaction
- Accessibility across different devices

## Architecture Overview
All communication features are built on a shared real-time backend layer, enabling:
- Event-driven updates
- Persistent user sessions
- Scalable messaging infrastructure

## Why this matters
Students often need quick feedback and peer validation when making academic decisions. This system enables collaborative decision-making rather than isolated choices.

---

## Frontend Code Architecture

**DECYRA** follows a hybrid Android architecture combining Java-based navigation with Kotlin and Jetpack Compose for state management and UI rendering. The system is designed with clear separation of concerns to ensure scalability, modularity, and maintainability.

### 1. Navigation Layer (Java Activities)
The Java layer is responsible for screen routing and Android lifecycle entry points. It primarily handles Intent-based navigation and acts as a bridge between different application modules. This layer contains minimal logic and focuses on directing users to the appropriate Compose-based screens.

### 2. State & Data Layer (Kotlin Activities)
The Kotlin Activity layer manages application state and backend integration. It handles real-time communication with Firebase, including data fetching, updates, and synchronization. This layer also processes user-related information and provides structured data to the UI layer.

### 3. UI Presentation Layer (Jetpack Compose)
The UI layer is fully implemented using Jetpack Compose and is responsible for rendering all user interfaces in a declarative manner. It reacts to state changes from the Activity layer and ensures a responsive and dynamic user experience across all features.

### 4. Component-Based UI System
The UI is built using reusable Jetpack Compose components such as cards, avatars, menus, and animated elements. This modular design promotes consistency across screens, reduces code duplication, and improves maintainability through a declarative UI approach. This approach enables a declarative and state-driven UI model, allowing the interface to react dynamically to application state changes.

### 5. Architecture Summary
This hybrid design enables:
- Clear separation between navigation, logic, and UI
- Scalable feature-based development
- Seamless integration between Java and Kotlin
- Reactive UI updates through Compose

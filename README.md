# DECYRA

DECYRA is a production-ready mobile application designed to help students make better academic and career decisions in the field of Computer Science. It combines AI-powered guidance with community-driven interaction, offering personalized support for Erasmus choices, Master studies, and early career planning.

## Overview

Many students struggle to decide between options such as joining an Erasmus program, applying for a Master's degree, or entering the job market. DECYRA was created to make this process easier through an intelligent mentor system, real-time communication features, and a modern mobile interface.

The app does not focus only on generic recommendations. Instead, it aims to provide more personalized guidance based on user needs, preferences, and goals, while also allowing students to exchange experiences through community features.

## Main Features

- AI mentor for academic and career guidance
- Recommendation support for Erasmus, Master's programs, and career paths
- RAG-based chatbot pipeline for smarter and more relevant answers
- User authentication with Face ID support
- Forum for sharing opinions and experiences
- P2P chat between users
- Video conferencing for live communication
- Notes system for saving useful results, meeting codes, and personal reminders
- Speech-to-text support for easier interaction
- Responsive and modern mobile UI built for usability

## Technologies Used

DECYRA is a multi-technology mobile application that combines Android development, cloud services, AI services, and real-time communication tools.

### Mobile Development
- **Kotlin** for modern Android frontend development
- **Jetpack Compose** for building the UI
- **Java** for backend and core application logic

### AI and NLP
- **OpenAI GPT-4.1** for final response generation
- **OpenAI GPT-4o-mini** for lightweight classification tasks
- **OpenAI text-embedding-3-small** for text embeddings
- **Pinecone** as the vector database for semantic retrieval
- **Cohere Rerank** for improving result relevance
- **RAG (Retrieval-Augmented Generation)** architecture for personalized recommendations

### Computer Vision and Authentication
- **Google ML Kit** for face detection
- **FaceNet** for facial embedding generation and face-based login
- **AR guidance** to help users capture correct face angles during registration and sign-in

### Cloud and Backend Infrastructure
- **Firebase Realtime Database** as the main cloud database
- **Firebase Authentication** for user login and password reset
- **Firebase Cloud Messaging** for notifications
- **Supabase Edge Functions** for handling messaging-related requests
- **Supabase Storage** for profile image storage
- **AWS Lambda** for secure API handling
- **AWS API Gateway** for request routing
- **AWS Secrets Manager** for secure API key storage
- **MySQL** for local database development and backup
- **Node.js**
- **TypeScript**
- **HTTP / Web APIs**

### Communication
- **ZEGOCLOUD API**
- **WebRTC** for video calling and real-time communication

### Additional Services
- **Google Services** for email-based password reset and speech-to-text support

## AI Architecture

The core intelligence of DECYRA is based on a Retrieval-Augmented Generation pipeline.

1. The user submits a query.
2. A lightweight classifier detects whether the system should retrieve new recommendation data or continue an existing conversation.
3. If retrieval is needed, the query is converted into embeddings.
4. Pinecone retrieves the most relevant candidate results.
5. Cohere reranks these results.
6. GPT-4.1 generates the final response based on the filtered context and system rules.

This architecture improves relevance, reduces unnecessary processing, and helps keep response times low.

## Project Structure

The project is organized into three main parts:

- **backend**: AI logic, domain model, and application services
- **frontend**: screen-level UI implementation with Java/Kotlin integration
- **extras**: utility classes and additional helper functionality

The application also includes cloud functions for secure API access and push notification handling.

## Codebase Summary

The codebase includes a total of 99 files:

- 47 Java files
- 43 Kotlin files
- 6 interface files
- 2 cloud function files
- 1 enum file

## Evaluation Highlights

The project was evaluated through user research, expert feedback, and usability testing. Results showed strong interest in an app of this type, especially for the AI mentor and forum features, while usability scores were high for chatbot performance, registration flow, and navigation.

## Demo

- **Source Code:** [Google Drive Folder](https://drive.google.com/drive/folders/1-2vgJKmJiMTkbafO4MwRBt8_libxXVx0?usp=sharing)
- **APK:** [Download APK](https://drive.google.com/file/d/1glCVYu89DaMa4YPIhF1TUdMCwJ4Vfod7/view?usp=sharing)
- **Video Presentation:** [Watch on YouTube](https://www.youtube.com/watch?v=UF1DgyN0P_4)

## Academic Context

Department of Informatics, Athens University of Economics and Business  
Course: Human-Computer Interaction  
Spring Semester 2025-26

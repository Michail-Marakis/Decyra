## Cloud and Backend Infrastructure

The cloud and backend infrastructure of DECYRA was designed to support a production-ready mobile application with secure authentication, real-time data handling, media storage, notification delivery, and protected API communication.

- **Firebase Realtime Database**: is the main cloud database of the application. It is used to store and synchronize dynamic app data in real time, allowing the system to support responsive user interactions and cloud-based content management.

- **Firebase Authentication**: handles user login and identity management. It is also used for password reset functionality, providing a secure and reliable authentication layer for the app.

- **Firebase Cloud Messaging (FCM)**: is used for push notifications. It enables real-time user alerts and supports interactive features that require immediate communication with the user.

- **Supabase Edge Functions**: are used for server-side messaging-related operations. In DECYRA, they act as an integration layer for sending requests connected to the notification workflow.

- **Supabase Storage**: is used to store user profile images. This separates media storage from the main database and keeps file handling more scalable and organized.

- **AWS Lambda**: is used as a secure serverless layer for handling API-related operations. This helps move sensitive logic away from the client application.

- **AWS API Gateway**: is responsible for routing requests between the mobile app and backend services. It acts as the controlled entry point for secure backend communication.

- **AWS Secrets Manager**: is used for secure storage of API keys and other sensitive credentials. This avoids exposing secrets inside the mobile codebase.

- **MySQL**: is used locally for database development, testing, and backup purposes. It helped structure and experiment with the data model before synchronizing key parts with the cloud environment.

- **HTTP / Web APIs**: provide the communication layer between the Android client, the cloud infrastructure, and external services. They allow all backend and cloud components to operate as one integrated system.

- **WebRTC / ZEGOCLOUD API** are used to support the video conference functionality of the application. WebRTC provides the real-time communication layer, while ZEGOCLOUD enables the implementation of user-to-user video calls through a dedicated cloud video calling platform.

- **Google Services** are used for additional user-support features, including email-based password reset, speech-to-text input, and text-to-speech assistance. These services improve accessibility and make the app more practical in real usage scenarios.

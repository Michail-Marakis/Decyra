## Cloud and Backend Infrastructure

The cloud and backend infrastructure of DECYRA was designed to support a production-ready mobile application with strong emphasis on real-time interaction, secure API orchestration, scalable data handling, and reliable user services. Since the app combines AI guidance, user-generated content, messaging, notifications, authentication, and profile management, the backend architecture had to integrate multiple cloud platforms in a coordinated and secure way.

### Firebase Realtime Database

**Firebase Realtime Database** is used as the main cloud database of the application. It stores and synchronizes core application data in real time, allowing the mobile client to retrieve updated information without relying on slow manual refresh flows.

In DECYRA, this database acts as the central online data layer for the app. It supports user-facing features that require immediate consistency from the application point of view, such as chatbot-related data retrieval, user profile information, stored interaction history, and other dynamic content that must remain accessible across sessions and devices.

A major reason for using Firebase Realtime Database is its real-time synchronization model. This makes it particularly suitable for an interactive mobile environment where users expect instant updates and smooth transitions between screens. In practice, this helps the app behave more like a live service rather than a static mobile client.

### Firebase Authentication

**Firebase Authentication** is responsible for user identity management. It supports the login workflow of the application and also handles password reset functionality through email-based recovery.

In DECYRA, authentication is an essential part of the user journey because many features depend on having a persistent and secure user identity, including profile personalization, saved notes, chatbot history, and user-to-user interaction. By using Firebase Authentication, the app avoids implementing a custom authentication stack from scratch and instead relies on a mature cloud-based identity solution.

This choice also improves maintainability and security. The authentication layer is separated from the client logic, while password reset flows are handled through managed infrastructure rather than through custom insecure implementations.

### Firebase Cloud Messaging

**Firebase Cloud Messaging (FCM)** is used to deliver push notifications to users. This allows the application to support real-time user engagement even when the target user is not actively using the app.

Within DECYRA, FCM is part of the communication and interaction layer. It is especially useful for scenarios where a user should be informed immediately about events related to app activity, such as messaging-related actions or communication workflows connected to the social and interactive features of the platform.

Using FCM strengthens the real-time character of the application. Instead of making the client poll for updates, the backend can trigger notifications directly, improving both responsiveness and the overall user experience.

### Supabase Edge Functions

**Supabase Edge Functions** are used as lightweight server-side functions for messaging-related backend operations. In the architecture of DECYRA, they act as an integration layer between parts of the application that need server-side execution but do not belong inside the Android client.

Based on your implementation, these functions are specifically used to send requests to Firebase Cloud Messaging. This means Supabase Edge Functions work as a backend bridge that helps trigger notifications safely and in a controlled way, instead of pushing this logic directly into the mobile application.

This approach improves modularity. It keeps sensitive or operational logic outside the client, reduces coupling between services, and makes the notification flow cleaner and easier to maintain.

### Supabase Storage

**Supabase Storage** is used for storing user profile images in the cloud. This gives the application a dedicated storage layer for user-uploaded media, instead of trying to store image binaries directly in the main application database.

In DECYRA, this is particularly important for profile customization and user identity presentation. Since users can manage profile information and upload profile images, a separate object storage service is a more appropriate architectural choice than using the primary data store for media handling.

This separation also improves scalability and cleanliness of the system design. Structured app data remains in the main database, while media assets are handled through a service designed specifically for file storage and retrieval.

### AWS Lambda

**AWS Lambda** is used as a secure serverless execution layer for API-related operations. In your architecture, Lambda is not just a utility choice but part of the app’s security design.

According to your implementation, Lambda functions are used so that external APIs and sensitive backend-facing operations are not directly exposed or hardcoded inside the mobile codebase. Instead, the app sends requests through a controlled cloud function layer, which then handles the secure interaction with the required services.

This gives DECYRA a more production-oriented backend model. It reduces the risk of exposing credentials, improves abstraction between client and external services, and allows backend logic to evolve without tightly coupling everything to the Android application.

### AWS API Gateway

**AWS API Gateway** is used as the routing and entry layer for requests that pass through the AWS serverless backend. In practice, it acts as the front door through which the mobile app communicates with Lambda-based backend functionality.

In DECYRA, API Gateway helps structure and expose backend endpoints in a more controlled way. Instead of embedding direct access patterns to protected services inside the client, the application communicates through managed API routes, which can then forward the request to the corresponding Lambda function.

This improves security, observability, and architectural clarity. It also makes the backend easier to extend, because new secure routes can be introduced without redesigning the whole mobile communication model.

### AWS Secrets Manager

**AWS Secrets Manager** is used to store and protect sensitive credentials such as API keys. This is a critical architectural decision in a project like DECYRA, where the app depends on multiple external services and AI-related APIs.

As described in your implementation, one of the reasons for adding the AWS layer was to avoid hardcoding API credentials either inside the mobile application or in a simple cloud database. Secrets Manager solves exactly this problem by keeping sensitive values in a secure secrets system, where they can be accessed programmatically by trusted backend components.

This significantly improves the security posture of the project. It also makes the architecture much closer to production standards, since secret handling is isolated from application code and managed through dedicated infrastructure.

### MySQL

**MySQL** is used locally during development for database design, experimentation, and backup purposes. In your project, it played an important role in building and testing the data model before or alongside cloud deployment.

More specifically, MySQL was used to structure the database locally and to maintain a backup-oriented representation of the cloud data model. This allowed you to experiment more safely with schema evolution and data organization before reflecting those changes in the cloud environment.

This is a practical engineering choice. Even though Firebase is the main cloud database in production usage, a local relational environment can still be very helpful during development, testing, and backup workflows.

### HTTP / Web APIs

**HTTP / Web APIs** form the communication backbone between the Android client and the different cloud services used by the application. Since DECYRA combines Firebase, Supabase, AWS, AI services, and communication services, a common request-response communication layer is necessary.

In practice, HTTP-based APIs are what allow the mobile frontend, backend functions, databases, and third-party services to exchange data. This includes sending user requests, triggering backend actions, retrieving cloud data, and integrating external functionality in a unified way.

This layer is important not because it is visually visible to the user, but because it is what ties the full distributed architecture together. Without it, the different components of the system would remain isolated technologies rather than one coordinated product.

### Architectural Role

Taken together, these technologies form the operational backbone of DECYRA. Firebase provides the real-time application layer, Supabase supports storage and messaging-side integration, AWS secures API access and secret management, MySQL supports local engineering workflows, and HTTP connects the entire ecosystem.

This combination reflects the needs of a modern AI-enabled mobile application: real-time responsiveness, secure identity handling, reliable notification delivery, modular backend execution, protected credentials, and scalable service integration. In that sense, the cloud and backend architecture is not just supportive infrastructure; it is one of the main reasons the application can behave like a production-ready system rather than a simple academic prototype.

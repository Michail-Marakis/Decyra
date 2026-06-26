# Face ID (Computer Vision + AR)

## Overview

This Face ID system combines **Computer Vision**, **Augmented Reality (AR)**, and **FaceNet** embeddings to provide a solid face registration and authentication pipeline.

A custom **AR guidance interface**, designed and implemented using **Jetpack Compose**, assists users during both the **registration** and **login** processes, ensuring correct face positioning and pose alignment before image capture.

---

## Registration Flow

### 1. User Registration

A custom **AR interface**, developed with **Jetpack Compose**, together with **voice guidance**, guides the user through the registration process.

The user is instructed to capture images in **6 different poses**:

- Look Straight (`LOOK_STRAIGHT`)
- Look Left (`LOOK_LEFT`)
- Look Right (`LOOK_RIGHT`)
- Look Up (`LOOK_UP`)
- Look Down (`LOOK_DOWN`)
- Blink Eyes (`BLINK_EYES`)

The AR overlay provides real-time visual feedback to help the user correctly align their face before each capture.

### 2. Face Detection

For every captured image:

- **Google ML Kit** detects and validates the face.
- Only valid face images continue to the next stage.

### 3. Face Embedding

Each detected face is processed by **FaceNet (CNN)** to generate a face embedding vector.

### 4. User Template Creation

For every pose:

- Store **3 embedding vectors**
- Compute **1 centroid vector**

This results in a total of **19 stored vectors** for each registered user.

---

## Login Flow

The authentication process follows a similar workflow.

1. A custom **Jetpack Compose AR interface** guides the user to align their face.
2. **ML Kit** detects the face in real time.
3. Once the pose is validated, an image is captured.
4. **FaceNet** generates the embedding vector.
5. The generated embedding is compared with the stored user vectors using **Cosine Similarity**.
6. Authentication succeeds if the similarity score exceeds the predefined threshold. We got the most accurate results with threashold = 0.75

---

## Embedding Evaluation

Several embedding configurations were tested during development. The selected approach, which stores **3 FaceNet embeddings for each pose** together with **1 centroid embedding**, provided the most consistent authentication results across different users and lighting conditions while maintaining reasonable storage requirements. Based on these experiments, the final user template consists of **19 stored vectors**, offering the best trade-off between accuracy and robustness.

---

## Technologies Used

- Jetpack Compose (Custom AR Guidance UI)
- Google ML Kit (Face Detection)
- FaceNet (CNN Face Embeddings)
- Voice Guidance
- Cosine Similarity Matching
- Firebase (store the vectors for every user)

<p align="center">
  <img width="1146" height="629" alt="image" src="https://github.com/user-attachments/assets/06662f10-dbae-4599-9e80-0edfa93b3e86" />
</p>

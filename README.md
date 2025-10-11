📝 Journal App

A personal journaling application built with Spring Boot, MongoDB, and JWT-based Authentication.
This app allows users to register, log in securely, and create, update, and manage personal journal entries — all while ensuring data privacy through token-based authentication.

🚀 Features

✅ User Authentication

Login/Signup with secure password hashing using BCrypt

JWT-based authentication to protect APIs

Google OAuth2 login

✅ Journal Management

Create, update, delete, and view personal journal entries

Each journal entry linked to a user

Filter or view entries per user

✅ Security & Access Control

Implemented using Spring Security

Public routes (like login/register) open to all

Protected routes accessible only to authenticated users

Admin-specific access for future scalability

✅ Tech Stack

☕ Spring Boot 3+

🍃 MongoDB Atlas

🔐 JWT Authentication

🔧 Maven Project

💾 Spring Data MongoDB

💬 Spring Web

🧩 Spring Security

🧰 Technologies Used

| Tool                     | Purpose                        |
| ------------------------ | ------------------------------ |
| **Spring Boot**          | Backend framework              |
| **Spring Security**      | Authentication & authorization |
| **MongoDB Atlas**        | NoSQL database                 |
| **JWT (JSON Web Token)** | Token-based authentication     |
| **BCrypt**               | Password encryption            |
| **Lombok**               | Reduces boilerplate code       |
| **Maven**                | Dependency management          |

🔐 Security Flow

1. User registers or logs in

2. Server validates credentials and generates JWT Token

3. Token is sent in the Authorization Header

4. JwtFilter validates token on each request

5. If valid → grants access, else → returns 401 Unauthorized

🧑‍💻 Example JWT Flow Diagram

+-----------+        +------------+        +-------------+
|   Client  |  --->  |  /login    |  --->  |   JWT Token |
+-----------+        +------------+        +-------------+
       |                     |                     |
       |---- Authorization: Bearer <token> ------> |
       | <-------- Authorized Response ------------|





# Chat-Spring: Simple Chat Room Application with Spring Boot WebSocket and STOMP

Chat-Spring is a simple chat room application built using Spring Boot WebSocket and STOMP (Simple Text Oriented Messaging Protocol). It allows users to join a single chat room and communicate with each other in real-time.

## Features

- **Real-time Messaging**: Utilizes WebSocket and STOMP to enable real-time messaging between users.
- **Single Chat Room**: Users join a single chat room where they can engage in conversations with each other.

## Technologies Used

- **Spring Boot**: Provides the framework for building the application.
- **WebSocket**: Enables bidirectional communication between the client and server.
- **STOMP**: Provides a lightweight messaging protocol for communication over WebSocket.

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 17 + installed on your machine
- Maven for building the project
- Docker

### Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/samezio/Chat-Spring.git
   ```

2. Navigate to the project directory:

   ```bash
   cd Chat-Spring
   ```

3. Compose docker

   ```bash
   docker compose up -d
   ```

4. Access the application at `http://localhost:6868`.

## Usage

1. Navigate to the application URL in your web browser.
2. Start sending messages in the chat room.
3. Interact with other users in real-time.

## Contributing

Contributions are welcome! If you'd like to contribute to this project, please follow these steps:

1. Fork the repository.
2. Create a new branch (`git checkout -b feature/your-feature-name`).
3. Make your changes and commit them (`git commit -am 'Add some feature'`).
4. Push to the branch (`git push origin feature/your-feature-name`).
5. Create a new Pull Request.

## License

This project is licensed under the [MIT License](LICENSE).

## TODO List

[View TODO List](TODO.md)

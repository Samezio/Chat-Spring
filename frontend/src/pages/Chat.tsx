import React, {
  ReactEventHandler,
  SyntheticEvent,
  useEffect,
  useRef,
  useState,
} from "react";
import { Container, Row, Col, Form, Button } from "react-bootstrap";
import "./chat.css";
import { useAuthentication } from "../hooks/AuthenticationProvider";
import { useStomp } from "../hooks/StompProvider";
type Message = {
  content: string;
  sender: string;
  messageType: string;
};
export default function Chat() {
  const { user } = useAuthentication();
  const { client, isConnected, connectionError } = useStomp();
  const [messages, setMessages] = useState<Message[]>([
    { content: "", sender: "Sameer", messageType: "JOIN" },
    { content: "Hello", sender: "Sameer", messageType: "CHAT" },
    { content: "", sender: "Cobra", messageType: "JOIN" },
    { content: "Hii", sender: "Cobra", messageType: "CHAT" },
  ]);

  const chatContainerRef = useRef<HTMLDivElement>(null);
  const [inputValue, setInputValue] = useState("");
  useEffect(() => {
    // Scroll to the bottom of the chat container when messages change
    if (chatContainerRef.current) {
      chatContainerRef.current.scrollTop =
        chatContainerRef.current.scrollHeight;
    }
  }, [messages]);
  useEffect(() => {
    if (isConnected()) {
      if (client) {
        // Subscribe to the Public Topic
        client.subscribe("/topic/public", (payload) => {
          var message = JSON.parse(payload.body) as Message;
          setMessages([...messages, message]);
        });

        // Tell your username to the server
        client.send(
          "/app/chat.addUser",
          {},
          JSON.stringify({ sender: user, messageType: "JOIN" })
        );
      }
    }
  }, [client, isConnected]);
  const handleSubmit = (e: SyntheticEvent) => {
    e.preventDefault();
    if (inputValue.trim() !== "") {
      setMessages([
        ...messages,
        { content: inputValue, sender: user, messageType: "CHAT" },
      ]);
      setInputValue("");
    }
  };

  return (
    <Container>
      <Row className="text-center">
        <h2>Chat Spring</h2>
        <h4 className={`text-${connectionError() ? "danger" : "warning"}`}>
          {isConnected()
            ? ""
            : connectionError()
            ? `${connectionError()}`
            : "Not connected"}
        </h4>
      </Row>
      <Row
        className="chat-container border border-3 rounded"
        ref={chatContainerRef}
      >
        <div className="chat-messages">
          {messages.map((message, index) => (
            <MessageDiv key={index} user={user} message={message} />
          ))}
        </div>
      </Row>
      <Row>
        <Form onSubmit={handleSubmit} className="chat-input">
          <Form.Group controlId="messageInput">
            <Form.Control
              type="text"
              value={inputValue}
              onChange={(e) => {
                setInputValue(e.target.value);
              }}
              placeholder="Type your message..."
            />
          </Form.Group>
          <Button variant="primary" type="submit">
            Send
          </Button>
        </Form>
      </Row>
    </Container>
  );
}

function MessageDiv({ user, message }: { user: string; message: Message }) {
  if (message.messageType === "CHAT") {
    return (
      <div
        className={`text-wrap message ${
          message.sender === user ? "user" : "other"
        } ${message.messageType}`}
      >
        {message.content}
      </div>
    );
  } else {
    return (
      <div className={`message ${message.messageType}`}>
        {message.sender} has{" "}
        {message.messageType === "JOIN" ? "joined" : "left"}
      </div>
    );
  }
}

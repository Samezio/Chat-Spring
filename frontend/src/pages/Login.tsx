import { useState } from "react";
import { useAuthentication } from "../hooks/AuthenticationProvider";
import { Container, Form } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

export default function Login() {
  const { login } = useAuthentication();
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const navigate = useNavigate();
  function register() {
    console.log("register");
  }
  function forgetPassword() {
    console.log("forget");
  }
  return (
    <Container fluid className="animate__animated animate__pulse">
      <h1 className="mt-3">Chat Spring</h1>
      <Form
        onSubmit={(e) => {
          e.preventDefault();
          let username = (e.currentTarget[0] as HTMLInputElement).value;
          let password = (e.currentTarget[1] as HTMLInputElement).value;
          login(username, password)
            .then((success) => {
              if (success) {
                setErrorMessage(null);
                navigate("/chat");
              } else {
                setErrorMessage("Wrong username or password");
              }
            })
            .catch((e) => {
              console.log(e);
              setErrorMessage("Some error occured");
            });
        }}
      >
        <Form.Label className="mt-3">
          <h3>Login</h3>
        </Form.Label>
        <Form.FloatingLabel label="Username">
          <Form.Control className="mt-2" type="text" name="username" />
        </Form.FloatingLabel>
        <Form.FloatingLabel label="Password">
          <Form.Control className="mt-2" type="password" name="password" />
        </Form.FloatingLabel>
        <Form.Control
          className="mt-2"
          type="submit"
          name="submit"
          value="Login"
        />
        <div className="mt-2 text-center">
          <a
            href="#"
            onClick={(e) => {
              e.preventDefault();
              register();
            }}
          >
            register
          </a>{" "}
          <div className="vr"></div> Forgot{" "}
          <a
            href="#"
            onClick={(e) => {
              e.preventDefault();
              forgetPassword();
            }}
          >
            password?
          </a>
        </div>
      </Form>
    </Container>
  );
}

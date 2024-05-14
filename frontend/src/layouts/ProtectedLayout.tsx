import React from "react";
import { Navigate, Outlet, useNavigate } from "react-router-dom";
import { useAuthentication } from "../hooks/AuthenticationProvider";
import { Container } from "react-bootstrap";

export default function ProtectedLayout( {navigateTo} : {navigateTo: string}) {
  const { isAuthenticated } = useAuthentication();
  console.log(`isAuthenticated: ${isAuthenticated()}`);

  if (!isAuthenticated()) {
    return <Navigate to={navigateTo} />;
  } else {
    return (
      <Container fluid>
        <Outlet />
      </Container>
    );
  }
}

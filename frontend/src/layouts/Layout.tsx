import React from "react";
import { Outlet } from "react-router-dom";
import { useAuthentication } from "../hooks/AuthenticationProvider";
import { Container } from "react-bootstrap";

export default function Layout() {
  return (
    <Container fluid>
      <Outlet />
    </Container>
  );
}

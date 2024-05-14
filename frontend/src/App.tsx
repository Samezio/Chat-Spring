import { Navigate, Route, Routes } from "react-router-dom";
import Login from "./pages/Login";
import Chat from "./pages/Chat";
import Layout from "./layouts/Layout";
import ProtectedLayout from "./layouts/ProtectedLayout";

function App() {
  return (
    <Routes>
      <Route path="" element={<ProtectedLayout navigateTo="/login" />}>
        <Route path="" element={<Navigate to="/chat" />} />
        <Route path="/chat" element={<Chat />} />
      </Route>
      <Route path="/" element={<Layout />}>
        <Route path="login" element={<Login />} />
      </Route>
    </Routes>
  );
}

export default App;

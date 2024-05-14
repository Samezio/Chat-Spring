import React, { createContext, useContext, useMemo } from "react";
import useLocalStorage from "./useLocalStorage";

export type IAuthenticationContext = {
  user: string;
  token: string;
  login: (username: string, password: string) => Promise<boolean>;
  logout: () => void;
  getAuthorization: () => string;
  isAuthenticated: () => boolean;
};
const DEFAULT_AUTHENTICATION_CONTEXT: IAuthenticationContext = {
  user: "",
  token: "",
  login: async () => false,
  logout: () => {},
  getAuthorization: () => "",
  isAuthenticated: () => false,
};
const AuthenticationContext = createContext<IAuthenticationContext>(
  DEFAULT_AUTHENTICATION_CONTEXT
);

export default function AuthenticationProvider({
  serverUrl,
  children
}: React.PropsWithChildren<{
  serverUrl: string
}>) {
  const [user, setUser] = useLocalStorage<string>("user", "");
  const [token, setToken] = useLocalStorage<string>("web-token", "");

  function login(username: string, password: string): Promise<boolean> {
    return fetch(`${serverUrl}/user/login`, {
      method: 'POST',
      headers: {
        'content-type': 'application/json'
      },
      body: JSON.stringify({
        username: username, password:password
      })
    }).then(response=> {
      if(response.status === 200) {
        return response.text();
      }
      throw Error(`Response Error: [${response.status}] ${response.statusText}`)
    }).then(setToken).then(_=>{
      setUser(username);
      return true;
    })
    .catch(e=>{
      console.log(e);
      return false;
    })
  }
  function logout() {
    setUser("")
    setToken("")
  }
  const value: IAuthenticationContext = useMemo<IAuthenticationContext>(()=>{
    return {
      user, token, login, logout,
      getAuthorization: () => `${user} ${token}`,
      isAuthenticated: () => user !== "" && token !== ""
    }
  }, [user, token])
  return (
    <AuthenticationContext.Provider value={value}>
      {" "}
      {children}{" "}
    </AuthenticationContext.Provider>
  );
}
export function useAuthentication() {
  return useContext(AuthenticationContext);
}

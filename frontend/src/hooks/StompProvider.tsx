import {
  createContext,
  PropsWithChildren,
  useContext,
  useMemo,
  useState,
} from "react";
import Stomp from "stompjs";import SockJsClient from 'sockjs-client';
import { useAuthentication } from "./AuthenticationProvider";

type IStompContext = {
  client: Stomp.Client | null;
  isConnected: () => boolean;
  connectionError: () => Error | string | Stomp.Frame | undefined | null;
};
const DEFAULT_STOMP_CONTEXT = {
  client: null,
  isConnected: () => false,
  connectionError: () => Error("Not initialised"),
};
const StompContext = createContext<IStompContext>(DEFAULT_STOMP_CONTEXT);

export default function StompProvider({
  websocketUrl,
  children,
}: PropsWithChildren<{
  websocketUrl: string;
}>) {
  const { user, token, isAuthenticated } = useAuthentication();
  const [connected, setConnected] = useState<boolean>(false);
  const [connectionError, setConnectionError] = useState<
    Error | string | Stomp.Frame | undefined | null
  >(undefined);
  const value = useMemo(() => {
    if (isAuthenticated()) {
      const sockJsClient = new SockJsClient(websocketUrl);
      var client = Stomp.over(sockJsClient);
      client.connect(
        {
          login: user,
          passcode: token,
        },
        () => setConnected(true),
        setConnectionError
      );
      return {
        client,
        isConnected: () => connected,
        connectionError: () => connectionError,
      };
    } else {
      return DEFAULT_STOMP_CONTEXT;
    }
  }, [user, token]);
  return (
    <StompContext.Provider value={value}>{children}</StompContext.Provider>
  );
}
export function useStomp() {
  return useContext(StompContext);
}

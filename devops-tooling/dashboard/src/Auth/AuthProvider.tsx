import { useState } from "react";
import AuthContext from "./AuthContext";
import AuthService from "./AuthService";


export default function AuthProvider({ children }: { children: React.ReactNode }) {
    const [user, setUser] = useState<any>(null);
  
    const signin = (newUser: string, callback: VoidFunction) => {
      setUser(newUser);
      AuthService.signIn(newUser);
      callback();
    };
  
    const signout = (callback: VoidFunction) => {
      AuthService.signOut();
      window.location.href = '/';
      callback();
    };
  
    const value = { user, signin, signout };
  
    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
  }
  
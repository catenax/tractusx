import { useState } from "react";
import AuthContext from "./AuthContext";
import AuthService from "./AuthService";


export default function AuthProvider({ children }: { children: React.ReactNode }) {
    const [user, setUser] = useState<any>(null);
  
    const signIn = (newUser: string, callback: VoidFunction) => {
      setUser(newUser);
      AuthService.signIn(newUser);
      callback();
    };
  
    const signOut = (callback: VoidFunction) => {
      AuthService.signOut();
      window.location.href = '/';
      callback();
    };
  
    const value = { user, signIn, signOut };
  
    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
  }
  
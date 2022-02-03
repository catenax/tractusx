/* eslint-disable no-unused-vars */
/* eslint-disable no-undef */
interface AuthContextType {
    user: any;
    signIn: (user: string, callback: VoidFunction) => void;
    signOut: (callback: VoidFunction) => void;
    isAdmin: () => boolean;
  }

export default AuthContextType;

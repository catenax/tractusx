export default interface AuthContextType {
    user: any;
    signIn: (user: string, callback: VoidFunction) => void;
    signOut: (callback: VoidFunction) => void;
    isAdmin: () => boolean;
  }

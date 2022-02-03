import { createContext } from 'react';
import AuthContextType from './AuthContextType';

const AuthContext = createContext<AuthContextType>(null!);

export default AuthContext;

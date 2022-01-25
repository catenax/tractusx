import { Navigate, useLocation } from "react-router-dom";
import useAuth from "./useAuth";
import Unauthorized from '../pages/Errors/Unauthorized';

export default  function RequireAuth({ children, needAdminRights = false }: { children: JSX.Element, needAdminRights?: boolean }) {
  const auth = useAuth();
  const location = useLocation();

  if (!auth.user) {
    // Redirect them to the /login page, but save the current location they were
    // trying to go to when they were redirected. This allows us to send them
    // along to that page after they login, which is a nicer user experience
    // than dropping them off on the home page.

    return <Navigate to="/" state={{ from: location }} replace={false}/>;
  }

  if (needAdminRights && !auth.user.isAdmin) {

    return <Unauthorized />
  }

  return children;
}
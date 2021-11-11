import Keycloak from "keycloak-js";

//TODO: go to company selection if no url parameter for company is specified
const searchParams = new URLSearchParams(window.location.search);
const realm = searchParams.get('company') || 'Catena-X';
const oneid = searchParams.get('oneId') || 'CAXABCDEFGHIJKLM';
const user = searchParams.get('user') || '';

const _kc = new Keycloak({
  "url": process.env.REACT_APP_KEYCLOAK_URL,
  "realm": realm,
  "clientId": `client-${realm.toLowerCase()}`,
  "ssl-required": "external",
  "public-client": true,
  "oneid": oneid
});

//const _kc = new Keycloak('/keycloak.json');

/**
 * Initializes Keycloak instance and calls the provided callback function if successfully authenticated.
 *
 * @param onAuthenticatedCallback
 */
const initKeycloak = (onAuthenticatedCallback) => {
  _kc.init({
    onLoad: 'login-required',
    silentCheckSsoRedirectUri: window.location.origin + '/silent-check-sso.html',
    pkceMethod: 'S256',
  })
    .then((authenticated) => {
      // if (authenticated) {
      onAuthenticatedCallback();
      // } else {
      //   doLogin();
      // }
    })
};

const doLogin = _kc.login;

const doLogout = _kc.logout;

//forward as header "authentication: Bearer ${getToken()}"
const getToken = () => _kc.token;

const isLoggedIn = () => !!_kc.token;

const updateToken = (successCallback) =>
  _kc.updateToken(5)
    .then(successCallback)
    .catch(doLogin);

const getUsername = () => _kc.tokenParsed?.preferred_username;

const getInitials = () => _kc.tokenParsed?.preferred_username.split(/[.@]/).reduce((a,b) => a+b[0],'').substring(0,2).toUpperCase();

const getDomain = () => realm;//_kc.tokenParsed?.split('/').pop();

const getOneid = () => oneid;

const hasRole = (roles) => roles.some((role) => _kc.hasRealmRole(role));

const UserService = {
  initKeycloak,
  doLogin,
  doLogout,
  isLoggedIn,
  getToken,
  updateToken,
  getUsername,
  getInitials,
  getDomain,
  hasRole,
  realm,
  oneid
};

export default UserService;

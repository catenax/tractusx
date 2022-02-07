import Keycloak from 'keycloak-js';

const realm = 'CX-Central';
const clientId = 'catenax-portal';
localStorage.setItem('clientId', clientId);

const _kc = new Keycloak({
  "url": process.env.REACT_APP_KEYCLOAK_URL,
  "realm": "CX-Central",
  "clientId": "catenax-portal",
  "ssl-required": "external",
  "public-client": true
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

const getParsedToken = () => _kc.tokenParsed;

const isLoggedIn = () => !!_kc.token;

const updateToken = (successCallback) =>
  _kc.updateToken(5)
    .then(successCallback)
    .catch(doLogin);

const getUsername = () => _kc.tokenParsed?.preferred_username;

const getName = () => _kc.tokenParsed?.name;

const getInitials = () => _kc.tokenParsed?.preferred_username.split(/[.@]/).reduce((a,b) => a+b[0],'').substring(0,2).toUpperCase();

const getDomain = () => realm;//_kc.tokenParsed?.split('/').pop();

const getCompany = () => _kc.tokenParsed?.email.split('@')[1].split('.')[0].toUpperCase();

const hasRole = (roles) => roles.some((role) => _kc.hasRealmRole(role));

const UserService = {
  initKeycloak,
  doLogin,
  doLogout,
  isLoggedIn,
  getToken,
  getParsedToken,
  updateToken,
  getUsername,
  getName,
  getInitials,
  getDomain,
  getCompany,
  hasRole,
  realm,
  clientId
};

export default UserService;

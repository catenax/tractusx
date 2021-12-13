export default class AuthService {

  static signIn(username){
    localStorage.setItem('username', username);
  }

  static signOut(){
    localStorage.removeItem('username');
  }

  static isSignedIn(){
    return localStorage.getItem('username') !== null;
  }

}
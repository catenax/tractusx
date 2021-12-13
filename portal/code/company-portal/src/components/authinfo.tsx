// Copyright (c) 2021 Microsoft, BMW, Catena-X
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

import * as React from 'react';
import { observer } from 'mobx-react';
import { observable } from 'mobx';
import UserService from '../helpers/UserService';
import '../styles/authinfo.css';

@observer
class Authinfo extends React.Component {
  @observable username = '';
  @observable initials = '';
  @observable token = '';

  public async componentDidMount() {
    this.username = UserService.getUsername();
    this.initials = UserService.getInitials();
    this.token = UserService.getToken();
    console.log(this.token);
  }

  public parseJwt(token) {
    try {
      const base64Url = token.split('.')[1];
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
      const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
          return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
      }).join(''));
      return JSON.parse(jsonPayload);
    } catch (e) {
      return e;
    }
  }

  public render() {
    return (
      <div>
        <pre>{this.username} - {this.initials}</pre>
        <pre>{JSON.stringify(this.parseJwt(this.token), null, 4)}</pre>
        <pre>{this.token}</pre>
      </div>
    );
  }
}

export default Authinfo;
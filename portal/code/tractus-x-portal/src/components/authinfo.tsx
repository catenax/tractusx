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
import UserService from '../helpers/UserService';
import '../styles/authinfo.css';

class Authinfo extends React.Component {

  public render() {
    return (
      <div>
        <pre>JWT Token - {UserService.getParsedToken().name}</pre>
        <pre>{JSON.stringify(UserService.getParsedToken(), null, 4)}</pre>
        <pre>{UserService.getToken()}</pre>
      </div>
    );
  }

}

export default Authinfo;
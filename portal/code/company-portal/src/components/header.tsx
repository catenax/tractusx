// Copyright (c) 2021 Microsoft
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
import i18n from '../i18n';
import { observer } from 'mobx-react';
import { observable } from 'mobx';
import UserService from '../helpers/UserService';
import { Icon } from '@fluentui/react';
import { AppState } from '../stores/appstate';
import { RouteComponentProps, withRouter } from 'react-router-dom';
import Logo from './logo';
import { getUserClientRolesComposite } from '../helpers/utils';
interface IProp extends RouteComponentProps {
  href: string;
  hidePivot?: boolean;
  appTitle?: string;
}

const changeLanguage = (ln: string) => {
  return () => {
    i18n.changeLanguage(ln);
  }
}

@observer
class Header extends React.Component<IProp> {
  @observable username = '';
  @observable initials = '';
  @observable selectedKey = '';
  @observable isAdmin = false;
  @observable userRoles = [];

  public async componentDidMount() {

    this.username = UserService.getUsername();
    this.initials = UserService.getInitials();
    this.userRoles = await getUserClientRolesComposite();
    AppState.state.isAdmin = true;

    this.isAdmin = AppState.state.isAdmin;
  }


  private homeClick(): void {
    this.props.history.push('/home/onboarding');
  }

  private userClick() {
    //const token = adalContext.getCachedToken();
    const token = UserService.getToken();
    console.log(token);
  }

  private logoutClick() {
    const token = UserService.getCachedToken();
    console.log(token);
    UserService.logOut();
  }

  public render() {

    return (
      <div className='w100pc minh80 df aic bgwhite'>
        <div className='df cpointer' onClick={() => this.homeClick()}>
          <Logo />
        </div>
        {this.props.appTitle && <div className='df aic'>
          <Icon className='fs14 bold fgblack' iconName='ChromeMinimize' />
          <div className='fs16 bold fgblack ml10 mb4'>{this.props.appTitle}</div>
        </div>}
        <div className='flex1' />
        <div className='flex1' />
        <div className='bgblue fgwhite aic jcc df fs16 br50pc h40 w40 mr10' onClick={() => this.userClick()}>{this.initials}</div>
        <div className='mr20'>
          <button onClick={changeLanguage("en")}>EN</button>
          <button onClick={changeLanguage("de")}>DE</button>
        </div>
        <div className='df fdc mr50'>
          <span className='fs14'>{this.username}</span>
          <div className='df'>
            <span className='fs14'>{UserService.getDomain()}</span>
            <span className='ml5 fs14'>({this.userRoles.join(", ")})</span>
          </div>
        </div>
        <div className='df mr50 fg cpointer'>
          <span onClick={() => this.logoutClick()}>Logout</span>
        </div>
      </div>
    );
  }
}

export default withRouter(Header);

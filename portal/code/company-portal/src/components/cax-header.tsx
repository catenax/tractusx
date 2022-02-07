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
import { Row, Col } from 'react-bootstrap';
import UserService from '../helpers/UserService';
import { RouteComponentProps, withRouter } from 'react-router-dom';
import { getUserClientRolesComposite } from '../helpers/utils';

interface IProp extends RouteComponentProps {
    href: string;
    hidePivot?: boolean;
    appTitle?: string;
}

@observer
class Header extends React.Component<IProp> {
    @observable username = '';
    @observable initials = '';
    @observable userRoles = [];
    @observable language = i18n.language;

    public async componentDidMount() {
        this.username = UserService.getUsername();
        this.initials = UserService.getInitials();
        this.userRoles = await getUserClientRolesComposite();
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

        const changeLanguage = lng => {
            this.language = lng;
            i18n.changeLanguage(lng);
        };

        return (

            <Row className='header-container' >
                <Col>
                    <div className='logo'>
                        <img src='/logo_cx.svg' alt='logo' />
                    </div>
                </Col>
                <Col>
                    <div className='d-flex flex-row-reverse profile'>
                        <div className='profile-lang'><span className={this.language === 'en' ? 'lang-sel' : ''} onClick={() => changeLanguage('en')} > EN </span></div>
                        <div className='profile-lang'><span className={this.language === 'de' ? 'lang-sel' : ''} onClick={() => changeLanguage('de')} > DE</span></div>
                        <div className='profile-link partion'></div>
                        <div className='profile-link user'>
                            <input id="myid" type="checkbox" />
                            <label htmlFor="myid" className='user-icon'></label>
                            <span className="tooltiptext"> <div> {this.username}</div>
                                < div > {UserService.getDomain()}</div>
                                <div>({this.userRoles.join(", ")})</div>
                                <div className='logout' onClick={() => this.logoutClick()}>Logout</div>
                            </span>
                        </div>
                        {/* <div className='profile-link user'><span className='user-icon'></span>
                            <span className="tooltiptext">Tooltip text</span>
                        </div> */}
                        <div className='profile-link'><a href='/help'>Help</a></div>
                    </div>
                </Col >
            </Row >
        );
    }
}
export default withRouter(Header);

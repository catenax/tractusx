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
import {observer} from 'mobx-react';

@observer
export default class Help extends React.Component {

    public render() {
        return (
            <div className='w100pc h100pc df fdc'>
                <div className='ml50 mr50 mt50 pb35 bgwhite w100-100 df fdc'>
                    <span className='fs20 bold ml50 mt20'>FAQ for Company Admins</span>
                    <span className='fs18 ml50 mt20'>How to change the administrator user roles?</span>
                    <span className='fs14 ml50 mt5'>If the current user-administrator is changing responsibilities within the company or leaving the company then the user-administrator rights should be passed on to another person.</span>
                    <span className='fs14 ml50 mt5'>Following steps need to get processed:</span>
                    <div className='ml50 mt5'>
                        <ol className='fs14 liststyleauto'>
                            <li>Add new user</li>
                            <li>Update user roles</li>
                        </ol>
                    </div>
                    <span className='fs20 bold ml50 mt20'>FAQ for Active Participants</span>
                    <span className='fs18 ml50 mt20'>Technical requirements to provide Apps?</span>
                    <span className='fs14 ml50 mt5'>To be able to use the full Catena-X platform, the user needs to use one of the following Browsers:</span>
                    <div className='ml50 mt5'>
                        <ul className='fs14 liststyledisc'>
                            <li>Chrome</li>
                            <li>Firefox</li>
                            <li>Microsoft Edge</li>
                        </ul>
                    </div>
                </div>
            </div>
        );
    }
}

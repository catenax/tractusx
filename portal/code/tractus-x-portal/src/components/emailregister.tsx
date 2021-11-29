// Copyright (c) 2021 Microsoft
//
// Licensed under the Apache License, Version 2.0 (the 'License');
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an 'AS IS' BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

import * as React from 'react';
import { observer } from 'mobx-react';
import { TextField } from '@fluentui/react';
import { PrimaryButton } from '@fluentui/react';
import { RouteComponentProps } from 'react-router';

@observer
export default class Emailregister extends React.Component<RouteComponentProps> {

private onLoginClick() {
    this.props.history.push('/home/register')
}

  public render() {
    return (
      <div className='w100pc h100pc df fdc bgimage'>
          <div className="w60pc bgwhite df fdc ml60 mt50 br15">
          <span className='fs20 bold mt20 ml30'>Catena-X Automotive network</span>
          <span className='fs60 bold mt10 ml30'>The gateway to a Digital Economy.</span>
          <span className='fs14 mt30 ml30'>New to Catena-X, and you want to participate? Enter your email and we send you invite to Catena-X - Portal</span>
          <span className='fs14 mt30 ml30 mb50'>
                    <TextField placeholder='Enter your Email - Address' className='w50pc brnone br4 pr10 h36' defaultValue='' />
          </span>
          </div>
          <div className='ml60 mt10'>
          <PrimaryButton text='GO TO PORTAL' className='p24 br5'  onClick={() => this.onLoginClick()}/>
          </div>
      </div>
    )} 
}
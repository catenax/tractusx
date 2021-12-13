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
import { observable } from 'mobx'
import { observer } from 'mobx-react';
import { TextField } from '@fluentui/react';
import { PrimaryButton, IDropdownOption, Dropdown, Icon } from '@fluentui/react';
import { ToastContainer, toast } from 'react-toastify';
import { getUserRoles } from '../helpers/utils';
import UserService from '../helpers/UserService';

interface IUserResponsibilities {
  id: number,
  eMail: string,  
  role: string,
  message: string
}

var options: IDropdownOption[] = [];

@observer
export default class Responsibilities extends React.Component {
  @observable private email: string = "";
  @observable private userRole: any = "";
  @observable private newarray: IUserResponsibilities[] = [];
  @observable private message: string = "";
  public newUserRole: IDropdownOption[];

  constructor(props) {
    super(props);
    this.state = { userRole: ''};
  }
  public userRoles: any;
  async componentDidMount() {
    const onboarding = window.localStorage.getItem('onboarding');
    console.log("responsibilities", onboarding)
    try {
      this.userRoles = await getUserRoles();
      console.log(this.userRoles);
      this.newUserRole = this.userRoles.map(x => { return { key: x, text: x } });
      Object.assign(options, this.newUserRole)
      console.log(options);
    } catch {

    }
  }

  private addButtonClick() {
    if (this.email === "" || this.userRole === "") {
      toast.error('Email or User Role empty.');
      return;
    }
    const data =
    {
      "id": Math.floor(Math.random() * 100),
      "eMail": this.email,      
      "role": this.userRole,
      "message": this.message
    }
    this.newarray.push(data);
    this.email = '';    
    this.userRole = '';
    this.message = '';
  }

  private removeUser(id: number) {
    this.newarray = this.newarray.filter(x => x.id !== id);
  }

  private sendInvites() {
    const realm = UserService.realm;
    const token = UserService.getToken();
    const url = process.env.REACT_APP_ONBOARDING_URL;
    const endpoint = process.env.REACT_APP_ONBOARDING_ENDPOINT;
    const u = `${url}/${endpoint}/${realm}/users`;
    const data = this.newarray.map(({ id, ...rest }) => ({ ...rest }));
    if (data.length > 0) {
      fetch(u, { method: 'POST', headers: { 'Authorization': `Bearer ${token}`, 'Content-Type': 'application/json' }, body: JSON.stringify(data) })
        .then((response) => {
          if (response.ok) {
            toast.success('Sent Invite');
          }
          else throw Error();
        }

        ).catch((error) => {
          toast.error('Unable to sent invite')
        });
    } else {
      toast.error('Email or User Role empty.')
    }

  }

  public render() {
    return (
      <div className='mb10'>
        <input className='collapse-open' type='checkbox' id='collapse-2' />
        <label className='collapse-btn bgwhite' htmlFor='collapse-2'>
          <div className='fl fs22 dblock pr20 bold'>2</div>
          <div className='df fdc'>
            <span className='fs22 bold'>Responsibilities &amp; Admin Account</span>
            <div className='fs14 mt10'>Distribute responsibilities and add admin account for the persons that know the best what needs to be done.
            </div>
          </div>
        </label>
        <div className='collapse-panel bgwhite'>
          <div className='pb20 p24'>
            <div className='ml30 fb pb6 df'>
              <TextField label='Email Address' className='w50pc brnone br4 pr10 h36' value={this.email} onChange={(ev, val) => this.email = val} />              
              <Dropdown placeholder="User Role" label="User Role" options={options} className='w50pc brnone br4 pr10 h36' onChange={(ev, val) => this.userRole = val.key} selectedKey={this.userRole} />
              <TextField label='Message' className='w50pc brnone br4 pr10 h36' value={this.message} onChange={(ev, val) => this.message = val} />
              <PrimaryButton className='w10pc mt24 br4 h36' text='ADD' onClick={() => this.addButtonClick()} />
            </div>
            <div className='ml30 mt10 df'>
              {this.newarray.map(d => {
                return (<div key={d.id} className='bgcda br15 pl10 pr10 pt3 pb3 mr15 jcc aic df'> <Icon className='fgblack fs14 bold mr5' iconName='mail' />
                  <span className='mr5'>{d.eMail} ({d.role}) </span> <Icon className='fgblack fs14 bold mr5 ' iconName='BoxMultiplySolid' onClick={() => this.removeUser(d.id)} /> </div>)
              })
              }
            </div>
          </div>
          <div className='ml30 pb8 mt50 p24 pb20 brbt df fdc fdrr'>
            <PrimaryButton text='SEND INVITE(S)' onClick={() => this.sendInvites()} />
          </div>
        </div>
        <ToastContainer />
      </div>
    )
  }
}
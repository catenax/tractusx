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
import { observer } from 'mobx-react';
import { PrimaryButton, Dropdown, IDropdownOption, TextField, SearchBox } from '@fluentui/react';
import UserService from '../helpers/UserService';
import User from '../data/user';
import { observable } from 'mobx';
import { compare } from '../helpers/utils';

@observer
export default class AddUser extends React.Component {
  @observable private users: User[] = [];
  @observable private filteredUsers: User[] = [];
  @observable private email = '';
  @observable private fullname = '';
  @observable private message = '';
  @observable private searchText = '';

  async componentDidMount() {
    try { this.users = await this.readPeople(); } catch { }
    this.searchChange(this.searchText);
  }
  
  private readPeople(searchText?: string): Promise<any> {
    const promise = new Promise<any>((resolve, reject) => {
      /*
      adalContext.acquireToken('https://graph.microsoft.com').then((token) => {
        const query = searchText ? `?$filter=startswith(displayName, '${searchText}')&$top=25` : '';
        const u = `https://graph.microsoft.com/v1.0/users${query}`;
        fetch(u, { headers: { 'Authorization': `Bearer ${token}`, 'Content-Type': 'application/json' } })
          .then((val) => val.json().then((data) => {
            if (val.ok) {
              const users: User[] = [];
              for (const user of data.value) {
                const usr = new User();
                Object.assign(usr, user);
                users.push(usr);
              }

              users.sort((a, b) => compare((a.mail || a.userPrincipalName), (b.mail || b.userPrincipalName)));
              resolve(users);
            } else {
              reject(val.statusText);
            }
          }).catch((error) => {
            console.log(error.message);
            reject(error.message);
          }))
      });
      */
    });
  
    return promise;
  }
  
  private addUser(email?: string): Promise<any> {
    const promise = new Promise<any>((resolve, reject) => {
      /*
      adalContext.acquireToken('https://graph.microsoft.com').then((token) => {
        const body = {
          invitedUserEmailAddress: email, invitedUserDisplayName: this.fullname,
          inviteRedirectUrl: 'https://catenax.azurewebsites.net', sendInvitationMessage: true
        };
        const u = 'https://graph.microsoft.com/v1.0/invitations';
        fetch(u, { method: 'POST', headers: { 'Authorization': `Bearer ${token}`, 'Content-Type': 'application/json' }, body: JSON.stringify(body) })
          .then((val) => val.json().then((data) => {
            if (!val.ok) {
              reject(val.statusText);
            } else {
              resolve(data.invitedUser.id);
            }
          }).catch((error) => {
            console.log(error.message);
            reject(error.message);
          }))
      });
      */
    });
  
    return promise;
  }
  //fe930be7-5e62-47db-91af-98c3a49a38b1
  private makeAdmin(id?: string): Promise<any> {
    const promise = new Promise<any>((resolve, reject) => {
      /*
      adalContext.acquireToken('https://graph.microsoft.com').then((token) => {
        const body = {'@odata.id': `https://graph.microsoft.com/v1.0/directoryObjects/${id}`};
        const u = 'https://graph.microsoft.com/v1.0/groups/463512e5-968f-4b2d-8283-737be4a67182/members/$ref';
        fetch(u, { method: 'POST', headers: { 'Authorization': `Bearer ${token}`, 'Content-Type': 'application/json' }, body: JSON.stringify(body) })
          .then(async (val) => {
            if (!val.ok) {
              console.log(val, await val.json());
              reject(val.statusText);
            } else {
              resolve('');
            }
          }).catch((error) => {
            console.log(error.message);
            reject(error.message);
          })
      });
      */
    });
  
    return promise;
  }
  
  private async addClick() {
    if (this.email) {
      this.message = '';
      try {
        await this.addUser(this.email);
        this.users = await this.readPeople();
        this.searchChange(this.searchText);
        // try {
        //   await this.makeAdmin(id);
        // } catch (e) {
        //   console.log(e);
        // }
        this.message = `${this.email} was sent an invitation`;
        this.email = '';
      } catch(error) {
        this.message = 'Error sending invitation - ' + error;
      }
    }
  }

  searchChange(text: string): void {
    this.searchText = text.toLowerCase();
    this.filteredUsers = this.users.filter((u) => (u.displayName || '').toLowerCase().includes(this.searchText)
      || (u.mail || u.userPrincipalName || '').toLowerCase().includes(this.searchText));
  }

  public render() {

    const statusOptions: IDropdownOption[] = [
      { key: 'active', text: 'Active' },
      { key: 'inactive', text: 'InActive' }
    ];

    return (
      <div className='w100pc h100pc df fdc'>
        <div className='w100-60 bgwhite df fdc ml30 mt30'>
          <span className='fs20 bold ml30 mt20'>Add new user</span>
          <span className='fs14 pb20 ml30 mt10'>
            To add a new user, please enter the following data,
            <span className='bold fglgreen ml5'>UPLOAD CSV FILE </span>
            or integrate your organization's
            <span className='bold fglgreen ml5'>IDENTITY PROVIDER</span>
          </span>
          <div className='pb8 df fdc'>
            <TextField placeholder='Full name' className='ml30 br0 br4 h36 w50-40 mb10' value={this.fullname} onChange={(ev, val) => this.fullname = val} />
            <div className='pb6 ml30 df w100-60'>
            <TextField placeholder='Email address' className='flex2 br0 br4 pr10 h36' value={this.email} onChange={(ev, val) => this.email = val} />
              <Dropdown className='pr10 flex1' disabled placeholder='Role' options={statusOptions} />
              <Dropdown className='flex1' disabled placeholder='Team' options={statusOptions} />
            </div>
            <div className='df w100-30 aic'>
              <span className='ml30 bold fggreen fs16'>{this.message}</span>
              <div className='flex1' />
              <PrimaryButton className='fs14 bold mb20 minw200' text='SEND INVITE' disabled={!this.email || !this.fullname} onClick={()=>this.addClick()} />
            </div>
          </div>
        </div>
        <div className='pb5pc' />
        <div className='w100pc bgf5 flex1 df fdc ml30'>
          <span className='fs14 fggrey w40pc'><SearchBox className='bcwhite' placeholder='Search' value={this.searchText}
            onChange={(ev, text) => this.searchChange(text)} /></span>
          <div className='df bgf5 mb15 mt20 w100-60'>
            <span className='fs16 bold ml10 mr5 flex3'>User management</span>
            <span className='fs14 fggrey mr5 flex1'>Sort by:  <span className='bold'>email address</span></span>
            <span className='fs14 fggrey mr5 flex1'>Filter:  <span className='bold'>none</span></span>
          </div>
          <div className='df mb5 w100-60'>
            <span className='fs14 fggrey ml10 mr5 flex3'>Email address</span>
            <span className='fs14 fggrey mr5 flex2'>Full Name</span>
            <span className='fs14 fggrey mr5 flex1'>Company</span>
            <span className='fs14 fggrey flex1'>Status</span>
          </div>
          <div className='df fdc oa'>
          {this.filteredUsers.map((c, index) => (
            <div key={index} className='df bgwhite h36 mb5 w100-60'>
              <span className='fs14 bold mr5 ml10 mt7 flex3 minw100'>{c.mail || c.userPrincipalName}</span>
              <span className='fs14 mr5 mt7 flex2'>{c.displayName}</span>
              <span className='fs14 mr5 mt7 flex1'>{UserService.getDomain()}</span>
              <div className='flex1 df'>
                <span className='dot mt12 bggraphgreen' />
                <span className='fs14 mt7 pl5 flex1'>Active</span>
              </div>
            </div>
          ))}
        </div>
        </div>
      </div>
    );
  }
}

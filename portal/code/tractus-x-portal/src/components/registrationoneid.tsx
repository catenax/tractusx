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
import { observable } from 'mobx';
import { observer } from 'mobx-react';
import { PrimaryButton, TextField } from '@fluentui/react';
import { ToastContainer, toast } from 'react-toastify';
import Logo from './logo';
import 'react-toastify/dist/ReactToastify.css';
// import 'react-toastify/dist/ReactToastify.minimal.css';

@observer
export default class Registrationoneid extends React.Component {

  @observable private email: string = "";
  @observable private oneId: string = "CAXLZJVJEBYWYYZZ";

  private registrationButtonClick() {
    console.log("register");

    var u = 'https://catenax-dev003-app-invitation-service.azurewebsites.net/api/invitation'

    var data =
    {
      "oneId": this.oneId,
      "eMail": this.email
    }

    if (this.email === "" || this.oneId === "") {
      toast.error('Email or OneId empty.');
      return;
    }


    console.log(data);

    fetch(u, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(data) })
      .then((response) => {
        if (response.ok) {
          toast.success('Onboarding for company id: ' + this.oneId + ' started.');
        }
        else throw Error();
      }

      ).catch((error) => {
        toast.error('Onboarding for company id: ' + this.oneId + ' failed. Company is already registered.')
      });

  }


  public render() {
    return (

      <div className='w100pc h100pc df fdc bge1 bgregisterimage ds'>
        <Logo />
        <div className='df mt100'>
          <div className='ml40 flex1 df jcc'>
            <div className='df fdc'>
              <span className='fs55 bold fg191 lh67'>Welcome</span>
              <span className='fs55 bold fg191 lh67'>to Catena-X</span>
              <span className='fs24 bold fg191 lh29'>Automotive Network</span>
              <span><p className='fs14 fw600 fg191 lh20 mt30'>If you have problems, please <br></br>contact our support.</p></span>
              <span className='h40 aic df pt24'><p className='fs18 bold fg191 lh40'>HELP? CONTACT US.</p></span>
              <span className='h20 aic df pt24'><p className='fs18 bold fg191 lh40'>MEMBER? LOGIN.</p></span>
            </div>
          </div>
          <div className='df fdc flex1 aic'>
            <div className="bgwhite w70pc br7 bsdatacatalog">
            <div className='m40 aic'>
              <span className='fs20 bold mt20'>Register to Catena-X</span>
            </div>
            <div className='mr40 ml40 aic'>
              <TextField placeholder='Enter  Email - Address' className='w100pc br4 h40' value={this.email} onChange={(ev, val) => this.email = val} />
              <TextField placeholder='One ID' className='w100pc br4 h40 mt10' value={this.oneId} onChange={(ev, val) => this.oneId = val} />
              <PrimaryButton className='w100pc br4 pr10 h40 mt20' text='REGISTER' onClick={() => this.registrationButtonClick()} />
            </div>
            <div className='m40 df fdc'>
              <div className='df jcc'>
                <img src='/logo_gaiaX.png' alt='logo' />
              </div>
            </div>
            <div className='ml40'>
              </div>

            </div>
          </div>
          <div className='w100' />
        </div>
        <ToastContainer />
      </div>
    )
  }
}
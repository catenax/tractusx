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
import { ToastContainer } from 'react-toastify';
import Logo from './logo';
import 'react-toastify/dist/ReactToastify.css';

@observer
export default class Registration extends React.Component {

    @observable private email: string = "";

    public render() {
        return (

            <div className='w100pc h100pc df fdc bge1 bgregisterimage ds'>
                <Logo />
                <div className='df mt70'>
                    <div className='ml40 flex1 df jcc'>
                        <div className='df fdc jcc'>
                            <span className='fs55 bold fg191 lh67'>JOIN</span>
                            <span className='fs55 bold fg191 lh67'>Catena-X</span>
                            <span className='fs24 bold fg191 lh29'>Automotive Network</span>
                            <span><p className='fs14 fw600 fg191 lh20 mt30'>If you have problems, please <br></br>contact our support.</p></span>
                            <span className='h40 aic df pt24'><p className='fs18 bold fg191 lh40'>HELP? CONTACT US.</p></span>
                            <span className='h20 aic df pt24'><p className='fs18 bold fg191 lh40'>MEMBER? LOGIN.</p></span>
                        </div>
                    </div>
                    <div className='df fdc flex1 aifs jcc'>
                        <div className="bgwhite w70pc br7 bsdatacatalog">
                            <div className='m40 aic'>
                                <span className='fs20 bold mt20'>Register your company</span>
                            </div>
                            <div className='mb20 ml40 aic'>
                                <p className='fs14 mt20'>To register and join Catena-X automotive</p>
                                <p className='fs14'>network, please enter uour passsword we send</p>
                                <p className='fs14'>to you via SMS</p>
                            </div>
                            <div className='mr40 ml40 aic'>
                                <div className='w100pc bold border-y pt10 pb10'>
                                    <div>
                                        <span className='mr10'>Company</span>
                                        <span>xAMPLcorp</span>
                                    </div>
                                    <div>
                                        <span className='mr10'>Email</span>
                                        <span>dorrian.s.grey@xanplcorp.com</span>
                                    </div>
                                    <div>
                                        <span>One-ID</span>
                                        <span>123102355555</span>
                                    </div>
                                </div>
                                <div className='w100pc br4'>
                                <TextField placeholder='Password' className='w100pc br4 h40 mt10' />
                                <PrimaryButton className='w100pc br4 pr10 h40 mt10' text='REGISTER' />
                                </div>
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
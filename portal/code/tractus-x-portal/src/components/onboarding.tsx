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
import { PrimaryButton, DefaultButton } from '@fluentui/react';


@observer
export default class Onboarding extends React.Component {

  public render() {
    return (
      <div className='w100pc h100pc df fdc'>
        <div className='ml50 mr50 mt50 bgfe w100-100 df fdc'>
          <span className='fs20 bold mt20'>Welcome to Catena-X</span>
          <span className='fs14 mt30'>Please finish the company onboarding by finishing the following tasks to actively participate and use all functions in the Catena-X Automotive Network Portal. Most of the tasks can be done in parallel.</span>
        </div>
        <div className='ml50 mr50 mt30 bgfe w100-100 df fdc'>
          <div className='collapse-list'>
            <div className='mb10'>
              <input className='collapse-open' type='checkbox' id='collapse-1' />
              <label className='collapse-btn bgwhite' htmlFor='collapse-1'>
                <div className='fl fs22 dblock pr20 bold fggreen'>1</div>
                <div className='df fdc'>
                  <span className='fs22 bold fggreen'>Verify Your Company Data</span>
                  <div className='fs14 mt10'>Check your company data and verify.</div>
                </div>
              </label>
              <div className='collapse-panel bgwhite'>
                <div className='ml30 pb20 p24'>
                  <div className='fb pb6 df'>
                    <TextField label='OneID' disabled className='w50pc brnone br4 pr10 h36' defaultValue='' />
                    <TextField label='Organization name' disabled className='w50pc brnone br4 pr10 h36' />
                    <TextField label='contact language' disabled className='w50pc brnone br4 h36' />
                  </div>
                </div>
                <div className='ml30 pb8 mt10 p24'>
                  <div className='bold fs14 pb8'>Organization names</div>
                  <div className='fb pb6 df'>
                    <TextField className='w50pc pr10' disabled label='Registered name' defaultValue='' />
                    <TextField className='w50pc pr10' disabled label='local name' defaultValue='' />
                    <TextField label='international name' disabled className='w50pc brnone br4 pr10 h36' />
                  </div>
                  <div className='fb pb6 df mt10'>
                    <TextField label='transliterated Name' disabled className='w50pc pr10 brnone br4 h36' defaultValue='' />
                    <TextField label='DBA name' disabled className='w50pc brnone br4 pr10 h36' defaultValue='' />
                    <TextField label='VAT registered name' disabled className='w50pc brnone br4  h36' />
                  </div>
                </div>

                <div className='ml30 pb8 mt10 p24'>
                  <div className='bold fs14 pb8'>Organization identifiers</div>
                  <div className='fb pb6 df'>
                    <TextField className='w50pc pr10' disabled label='External Business Partner Identifier' defaultValue='' />
                    <TextField className='w50pc pr10' disabled label='issuer' defaultValue='' />
                  </div>
                  <div className='fb pb6 df mt10'>
                    <TextField label='Type of Business Partner Identifier' disabled className='w50pc pr10 brnone br4 h36' defaultValue='' />
                    <TextField label='Identification number' disabled className='w50pc brnone br4 pr10 h36' defaultValue='' />
                  </div>
                </div>

                <div className='ml30 pb8 mt10 p24'>
                  <div className='bold fs14 pb8'>Business Status</div>
                  <div className='fb pb6 df'>
                    <TextField className='w50pc pr10' disabled label='State of active/operation' defaultValue='' />
                    <TextField className='w50pc pr10' disabled label='Valid from' defaultValue='' />
                    <TextField className='w50pc pr10' disabled label='Valid until' defaultValue='' />
                  </div>
                </div>

                <div className='ml30 pb8 mt50 p24 pb20 brbt df fdc fdrr'>
                <PrimaryButton text='DATA IS CORRECT'  className='ml30'/>
                  <DefaultButton text='REPORT INCORRECT DATA' />
                </div>

              </div>
            </div>
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
                    <TextField label='Email Address' disabled className='w50pc brnone br4 pr10 h36' defaultValue='' />
                    <TextField label='User Role' disabled className='w50pc brnone br4 pr10 h36' />
                    <PrimaryButton className='w10pc mt24 br4 h36' text='ADD' />
                  </div>
                </div>
                <div className='ml30 pb8 mt50 p24 pb20 brbt'>
                  <PrimaryButton text='DATA IS CORRECT' />
                </div>
              </div>
            </div>
            <div className='mb10'>
              <input className='collapse-open' type='checkbox' id='collapse-3' />
              <label className='collapse-btn bgwhite' htmlFor='collapse-3'>
                <div className='fl fs22 dblock pr20 bold'>3</div>
                <div className='df fdc'>
                  <span className='fs22 bold'>Catena-X Company Role</span>
                  <div className='fs14 mt10'>You can choose different roles to participate in Catena-X. Don't worry, you can change it later again.</div>
                </div>
              </label>
              <div className='collapse-panel bgwhite'>
                <div className='p20'>
                  <div className='pb20 p24'>
                    <div className='fb pb6 df'>
                      <TextField label='Email Address' disabled className='w50pc brnone br4 pr10 h36' defaultValue='' />
                    </div>
                    <div className='mr50 mt50 bgfe w100-100 df fdc'>
                      <span className='fs18 bold mt20'>Role description and details</span>
                      <span className='fs16 bold mt20'>Active Participant</span>
                      <span className='fs14 mt20'>A network partner that provides and/or consumes business data (e.g. parts master data) and actively participates in at least one  use case.</span>
                      <span className='fs14'>Examples : OEM, Supplier, KMU(SME) etc. We differentiate in two types: Self-Managed (e.g. own IdP) and Catena-X-Managed (e.g. IdP-Integration)  </span>
                    </div>
                  </div>

                </div>
              </div>
            </div>
            <div className='mb10'>
              <input className='collapse-open' type='checkbox' id='collapse-4' />
              <label className='collapse-btn bgwhite' htmlFor='collapse-4'>
                <div className='fl fs22 dblock pr20 bold'>4</div>
                <div className='df fdc'>
                  <span className='fs22 bold'>Terms &amp; Conditions</span>
                  <div className='fs14 mt10'>Please check and agree to our terms and conditions. Depending on your selected role, there are different versions.
                  </div>
                </div>
              </label>
              <div className='collapse-panel bgwhite'>
                <div className='p20'>
                  <p>
                    Lorem ipsum dolor sit amet, consectetur adipiscing
                    elit, sed do eiusmod tempor incididunt ut labore et
                    dolore magna aliqua. Ut enim ad minim veniam, quis
                    nostrud exercitation ullamco laboris nisi ut aliquip
                    ex ea commodo consequat.
                  </p>
                </div>
              </div>
            </div>
            <div className='mb10'>
              <input className='collapse-open' type='checkbox' id='collapse-5' />
              <label className='collapse-btn bgwhite' htmlFor='collapse-5'>
                <div className='fl fs22 dblock pr20 bold'>5</div>
                <div className='df fdc'>
                  <span className='fs22 bold'>Identity Provider</span>
                  <div className='fs14 mt10'>Use Catena-X own IDP or set up and integrate your own IDP
                  </div>
                </div>
              </label>
              <div className='collapse-panel bgwhite'>
                <div className='p20'>
                  <p>
                    Lorem ipsum dolor sit amet, consectetur adipiscing
                    elit, sed do eiusmod tempor incididunt ut labore et
                    dolore magna aliqua. Ut enim ad minim veniam, quis
                    nostrud exercitation ullamco laboris nisi ut aliquip
                    ex ea commodo consequat.
                  </p>
                </div>
              </div>
            </div>
            <div className='mb10'>
              <input className='collapse-open' type='checkbox' id='collapse-6' />
              <label className='collapse-btn bgwhite' htmlFor='collapse-6'>
                <div className='fl fs22 dblock pr20 bold'>6</div>
                <div className='df fdc'>
                  <span className='fs22 bold'>Certificates</span>
                  <div className='fs14 mt10'>Upload and verify your certificate</div>
                </div>
              </label>
              <div className='collapse-panel bgwhite'>
                <div className='p20'>
                  <p>
                    Lorem ipsum dolor sit amet, consectetur adipiscing
                    elit, sed do eiusmod tempor incididunt ut labore et
                    dolore magna aliqua. Ut enim ad minim veniam, quis
                    nostrud exercitation ullamco laboris nisi ut aliquip
                    ex ea commodo consequat.
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>

      </div>
    );
  }
}

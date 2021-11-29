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
import { getCompanyDetails } from '../helpers/utils';
import { CompanyDetails } from '../data/companyDetails';
import { observable } from 'mobx';
import AlertDialog  from './alertdialog';
import { TextField, PrimaryButton, DefaultButton} from '@fluentui/react';



@observer
export default class Companydata extends React.Component {

  @observable companyDetails: CompanyDetails;
  @observable alertRef;

  async componentDidMount() {
    try {
      this.companyDetails = await getCompanyDetails('CAXLZJVJEBYWYYZZ');
      console.log(this.companyDetails);
    } catch {

    }
  }

  onButtonClick() {
    console.log( this.alertRef);
    this.alertRef?.show(true, 'Disclaimer', 'Are you authorized by your company to confirm this data?');
  }

  yesClick() { 
    this.alertRef?.show(false); 
  }


  public render() {
    const bpn = this.companyDetails?.bpn || '';
    console.log(bpn);
    // const parent = this.companyDetails?.parent || '';
    // const accountGroup = this.companyDetails?.accountGroup || '';
    const name1 = this.companyDetails?.name1 || '';
    // const name2 = this.companyDetails?.name2 || '';
    // const name3 = this.companyDetails?.name3 || '';
    // const name4 = this.companyDetails?.name4 || '';
    // const addressVersion = this.companyDetails?.addressVersion || '';
    // const country = this.companyDetails?.country || '';
    // const city = this.companyDetails?.city || '';
    // const postalCode = this.companyDetails?.postalCode || '';;
    // const street1 = this.companyDetails?.street1 || '';
    // const street2 = this.companyDetails?.street2 || '';
    // const street3 = this.companyDetails?.street3 || '';
    // const houseNumber = this.companyDetails?.houseNumber  || '';
    // const taxNumber1 =this.companyDetails?.taxNumber1 || '';
    // const taxNumber1Type =this.companyDetails?.taxNumber1Type || '';
    // const taxNumber2 =this.companyDetails?.taxNumber2 || '';
    // const taxNumber2Type =this.companyDetails?.taxNumber2 || '';
    // const taxNumber3 =this.companyDetails?.taxNumber3 || '';
    // const taxNumber3Type =this.companyDetails?.taxNumber3Type || '';
    // const taxNumber4 =this.companyDetails?.taxNumber4 || '';
    // const taxNumber4Type =this.companyDetails?.taxNumber4Type || '';
    // const taxNumber5 =this.companyDetails?.taxNumber5 || '';
    // const taxNumber5Type =this.companyDetails?.taxNumber5Type || '';
    // const vatNumber =this.companyDetails?.vatNumber || '';
    // const vatNumberType =this.companyDetails?.vatNumberType || '';
    return (
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
              <TextField label='OneID' disabled className='w50pc brnone br4 pr10 h36' value={bpn} />
              <TextField label='Organization name' disabled value={name1} className='w50pc brnone br4 pr10 h36' />
              <TextField label='contact language' disabled className='w50pc brnone br4 h36' value={name1}/>
            </div>
          </div>
          <div className='ml30 pb8 mt10 p24'>
            <div className='bold fs14 pb8'>Organization names</div>
            <div className='fb pb6 df'>
              <TextField className='w50pc pr10' disabled label='Registered name' value={name1} />
              <TextField className='w50pc pr10' disabled label='local name' value={name1} />
              <TextField label='international name' disabled  value={name1} className='w50pc brnone br4 pr10 h36' />
            </div>
            <div className='fb pb6 df mt10'>
              <TextField label='transliterated Name' disabled className='w50pc pr10 brnone br4 h36' defaultValue={name1} />
              <TextField label='DBA name' disabled className='w50pc brnone br4 pr10 h36' defaultValue={name1} />
              <TextField label='VAT registered name' disabled className='w50pc brnone br4  h36' defaultValue={name1}/>
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
            <PrimaryButton text='DATA IS CORRECT' onClick={()=>this.onButtonClick()} className='ml30' />
            <DefaultButton text='REPORT INCORRECT DATA' />
          </div>

        </div>
        <AlertDialog message='Are you authorized by your company to confirm this data?' ref={(ref)=>this.alertRef = ref} button1Text='YES, I AM AUTHORIZED' button1Action={()=>this.yesClick()} button2Text='NO I AM NOT' button2Action={()=>this.yesClick()} />
      </div>
    )
  }
}
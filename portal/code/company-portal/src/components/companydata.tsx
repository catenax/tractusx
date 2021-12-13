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
import { CompanyDetails, CompanyTechnicalKey } from '../data/companyDetails';
import { observable } from 'mobx';
import AlertDialog from './alertdialog';
import { TextField, PrimaryButton, DefaultButton, SearchBox, Dropdown, IDropdownOption } from '@fluentui/react';
import { toJS } from 'mobx'


let dropdownOptions: IDropdownOption[] = [];
let data;
@observer
export default class Companydata extends React.Component {

  @observable companyDetails: CompanyDetails[];
  @observable companyDetailsById: CompanyDetails[];
  @observable alertRef;
  @observable searchParameter: string;

  async fillFormData(value) {
    console.log(value)
    try {
      this.companyDetails = await getCompanyDetails(value);
      let details = toJS(this.companyDetails);

      if (details.length > 1) {
        // let companies = details.map(x => { return { key: x.cdqId, text: (x.businessPartner.names?.find(y => y.type?.technicalKey === CompanyTechnicalKey.International)?.value) || x.businessPartner.names[0].value } })
        let companies = details.map(x => { return { key: x.cdqId, text: x.businessPartner.names[0].value } });
        Object.assign(dropdownOptions, companies);
      } else {
        try {
          this.companyDetailsById = await getCompanyDetails(this.companyDetails[0].cdqId);
        } catch (e) {
          console.log(e.message)
        }
      }

    } catch (e) {
      console.log(e.message)
    }
  }

  async onChange(ev, item) {
    try {
      this.companyDetailsById = await getCompanyDetails(item.key)
      let details = toJS(this.companyDetails);
      console.log(details);
    } catch (e) {
      console.log(e.message)
    }
  }

  onButtonClick() {
    console.log(this.alertRef);
    this.alertRef?.show(true, 'Disclaimer', 'Are you authorized by your company to confirm this data?');
  }

  yesClick() {
    this.alertRef?.show(false);
  }

  public render() {
    // console.log(data)
    const bpn = toJS(this.companyDetailsById?.[0]?.businessPartner.identifiers.find(x => x.type.technicalKey === 'CX_BPN')?.value) || '';
    const name = toJS(this.companyDetailsById?.[0]?.businessPartner.names.find(x => x.type.technicalKey === 'LOCAL')?.value) || '';
    const registeredName = toJS(this.companyDetailsById?.[0]?.businessPartner.names.find(x => x.type.technicalKey === 'REGISTERED')?.value) || '';
    const localName = toJS(this.companyDetailsById?.[0]?.businessPartner.names.find(x => x.type.technicalKey === 'LOCAL')?.value) || '';
    const internationalName = toJS(this.companyDetailsById?.[0]?.businessPartner.names.find(x => x.type.technicalKey === 'INTERNATIONAL')?.value) || '';
    const transliteralName = toJS(this.companyDetailsById?.[0]?.businessPartner.names.find(x => x.type.technicalKey === 'TRANSLITERATED')?.value) || '';
    const dbaName = toJS(this.companyDetailsById?.[0]?.businessPartner.names.find(x => x.type.technicalKey === 'DOING_BUSINESS_AS')?.value) || '';
    const vatRegisteredName = toJS(this.companyDetailsById?.[0]?.businessPartner.names.find(x => x.type.technicalKey === 'VAT_REGISTERED')?.value) || '';
    // const externalBusiness = toJS(this.companyDetailsById?.[0]?.businessPartner.identifiers.pop());
    // const externalBusinessIdentifier = externalBusiness?.value || '';
    // const externalBusinessIdentifierType = externalBusiness?.type.technicalKey || '';
    // const identificationNumber = externalBusiness?.value || '';
    // const stateActivity = toJS(this.companyDetailsById?.businessPartner.identifiers.st) || ''
    const street1 = toJS(this.companyDetailsById?.[0]?.businessPartner.addresses[0].thoroughfares?.find(x => x.type?.technicalKey === 'STREET')?.value) || '';
    const street2 = toJS(this.companyDetailsById?.[0]?.businessPartner.addresses[0].thoroughfares?.find(x => x.type?.technicalKey === 'STREET')?.value) || '';
    const street3 = toJS(this.companyDetailsById?.[0]?.businessPartner.addresses[0].thoroughfares?.find(x => x.type?.technicalKey === 'SQUARE')?.value) || '';
    const additionalInformation = 'N/A';
    const country = toJS(this.companyDetailsById?.[0]?.businessPartner.addresses[0]?.country.shortName) || '';
    const region = toJS(this.companyDetailsById?.[0]?.businessPartner.addresses[0]?.administrativeAreas?.find(x => x.value === 'REGION')?.shortName) || '';
    const county = toJS(this.companyDetailsById?.[0]?.businessPartner.addresses[0]?.administrativeAreas?.find(x => x.value === 'COUNTY')?.value) || '';
    const postal = toJS(this.companyDetailsById?.[0]?.businessPartner.addresses[0]?.postCodes.find(x => x.type?.technicalKey === 'REGULAR')?.value) || '';
    const city = toJS(this.companyDetailsById?.[0]?.businessPartner.addresses[0]?.postCodes.find(x => x.type?.technicalKey === 'CITY')?.value) || '';
    const district = toJS(this.companyDetailsById?.[0]?.businessPartner.addresses[0]?.postCodes.find(x => x.type?.technicalKey === 'DISTRICT')?.value) || '';
    console.log(name);
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
          <div className='h100vh-212 oa'>
            <div className='ml30 pl20 pr20 pt20 holder'>
              <div className='fb pb6 df'>
                <SearchBox
                  placeholder="Search"
                  onClear={ev => {
                    console.log('Custom onClear Called');
                  }}
                  onSearch={(value) => this.fillFormData(value)}
                />
              </div>
              {(this.companyDetails) ?
                <Dropdown options={dropdownOptions} className='w50pc brnone br4 pr10 h36' onChange={(e, item) => this.onChange(e, item)} /> : ''}
            </div>
            <div className='ml30 pb20 p24'>
              <div className='fb pb6 df'>
                <TextField label='OneID' className='w50pc brnone br4 pr10 h36' value={bpn} />
                <TextField label='Organization name' value={name} className='w50pc brnone br4 pr10 h36' />
                <TextField label='contact language' className='w50pc brnone br4 h36' value='' />
              </div>
            </div>
            <div className='ml30 pb8 mt10 p24'>
              <div className='bold fs14 pb8'>Organization names</div>
              <div className='fb pb6 df'>
                <TextField className='w50pc pr10' label='Registered name' value={registeredName} />
                <TextField className='w50pc pr10' label='local name' value={localName} />
                <TextField label='international name' value={internationalName} className='w50pc brnone br4 pr10 h36' />
              </div>
              <div className='fb pb6 df mt10'>
                <TextField label='transliterated Name' className='w50pc pr10 brnone br4 h36' value={transliteralName} />
                <TextField label='DBA name' className='w50pc brnone br4 pr10 h36' value={dbaName} />
                <TextField label='VAT registered name' className='w50pc brnone br4  h36' value={vatRegisteredName} />
              </div>
            </div>

            <div className='ml30 pb8 mt10 p24'>
              <div className='bold fs14 pb8'>Organization identifiers</div>
              <div className='fb pb6 df'>
                <TextField className='w50pc pr10' label='External Business Partner Identifier' value='' />
                <TextField className='w50pc pr10' label='issuer' value="N/A" />
              </div>
              <div className='fb pb6 df mt10'>
                <TextField label='Type of Business Partner Identifier' className='w50pc pr10 brnone br4 h36' value='' />
                <TextField label='Identification number' className='w50pc brnone br4 pr10 h36' value='' />
              </div>
            </div>

            <div className='ml30 pb8 mt10 p24'>
              <div className='bold fs14 pb8'>Business Status</div>
              <div className='fb pb6 df'>
                <TextField className='w50pc pr10' label='State of active/operation' defaultValue='' />
                <TextField className='w50pc pr10' label='Valid from' defaultValue='' />
                <TextField className='w50pc pr10' label='Valid until' defaultValue='' />
              </div>
            </div>

            <div className='ml30 pb8 mt10 p24'>
              <div className='bold fs14 pb8'>Address information</div>
              <div className='fb pb6 df'>
                <TextField className='w50pc pr10' label='Street' value={street1} />
                <TextField className='w50pc pr10' label='Street 2' value={street2} />
              </div>
              <div className='fb pb6 df'>
                <TextField className='w50pc pr10' label='House number' value={street3} />
                <TextField label='Add. information' className='w50pc pr10 brnone br4 h36' value={additionalInformation} />
              </div>
              <div className='fb pb6 df mt10'>
                <TextField className='w50pc pr10' label='Country' value={country} />
                <TextField label='District' className='w50pc pr10 brnone br4 h36' value={district} />
              </div>
              <div className='fb pb6 df mt10'>
                <TextField className='w50pc pr10' label='Postal Code' value={postal} />
                <TextField label='City' className='w50pc pr10 brnone br4 h36' value={city} />
              </div>
            </div>

            <div className='ml30 pb8 mt10 p24'>
              <div className='bold fs14 pb8'>Contact</div>
              <div className='fb pb6 df'>
                <TextField className='w50pc pr10' label='Email address' defaultValue='' />
                <TextField className='w50pc pr10' label='Website' defaultValue='' />
                <TextField className='w50pc pr10' label='Country prefix' defaultValue='' />
              </div>
              <div className='fb pb6 df'>
                <TextField className='w50pc pr10' label='Phone number' defaultValue='' />
                <TextField className='w50pc pr10' label='Mobile Phone' defaultValue='' />
                <TextField className='w50pc pr10' label='Fax' defaultValue='' />
              </div>
            </div>

            <div className='ml30 pb8 mt10 p24'>
              <div className='bold fs14 pb8'>Account</div>
              <div className='fb pb6 df'>
                <TextField className='w50pc pr10' label='IBAN' defaultValue='' />
                <TextField className='w50pc pr10' label='Currency' defaultValue='' />
              </div>
              <div className='fb pb6 df'>
                <TextField className='w50pc pr10' label='BIC' defaultValue='' />
                <TextField className='w50pc pr10' label='Country of bank' defaultValue='' />
              </div>
            </div>

            <div className='ml30 pb8 mt50 p24 pb20 brbt df fdc fdrr'>
              <PrimaryButton text='DATA IS CORRECT' onClick={() => this.onButtonClick()} className='ml30' />
              <DefaultButton text='DELETE' />
            </div>
          </div>

        </div>
        <AlertDialog message='Are you authorized by your company to confirm this data?' ref={(ref) => this.alertRef = ref} button1Text='YES, I AM AUTHORIZED' button1Action={() => this.yesClick()} button2Text='NO I AM NOT' button2Action={() => this.yesClick()} />
      </div>
    )
  }
}
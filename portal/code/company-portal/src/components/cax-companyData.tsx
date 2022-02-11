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

import { Row } from 'react-bootstrap';
import { getCompanyDetails } from '../helpers/utils';
import { AiOutlineQuestionCircle, AiOutlineCalendar } from 'react-icons/ai'
import DatePicker from "react-datepicker";
import SearchInput from 'react-search-input';
import { FetchBusinessPartnerDto } from "../data/companyDetailsById"
import { useTranslation } from 'react-i18next';


export const CompanyDataCax = () => {

    const { t } = useTranslation();

    // async fillFormData(value) {
    //     console.log(value)      
    //         try {
    //           this.companyDetailsById = await getCompanyDetails(value);
    //         } catch (e) {
    //           console.log(e.message)
    //         }
    //   }

    //   async onSeachChange(ev, item) {
    //     try {
    //       this.companyDetailsById = await getCompanyDetails(item.key)
    //       let details = toJS(this.companyDetails);
    //       console.log(details);
    //     } catch (e) {
    //       console.log(e.message)
    //     }
    //   }

    //   async onChange(ev) {
    //     try {
    //       this.companyDetailsById = await getCompanyDetails(ev.target.value)
    //       let details = toJS(this.companyDetailsById);
    //       console.log(details);
    //     } catch (e) {
    //       console.log(e.message)
    //     }
    //   }


        //  const bpn = toJS(this.companyDetailsById?.[0]?.bpn) ||"";
        //  const legalEntity = toJS(this.companyDetailsById?.[0]?.names.find(x => x.type === 'INTERNATIONAL')?.value) || "";
        //  const registeredName = toJS(this.companyDetailsById?.[0]?.names.find(x => x.type === 'REGISTERED')?.value) || "";
        //  const streetHouseNumber =  toJS(this.companyDetailsById?.[0]?.addresses?.[0]?.thoroughfares.find(x => x.type === 'INDUSTRIAL_ZONE')?.value) || "";
        //  const postalCode =  toJS(this.companyDetailsById?.[0]?.addresses?.[0]?.postCodes.find(x => x.type === 'REGULAR')?.value) || "";
        //  const city = toJS(this.companyDetailsById?.[0]?.addresses?.[0]?.localities.find(x => x.type === 'BLOCK')?.value) || "";
        //  const country = toJS(this.companyDetailsById?.[0]?.addresses?.[0]?.countryCode) || "";

         


        return (
            <div className='mx-auto col-9 container-registration'>
                <div className='head-section'>
                    <div className='mx-auto step-highlight d-flex align-items-center justify-content-center'>1</div>
                    <h4 className='mx-auto d-flex align-items-center justify-content-center'>{t('registrationStepOne.verifyCompayDataHeading')}</h4>
                    <div className='mx-auto text-center col-9'>{t('registrationStepOne.verifyCompayDataSubHeading')}</div>
                </div>
                <div className='companydata-form'>
                    <Row className='mx-auto col-9'>
                        <div className='form-search'>
                            <label> {t('registrationStepOne.seachDatabase')}</label>
                            <SearchInput className="search-input" value="" />
                        </div>
                    </Row>
                    <Row className='col-9 mx-auto'>
                        <div className="section-divider">
                            <span className='text-center'>{t('registrationStepOne.enterManualText')}</span>
                        </div>
                    </Row>
                    <Row className='mx-auto col-9'>
                        <div className='form-data'>
                            <label> {t('registrationStepOne.bpn')} <AiOutlineQuestionCircle color='#939393' data-tip="hello world" /></label>
                            <input type="text" value=""/>
                            <div className='company-hint'>{t('registrationStepOne.helperText')}</div>
                        </div>
                    </Row>
                    <Row className='mx-auto col-9'>
                        <div className='form-data'>
                            <label> {t('registrationStepOne.legalEntity')} <AiOutlineQuestionCircle color='#939393' data-tip="hello world" /> </label>
                            <input type="text" value=""/>
                            <div className='company-hint'>{t('registrationStepOne.helperText')}</div>
                        </div>
                    </Row>
                    <Row className='mx-auto col-9'>
                        <div className='form-data'>
                            <label> {t('registrationStepOne.registeredName')} <AiOutlineQuestionCircle color='#939393' data-tip="hello world" /></label>
                            <input type="text" value=""/>
                            <div className='company-hint'>{t('registrationStepOne.helperText')}</div>
                        </div>
                    </Row>

                    <Row className='mx-auto col-9'>
                        <span className='form-heading'>{t('registrationStepOne.organizationAdd')}</span>
                    </Row>

                    <Row className='mx-auto col-9'>
                        <div className='form-data'>
                            <label> {t('registrationStepOne.streetHouseNumber')} </label>
                            <input type="text" value=""/>
                        </div>
                    </Row>

                    <Row className='mx-auto col-9'>
                        <div className='col-4 form-data'>
                            <label> {t('registrationStepOne.postalCode')} </label>
                            <input type="text" value=""/>
                        </div>

                        <div className='col-8 form-data'>
                            <label>{t('registrationStepOne.city')}</label>
                            <input type="text" value=""/>
                        </div>
                    </Row>

                    <Row className='mx-auto col-9'>
                        <div className='form-data'>
                            <label>{t('registrationStepOne.country')}</label>
                            {/* <select
                                defaultValue='Choose your country'
                            >
                                <option value="">Choose your country</option>
                                <option value="test1">test 1</option>
                                <option value="test2">Test 2</option>
                                <option value="test3">Test 3</option>
                            </select> */}
                            <input type="text" value=""/>
                        </div>
                    </Row>

                    <Row className='mx-auto col-9'>
                        <span className='form-heading'>{t('registrationStepOne.businessStatus')}</span>
                    </Row>

                    <Row className='mx-auto col-9'>
                        <div className='form-data'>
                            <label>{t('registrationStepOne.stateOfActivity')} </label>
                            <select
                                defaultValue='active'
                            >
                                <option value="active">Active</option>
                                <option value="test2">Test 2</option>
                                <option value="test3">Test 3</option>
                            </select>
                        </div>
                    </Row>

                    <Row className='mx-auto col-9'>
                        <div className='form-data calender'>
                            <label> {t('registrationStepOne.validFrom')}</label>
                            <DatePicker className='date-picker' />
                            <AiOutlineCalendar className='calender-icon' />
                        </div>
                    </Row>

                    <Row className='mx-auto col-9'>
                        <div className='form-data calender'>
                            <label>  {t('registrationStepOne.validUntil')}</label>
                            <DatePicker className='date-picker' />
                            <AiOutlineCalendar className='calender-icon' />
                        </div>
                    </Row>

                </div>
            </div>
            
        )
}
export default (CompanyDataCax);
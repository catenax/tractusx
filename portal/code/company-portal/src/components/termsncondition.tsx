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
import { Checkbox, Link } from '@fluentui/react';
import AlertDialog from './alertdialog';
import { observable } from 'mobx';

let alertRef;
@observer
export default class Termsncondition extends React.Component {
@observable private data

 

    _renderLabelWithLinktnc() {
        return (
          <span>
            Yes, I agree to this{' '}
            <Link onClick={() => alertRef?.show(true, 'Participent Terms and Condition', 'Last updated: September 30th, 2021 Agreement to Terms These Terms and Conditions constitute a legally binding agreement made between you, whether personally or on behalf of an entity ("you") and [business entity name] ("we," "us" or "our"), concerning your access to and use of the [website name.com] website as well as any other media form, media channel, mobile website or mobile application related, linked, or otherwise connected thereto (collectively, the “Site"). You agree that by accessing the Site, you have read, understood, and agree to be bound by all of these Terms and Conditions. If you do not agree with all of these Terms and Conditions, then you are expressly prohibited from using the Site and you must discontinue use immediately. <br /><br /> Supplemental terms and conditions or documents that may be posted on the Site from time to time are hereby expressly incorporated herein by reference. We reserve the right, in our sole discretion, to make changes or modifications to these Terms and Conditions at any time and for any reason.<br /><br /> We will alert you about any changes by updating the "Last updated" date of these Terms and Conditions, and you waive any right to receive specific notice of each such change.<br /><br /> It is your responsibility to periodically review these Terms and Conditions to stay informed of updates. You will be subject to,')} >
            terms and conditions
            </Link>
          </span>
        );
      }
      
    
      _renderLabelWithLinkdsr() {
        return (
          <span>
            Yes, I agree to this{' '}
            <Link onClick={() => alertRef?.show(true, 'Participent Terms and Condition', 'Last updated: September 30th, 2021 Agreement to Terms These Terms and Conditions constitute a legally binding agreement made between you, whether personally or on behalf of an entity ("you") and [business entity name] ("we," "us" or "our"), concerning your access to and use of the [website name.com] website as well as any other media form, media channel, mobile website or mobile application related, linked, or otherwise connected thereto (collectively, the “Site"). You agree that by accessing the Site, you have read, understood, and agree to be bound by all of these Terms and Conditions. If you do not agree with all of these Terms and Conditions, then you are expressly prohibited from using the Site and you must discontinue use immediately. <br /><br /> Supplemental terms and conditions or documents that may be posted on the Site from time to time are hereby expressly incorporated herein by reference. We reserve the right, in our sole discretion, to make changes or modifications to these Terms and Conditions at any time and for any reason.<br /><br /> We will alert you about any changes by updating the "Last updated" date of these Terms and Conditions, and you waive any right to receive specific notice of each such change.<br /><br /> It is your responsibility to periodically review these Terms and Conditions to stay informed of updates. You will be subject to,')} >
            data security regulations
            </Link>
          </span>
        );
      }
    
      _renderLabelWithLinkaptnc() {
        return (
          <span>
            Yes, I agree to this{' '}
            <Link onClick={() => alertRef?.show(true, 'Participent Terms and Condition', 'Last updated: September 30th, 2021 Agreement to Terms These Terms and Conditions constitute a legally binding agreement made between you, whether personally or on behalf of an entity ("you") and [business entity name] ("we," "us" or "our"), concerning your access to and use of the [website name.com] website as well as any other media form, media channel, mobile website or mobile application related, linked, or otherwise connected thereto (collectively, the “Site"). You agree that by accessing the Site, you have read, understood, and agree to be bound by all of these Terms and Conditions. If you do not agree with all of these Terms and Conditions, then you are expressly prohibited from using the Site and you must discontinue use immediately. <br /><br /> Supplemental terms and conditions or documents that may be posted on the Site from time to time are hereby expressly incorporated herein by reference. We reserve the right, in our sole discretion, to make changes or modifications to these Terms and Conditions at any time and for any reason.<br /><br /> We will alert you about any changes by updating the "Last updated" date of these Terms and Conditions, and you waive any right to receive specific notice of each such change.<br /><br /> It is your responsibility to periodically review these Terms and Conditions to stay informed of updates. You will be subject to,')} >
            terms and conditions
            </Link>
          </span>
        );
      }
    
      _renderLabelWithLinkapdsr() {
        return (
          <span>
            Yes, I agree to this{' '}
            <Link onClick={() => alertRef?.show(true, 'Participent Terms and Condition', 'Last updated: September 30th, 2021 Agreement to Terms These Terms and Conditions constitute a legally binding agreement made between you, whether personally or on behalf of an entity ("you") and [business entity name] ("we," "us" or "our"), concerning your access to and use of the [website name.com] website as well as any other media form, media channel, mobile website or mobile application related, linked, or otherwise connected thereto (collectively, the “Site"). You agree that by accessing the Site, you have read, understood, and agree to be bound by all of these Terms and Conditions. If you do not agree with all of these Terms and Conditions, then you are expressly prohibited from using the Site and you must discontinue use immediately. <br /><br /> Supplemental terms and conditions or documents that may be posted on the Site from time to time are hereby expressly incorporated herein by reference. We reserve the right, in our sole discretion, to make changes or modifications to these Terms and Conditions at any time and for any reason.<br /><br /> We will alert you about any changes by updating the "Last updated" date of these Terms and Conditions, and you waive any right to receive specific notice of each such change.<br /><br /> It is your responsibility to periodically review these Terms and Conditions to stay informed of updates. You will be subject to,')} >
            data security regulations
            </Link>
          </span>
        );
      }
    
      _renderLabelWithLinkpnsc() {
        return (
          <span>
            Yes, I agree to {' '}
            <Link onClick={() => alertRef?.show(true, 'Participent Terms and Condition', 'Last updated: September 30th, 2021 Agreement to Terms These Terms and Conditions constitute a legally binding agreement made between you, whether personally or on behalf of an entity ("you") and [business entity name] ("we," "us" or "our"), concerning your access to and use of the [website name.com] website as well as any other media form, media channel, mobile website or mobile application related, linked, or otherwise connected thereto (collectively, the “Site"). You agree that by accessing the Site, you have read, understood, and agree to be bound by all of these Terms and Conditions. If you do not agree with all of these Terms and Conditions, then you are expressly prohibited from using the Site and you must discontinue use immediately. <br /><br /> Supplemental terms and conditions or documents that may be posted on the Site from time to time are hereby expressly incorporated herein by reference. We reserve the right, in our sole discretion, to make changes or modifications to these Terms and Conditions at any time and for any reason.<br /><br /> We will alert you about any changes by updating the "Last updated" date of these Terms and Conditions, and you waive any right to receive specific notice of each such change.<br /><br /> It is your responsibility to periodically review these Terms and Conditions to stay informed of updates. You will be subject to,')} >
            provider and seller conditions
            </Link>
          </span>
        );
      }

      yesClick() { 
        alertRef?.show(false); 
      }
    

  public render() {
    this.data = 'Last updated: September 30th, 2021 Agreement to Terms These Terms and Conditions constitute a legally binding agreement made between you, whether personally or on behalf of an entity ("you") and [business entity name] ("we," "us" or "our"), concerning your access to and use of the [website name.com] website as well as any other media form, media channel, mobile website or mobile application related, linked, or otherwise connected thereto (collectively, the “Site"). You agree that by accessing the Site, you have read, understood, and agree to be bound by all of these Terms and Conditions. If you do not agree with all of these Terms and Conditions, then you are expressly prohibited from using the Site and you must discontinue use immediately. <br /><br /> Supplemental terms and conditions or documents that may be posted on the Site from time to time are hereby expressly incorporated herein by reference. We reserve the right, in our sole discretion, to make changes or modifications to these Terms and Conditions at any time and for any reason.<br /><br /> We will alert you about any changes by updating the "Last updated" date of these Terms and Conditions, and you waive any right to receive specific notice of each such change.<br /><br /> It is your responsibility to periodically review these Terms and Conditions to stay informed of updates. You will be subject to,'
    return (
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
            <div className='pb20 p24'>
              <span className='fs18 bold mt20'>Active Participant Tearms & Condition</span>
              <Checkbox className='mt20' label="Yes, I agree to this"  onRenderLabel={this._renderLabelWithLinktnc}/>
              <Checkbox className='mt20' label="Yes, I agree to this" onRenderLabel={this._renderLabelWithLinkdsr} />
            </div>
            <div className='pb20 p24'>
            <span className='fs18 bold mt30'>App Provider Terms & Conditions</span>
            <Checkbox className='mt20' label="Yes, I agree to this"  onRenderLabel={this._renderLabelWithLinkaptnc}/>
              <Checkbox className='mt20' label="Yes, I agree to this" onRenderLabel={this._renderLabelWithLinkapdsr} />
            <Checkbox className='mt20' label="Yes, I agree to" onRenderLabel={this._renderLabelWithLinkpnsc} />
        </div>
      </div>
    </div>
    <AlertDialog message='Last updated: September 30th, 2021 Agreement to Terms These Terms and Conditions constitute a legally binding agreement made between you, whether personally or on behalf of an entity ("you") and [business entity name] ("we," "us" or "our"), concerning your access to and use of the [website name.com] website as well as any other media form, media channel, mobile website or mobile application related, linked, or otherwise connected thereto (collectively, the “Site"). You agree that by accessing the Site, you have read, understood, and agree to be bound by all of these Terms and Conditions. If you do not agree with all of these Terms and Conditions, then you are expressly prohibited from using the Site and you must discontinue use immediately. <br /><br /> Supplemental terms and conditions or documents that may be posted on the Site from time to time are hereby expressly incorporated herein by reference. We reserve the right, in our sole discretion, to make changes or modifications to these Terms and Conditions at any time and for any reason.<br /><br /> We will alert you about any changes by updating the "Last updated" date of these Terms and Conditions, and you waive any right to receive specific notice of each such change.<br /><br /> It is your responsibility to periodically review these Terms and Conditions to stay informed of updates. You will be subject to,' ref={(ref)=>alertRef = ref} button1Text='YES, I AGREE' button1Action={()=>this.yesClick()} button2Text='NO, I DONT AGREE' button2Action={()=>this.yesClick()} />
  </div>
  
    )} 
}
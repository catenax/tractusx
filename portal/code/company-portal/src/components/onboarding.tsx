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
import { withTranslation, WithTranslation } from 'react-i18next';
import { observer } from 'mobx-react';
import Companydata from './companydata';
import Responsibilities from './responsibilities';
import Companyrole from './companyrole';
import Termsncondition from './termsncondition';
import Identity from './identity';
import OnboardingSubmit from './onboardingSubmit';


@observer
class Onboarding extends React.Component<WithTranslation> {
  componentDidMount() {
    const onboarding = {
      companydata: false,
      userRole: false,
      companyrole: false,
      companyterms: false,
      identity: false
    }
    window.localStorage.setItem('onboarding', JSON.stringify(onboarding));
  }

  public render() {
    // const { t } = this.props;
    return (
      <div className='w100pc h100pc df fdc'>
        <div className='ml50 mr50 mt50 bgfe w100-100 df fdc'>
          <span className='fs20 bold mt20'>{this.props.t('greetMessage')}</span>
          <span className='fs14 mt30'>{this.props.t('subHeading')}</span>
        </div>
        <div className='ml50 mr50 mt30 bgfe w100-100 df fdc'>
          <div className='collapse-list'>
            <Companydata />
            <Responsibilities />
            <Companyrole />
            <Termsncondition />
            <Identity />
            <OnboardingSubmit />
          </div>
        </div >
      </div >
    );
  }
}

export default withTranslation()(Onboarding);

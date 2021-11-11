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
import RatingsApp from './ratingsApp';
import { RouteComponentProps, withRouter } from 'react-router';

interface IProp extends RouteComponentProps {
  wide?: boolean
  buttonText?: string
  hideUsage?: boolean
  hideRating?: boolean
}

@observer
class AppCardData extends React.Component<IProp> {

  cardClick(ev?: React.MouseEvent, detail = false): void {
    
  }

  public render() {
    return (
      <>
        <div className='h270 m5 br4 bsAppStore bgwhite minw200 maxw200 cpointer hov'>
        <div className='df fdc h120'>
            <div className='flex1' />
            <RatingsApp className='ml15 mt50' app='4.8' />
          </div>
          <div className='ml15 fs14 fggrey mb5'>CDQ inside</div>
          <div className='ml15 bold fs14 minh40'><a className='tdn fg191' href='https://apps.cdq.com/signin/catenax' target="_blank">BPDM Service</a></div>
          <div className='h50 mt20 tal ml15'>
            <div className='fglgreen bold fs14'>SEE DETAILS</div>
            <div className='fsxs fgb5'>Free for use</div>
          </div>
        </div>

        <div className='h270 m5 br4 bsAppStore bgwhite minw200 maxw200 cpointer hov'>
          <div className='df fdc h120'>
            <div className='flex1' />
            <RatingsApp className='ml15 mt50' app='4.9' />
          </div>
          <div className='ml15 fs14 fggrey mb5'>K.a.p.u.t.t GmbH</div>
          <div className='ml15 bold fs14 minh40'>Circular Economy-K.a.p.u.t.t</div>
          <div className='h50 mt20 tal ml15'>
            <div className='fglgreen bold fs14'>SEE DETAILS</div>
            <div className='fsxs fgb5'>Per month</div>
          </div>
        </div>

        <div className='h270 m5 br4 bsAppStore bgwhite minw200 maxw200 cpointer hov'>
        <div className='df fdc h120'>
            <div className='flex1' />
            <RatingsApp className='ml15 mt50' app='4.5' />
          </div>
          <div className='ml15 fs14 fggrey mb5'>SAP</div>
          <div className='ml15 bold fs14 minh40'>Material Traceability</div>
          <div className='h50 mt20 tal ml15'>
            <div className='fglgreen bold fs14'>SEE DETAILS</div>
            <div className='fsxs fgb5'>per year</div>
          </div>
        </div>

        <div className='h270 m5 br4 bsAppStore bgwhite minw200 maxw200 cpointer hov'>
        <div className='df fdc h120'>
            <div className='flex1' />
           <RatingsApp className='ml15 mt50' app='4.7' />
          </div>
          <div className='ml15 fs14 fggrey mb5'>Catena-X</div>
          <div className='ml15 bold fs14 minh40'>Part Chain</div>
          <div className='h50 mt20 tal ml15'>
            <div className='fglgreen bold fs14'>SEE DETAILS</div>
           <div className='fsxs fgb5'>demo</div>
          </div>
        </div>
        <div className='h270 m5 br4 bsAppStore bgwhite minw200 maxw200 cpointer hov'>
        <div className='df fdc h120'>
            <div className='flex1' />
            <RatingsApp className='ml15 mt50' app='4.7' />
          </div>
          <div className='ml15 fs14 fggrey mb5'>SAP</div>
          <div className='ml15 bold fs14 minh40'>Circular Economy - SAP</div>
          <div className='h50 mt20 tal ml15'>
            <div className='fglgreen bold fs14'>SEE DETAILS</div>
           <div className='fsxs fgb5'>Free for use</div>
          </div>
        </div>
      </>
    );
  }
}

export default withRouter(AppCardData);

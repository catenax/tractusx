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
import DragDrop from './dragdrop';

@observer
export default class Certificate extends React.Component {

  public render() {
    return (
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
            <DragDrop />
          </div>
        </div>
      </div>
 
    )} 
}
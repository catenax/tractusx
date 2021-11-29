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
import { PrimaryButton } from '@fluentui/react';
import { AppState } from '../stores/appstate';
import AppCard from './appcard';
import { RouteComponentProps, withRouter } from 'react-router';

@observer
class AppStore extends React.Component<RouteComponentProps> {

  public render() {

    return (
      <div className='w100pc bgf5'>
        <div className='ml50 mr50 mt50 bgimage w100-100 df fdc br10 flex1 mb30'>
          <span className='fs20 bold ml50 pb5 mt20pc tac'>Alliance for secure and standardized data exchange</span>
          <span className='tac fs14 ml50'>We share the vision of continous data exchange for all participants <br />
            in the automotive value chain. We can only achieve this goal together.<br />
            That's what Catena-X is for.
          </span>
          <span className='tac pt18 ml50'>
            <PrimaryButton text='TRY OUT NOW' />
          </span>
          <span className='tac pt5 fgb5 fs10 ml50'>Test our FREE applications</span>
        </div>

        {AppState.state.categoriesnew.map((c, index) => (
          <div key={index} className='ml50 mr50 mb30 w100pc df fdc bgf5'>
            <span className='bold fs14 ml10'>{c.text}</span>
            <div className='w100-100'>
              <div className='ovx h290 df'>
                {c.apps.map((a, index) => <AppCard key={index} app={a} />)}
              </div>
            </div>
          </div>
        ))}
      </div>
    );
  }
}

export default withRouter(AppStore);

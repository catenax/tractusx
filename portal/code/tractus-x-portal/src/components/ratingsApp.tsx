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
import { Icon } from '@fluentui/react';

@observer
export default class RatingsApp extends React.Component<{ app: string, className?: string }> {

  public render() {
    return (
      <div className='ml15 mt50'>
        <Icon className='fgblack fs20' iconName='FavoriteStarFill' />
        <span className='fs14 fgblack bold ml5 mt2'>{this.props.app}</span>
        <span className='fs14 fggrey ml5 mt2'>/ 5</span>
      </div>
    );
  }
}

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
import { Application } from '../data/application';
import Ratings from './ratings';
import { RouteComponentProps, withRouter } from 'react-router';
import { IconButton } from '@fluentui/react';

interface IProp extends RouteComponentProps {
  app: Application
  wide?: boolean
  buttonText?: string
  hideUsage?: boolean
  hideRating?: boolean
}

@observer
class AppCard extends React.Component<IProp> {

  cardClick(ev?: React.MouseEvent, detail = false): void {
    if (ev) {
      ev.stopPropagation();
      ev.preventDefault();
    }

    if (this.props.buttonText === 'UPLOAD') {
      if (this.props.app.title === 'PartChain') {
        this.props.history.replace('/dataupload2');
      }
    } else if (!detail && (this.props.buttonText === 'OPEN' || this.props.app.purchase === 'OPEN')) {
      if (this.props.app.url) {
        if (this.props.app.url.substr(0, 1) !== '/') {
          window.open(this.props.app.url, '_blank');
        } else {
          this.props.history.push(this.props.app.url);
        }
      }
    } else if (this.props.history.length > 1 && this.props.history.location.pathname.indexOf('/appdetail/') >= 0) {
      this.props.history.replace(`/home/appdetail/${this.props.app.id}`);
    } else {
      this.props.history.push(`/home/appdetail/${this.props.app.id}`);
    }
  }

  onClickTitle() {
    if (this.props.app.url) {
      if (this.props.app.url.substr(0, 1) !== '/') {
        window.open(this.props.app.url, '_blank');
      } else {
        this.props.history.push(this.props.app.url);
      }
      return false;
    }
  }

  public render() {
    const a = this.props.app;
    let icon: string = 'Catena-X.png';
    if (a.companyName === 'BMW' || a.companyName === 'SAP') {
      icon = a.companyName + '.png';
    }

    console.log(this.props);
    let appCardClass = '';
    (this.props.app.background !== null && this.props.hideRating === false) ? appCardClass = 'h150 m5 br4 bsAppStore bgwhite minw200 maxw200 cpointer hov'
      : appCardClass = 'h150 m5 br4 bsAppStore bgwhite minw200 maxw200 cpointer hov';
    const text = this.props.buttonText || a.details || a.purchase;
    if (!this.props.wide) {
      return (

        <div className={appCardClass} onClick={() => this.cardClick()}>
          <div className='w100pc df'>
            <div className='mt10 ml15'>
              <img height='20' src={`../${icon}`} />
            </div>
            <div className='flex1'/>
              <IconButton className='mt5' iconProps={{ iconName: 'infoSolid', className: 'fs20' }} title='Show app description'
                onClick={(ev: any) => this.cardClick(ev, true)} />
          </div>
          {/* <div className='ml15 fs14 fggrey mb5'>{a.companyName}</div> */}
          <div className='ml15 mt5 bold fs14 minh40' onClick={() => this.onClickTitle()}>{a.title}</div>
          {!this.props.hideRating && <Ratings className='ml15 mt30' app={a} />}
          {/* <div className='h50 mt20 tal ml15'>
            <div className='fglgreen bold fs14'>{text}</div>
            {!this.props.hideUsage && <div className='fsxs fgb5'>{a.usage}</div>}
          </div> */}
        </div>
      );
    } else {
      return (
        <div className='w340 minh160 maxh160 m5 br4 bsAppStore bgwhite cpointer hov' onClick={(ev) => this.cardClick(ev, true)}>
          <Ratings className='mt20 ml15 mb5' app={a} />
          <div className='ml15 fs14 fggrey mb5 mt20'>{a.companyName}</div>
          <div className='df'>
            <div className='ml15 bold fs14 minh40'>{a.title}</div>
            <div className='flex1' />
            <div className='h50 mt20 mr20'>
              <div className='fglgreen bold fs14'>{text}</div>
              {!this.props.hideUsage && <div className='fsxs fgb5'>{a.usage}</div>}
            </div>
          </div>
        </div>
      );
    }
  }
}

export default withRouter(AppCard);

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

// eslint-disable-next-line
import * as React from 'react';
import { DefaultButton, Modal, PrimaryButton } from '@fluentui/react';
import { observable } from 'mobx';
import { observer } from 'mobx-react';

interface IProp {
  message: string;
  button1Text: string;
  button1Action: () => void;
  button2Text?: string;
  button2Action?: () => void;
  isGlobal?: boolean;
  isSave?: boolean;
}

@observer
export default class AlertDialog extends React.Component<IProp> {
  @observable private isVisible = false;
  public static global: AlertDialog;
  public static save: AlertDialog;
  public navUrl: string;
  private message: string;
  private title: string;

  constructor(props: any) {
    super(props);
    if (this.props.isGlobal) {
      AlertDialog.global = this;
    }
    if (this.props.isSave) {
      AlertDialog.save = this;
    }
    this.message = this.props.message;
  }

  public setMessage(message: string) {
    this.message = message;
  }

  public show(visible: boolean, title?: string, message?: string) {
    if (message && message.indexOf('No server on localhost') >= 0) {
      return;
    }
    this.title = title;
    this.message = message || this.message;
    this.isVisible = visible;
  }

  public render() {
    return (
      <Modal isOpen={this.isVisible} containerClassName='w440' scrollableContentClassName='df h100pc'>
        <div>
          {this.title && <h1 className='fs20 lh32 fw600 p10 gray-border-bottom'>{this.title}</h1>}
          <p className='fs16 lh32 fw600 p10 gray-border-bottom'>{this.message}</p>
          <div className='df jcfe p10'>
            <PrimaryButton text={this.props.button1Text} onClick={() => this.props.button1Action()} />
            {this.props.button2Text && <DefaultButton className='ml10' text={this.props.button2Text} onClick={() => this.props.button2Action()} />}
          </div>
        </div>
      </Modal>
    );
  }
}

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
import { Dialog, DialogFooter, Pivot, PivotItem, PrimaryButton, SearchBox } from '@fluentui/react';
import jsonData from '../data/gpm_testdata.json';
import YellowPage from '../data/yellowpage';
import { compare } from '../helpers/utils';
import { observable } from 'mobx';
import OrgBankData from './orgbankdata';
import OrgContactData from './orgcontactdata';
import OrgGeneralInfo from './orggeneralinfo';

@observer
export default class YellowPages extends React.Component {
  @observable private yellowPagesData: YellowPage[] = [];
  private readonly data: YellowPage[];
  private lastLetter = '';
  @observable private selectedItem: YellowPage;
  @observable private contentOverflowing = false;
  private refMainDiv: HTMLDivElement;

  constructor(props: any) {
    super(props);
    this.data = JSON.parse(JSON.stringify(jsonData));
    this.yellowPagesData = this.data.sort((a, b) => compare(a.businessPartnerName[0].name.toLowerCase(), b.businessPartnerName[0].name.toLowerCase()));
  }

  public componentDidMount() {
    this.componentDidUpdate();
  }

  public componentDidUpdate() {
    if (this.refMainDiv) {
      this.contentOverflowing = this.refMainDiv.scrollHeight > this.refMainDiv.clientHeight;
    }
  }

  private searchVal(newvalue: string): void {
    this.yellowPagesData = this.data.filter(a => a.businessPartnerName[0].name.toLowerCase().includes(newvalue.toLowerCase()));
  }

  private rolodex(name: string) {
    const letter = name.substr(0, 1).toUpperCase();
    if (this.lastLetter === letter) {
      return null;
    } else {
      this.lastLetter = letter;
      return <span className='bold fs24 mb10 ml10 mt3'>{letter}</span>
    }
  }

  public render() {
    this.lastLetter = '';
    return (
      <div className='w100-10 h100pc df fdc'>
        <div className='w100pc bgf5 h100pc df fdc ml50'>
          <div className='df bgf5 mb15 mt50 w100-60 aic'>
            <span className='fs14 fggrey flex2'><SearchBox className='bcwhite' placeholder='Search' onChange={(ev, newvalue) => this.searchVal(newvalue)} /></span>
            <div className='flex3' />
            {/* <span className='fs14 fggrey flex1 mr35'>country:  <span className='bold'>all</span></span> */}
            {/* <span className='fs14 fggrey mr5 flex1'>commodity:  <span className='bold'>none</span></span> */}
          </div>
          <div className='df mb5 w100-80 p5'>
            <span className='fs14 fgblack bold flex3'>Name</span>
            <span className='fs14 fgblack ml30 bold flex2'>OneID</span>
            <span className='fs14 fgblack ml10 bold mr5 flex1'>Country</span>
            {/* <span className='fs14 fggrey flex1'>Commodity</span> */}
            {this.contentOverflowing && <div className='minw17'>&nbsp;</div>}
          </div>
          <div className='df fdc oa w100-40 mb40' ref={(ref) => this.refMainDiv = ref} >
            {this.yellowPagesData.map((c, index) => (
              <div key={index} className='df fdc'>
                {this.rolodex(c.businessPartnerName[0].name)}
                <div className='df bgwhite h36 mb5 w100-40 p5 aic hov cpointer' onClick={() => this.selectedItem = c}>
                  <span className='fs14 ml5 mr5 flex3'>{c.businessPartnerName[0].name}</span>
                  <span className='fs14 flex2 minw100'>{c.oneID}</span>
                  <span className='fs14 flex1'>{c.addressData[0].country.countryNameEN}</span>
                  {/* <span className='fs14 mt7 pl5 flex1'></span> */}
                </div>
              </div>
            ))}
          </div>
        </div>
        {this.selectedItem && <Dialog hidden={false} modalProps={{ containerClassName: 'minw100-100 br7', topOffsetFixed: true }}>
          <span className='bold fs24 ml30 mb30'>{this.selectedItem.businessPartnerName[0].name}</span>
          <Pivot className='px30 bgfa h100pc' aria-label='Header'>
            <PivotItem className='mr20' headerText='General Information' >
              <OrgGeneralInfo orgDetails={this.selectedItem as any}> </OrgGeneralInfo>
            </PivotItem>
            <PivotItem className='mr20' headerText='Contact Data' >
              <OrgContactData orgDetails={this.selectedItem as any} />
            </PivotItem>
            <PivotItem className='mr20' headerText='Bank Data' >
              <OrgBankData orgDetails={this.selectedItem as any} />
            </PivotItem>
          </Pivot>
          <DialogFooter>
            <PrimaryButton className='br4' text='CLOSE' onClick={() => this.selectedItem = null} />
          </DialogFooter>
        </Dialog>}
      </div>
    );
  }
}

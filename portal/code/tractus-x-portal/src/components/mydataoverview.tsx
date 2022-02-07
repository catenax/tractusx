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
import { SearchBox, Icon } from '@fluentui/react';
import { Link } from 'react-router-dom';

@observer
export default class MyDataOverview extends React.Component {

  public render() {

    const gridData: any[] = [
      {
        fileName: 'Vehicle BOM 01.07.2021',
        category: 'Traceability',
        type: 'upload',
        synced: 'NA',
        items: '21.578',
        size: '8.9MB',
        uploadData: 'today',
        user: 'Jane Doe',
        typeIcon: '',
        syncIcon: ''
      },
      {
        fileName: 'Update CO2 Footprint Q2/2021',
        category: 'Sustainability',
        type: 'sustain',
        synced: 'Yes',
        items: '21.578',
        size: '10.9MB',
        uploadData: '12.05.2021',
        user: 'Jane Doe',
        typeIcon: '',
        syncIcon: ''
      },
      {
        fileName: 'Vehicle BOM 02.07.2021',
        category: 'Traceability',
        type: 'upload',
        synced: 'Yes',
        items: '7.013',
        size: '5.1MB',
        uploadData: '05.02.2021',
        user: 'Jane Doe',
        typeIcon: '',
        syncIcon: ''
      },
      {
        fileName: 'Vehicle BOM 03.07.2021',
        category: 'Traceability',
        type: 'upload',
        synced: 'No',
        items: '',
        size: '',
        uploadData: '05.02.2021',
        user: 'Jane Doe',
        typeIcon: '',
        syncIcon: ''
      },
      {
        fileName: 'Initial Engineering BOM Q2/2021',
        category: 'Traceability',
        type: 'upload',
        synced: 'Yes',
        items: '6.987',
        size: '4.9MB',
        uploadData: '31.10.2020',
        user: 'Jane Doe',
        typeIcon: '',
        syncIcon: ''
      },
      {
        fileName: 'Component BOM ZF',
        category: 'Traceability',
        type: 'upload',
        synced: 'Yes',
        items: '6.987',
        size: '4.9MB',
        uploadData: '31.10.2020',
        user: 'ZF',
        typeIcon: '',
        syncIcon: ''
      },
      {
        fileName: 'New parts to upload for MT - SAP Catalog',
        category: 'Traceability',
        type: 'upload',
        synced: 'NA',
        items: '21.578',
        size: '8.9MB',
        uploadData: '22.10.2020',
        user: 'Jane Doe',
        typeIcon: '',
        syncIcon: ''
      }
    ]

    gridData.forEach(element => {
      if (element.type === 'upload') {
        element.typeIcon = <Icon className='mr5 flex1' iconName='Upload' title='Downloaded' />
        element.tooltip = 'Downloaded';
      }
      else {
        element.typeIcon = <Icon className='fs14 mr5 flex1' iconName='AllCurrency' title='Downloaded' />
      }
      if (element.synced === 'Yes') {
        element.syncIcon = <Icon className='pl5 flex1 fglgreen' iconName='CompletedSolid' title='Synchronized' />
      }
      else if (element.synced === 'No') {
        element.syncIcon = <Icon className='pl5 flex1 fgyellow' iconName='Warning' title='Sync failed' />
      }
      else if (element.synced === 'NA') {
        element.syncIcon = <Icon className='pl5 flex1' iconName='More' />
      }
    });

    return (
      <div className='w100pc bgf5 h100pc df fdc'>
        <div className='w100-60 df fdc ml30 mt30'>
          <div className='df bgf5 mb15 mt20'>
            <span className='fs16 bold flex3'>Data Overview</span>
            <span className='fs14 fggrey mr5 flex1'>Sort by:  <span className='fw600 fgblack'>upload date</span></span>
            <span className='fs14 fggrey mr5 flex1'>Filter:  <span className='fw600 fgblack'>none</span></span>
            <span className='fs14 fggrey flex1'><SearchBox className='bcwhite fgblack' placeholder='Search' /></span>
          </div>
        </div>
        <div className='w100-60 df fdc ml30 h100pc df fdc'>
          <div className='df mb24'>
            <span className='fs14 fggrey ml20 mr5 flex3'>file name</span>
            <span className='fs14 fggrey mr5 flex1'>category</span>
            <span className='fs14 fggrey mr5 flex1'>type</span>
            <span className='fs14 fggrey mr5 flex1'>synced</span>
            <span className='fs14 fggrey mr5 flex1'>items</span>
            <span className='fs14 fggrey mr5 flex1'>size</span>
            <span className='fs14 fggrey mr5 flex1'>upload date</span>
            <span className='fs14 fggrey mr35 flex1'>user</span>
          </div>
          {gridData.map((c, index) => (
            <div key={index} className='df bgwhite h36 mb5 aic'>
              <span className='fs14 bold mr5 ml20 flex3 minw100'>{c.fileName}</span>
              <span className='fs14 mr5 flex1'>{c.category}</span>
              {c.typeIcon}
              {c.syncIcon}
              <span className='fs14 mr5 flex1'>{c.items}</span>
              <span className='fs14 mr5 flex1'>{c.size}</span>
              <span className='fs14 mr5 flex1'>{c.uploadData}</span>
              <span className='fs14 mr5 flex1'>{c.user}</span>
              <Icon className='mr10 fs20' iconName='MoreVertical' />
            </div>
          ))}
          <div className='pb12' />
          {/* <div className='df bgf5 h36 mb5 aic mt20'>
            <Icon className='bold mr10 mt5' iconName='Forward' />
            <span className='bold'>Find more Data sets in the
              <Link className='fglgreen fs14 bold mr5 tdn ml5' to='/home/datacatalog'>DATA CATALOG</Link>
            </span>
          </div> */}
        </div>
      </div>
    );
  }
}

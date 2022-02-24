// Copyright (c) 2021 T-Systems
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

import { IContextualMenuItem, PrimaryButton } from '@fluentui/react';
import * as React from 'react';
import { useState } from 'react';
import { Link } from 'react-router-dom';
import ErrorMessage from '../ErrorMessage';
import DescriptionList from '../lists/DescriptionList';
import Loading from '../loading';
import HelpContextMenu from '../navigation/HelpContextMenu/HelpContextMenu';
import ListCountSelector from '../navigation/ListCountSelector';
import Pagination from '../navigation/Pagination';
import { getTwins } from './data';
import { TwinList } from './interfaces';

const helpMenuItems: IContextualMenuItem[] = [
      {
        key: 'howto',
        text: 'How to',
        href: 'https://confluence.catena-x.net/x/A2sAAQ',
        target: '_blank',
      },
      {
        key: 'bestpractice',
        text: 'Best Practices',
        href: 'https://confluence.catena-x.net/x/_GoAAQ',
        target: '_blank',
      },
      {
        key: 'faq',
        text: 'FAQ',
        href: 'https://confluence.catena-x.net/x/_2oAAQ',
        target: '_blank',
      },
      {
        key: 'govprocess',
        text: 'Governance Process',
        href: 'https://confluence.catena-x.net/x/AWsAAQ',
        target: '_blank',
      },
];

const DEFAULT_PAGE_SIZE = 10;
const DEFAULT_PAGE = 0;

function DigitalTwinOverview(){
  const [data, setData] = useState<TwinList>();
  const [filterParams, setFilterParams] = useState(new URLSearchParams(`page=${DEFAULT_PAGE}&pageSize=${DEFAULT_PAGE_SIZE}`))
  const [error, setError] = useState<[]>();
  const [selectedPageSize, setSelectedPageSize] = useState<number>(DEFAULT_PAGE_SIZE);

  React.useEffect(()=>{
    updateTwins();
  }, [filterParams]);

  const updateTwins = () => {
    getTwins(filterParams)
      .then(
        res => setData(res),
        error => setError(error.message)
      );
  }

  const updateFilterParams = (params: string) => {
    setFilterParams(new URLSearchParams(params));
  }

  const onItemCountClick = (count: number) => {
    setSelectedPageSize(count);
    updateFilterParams(`page=0&pageSize=${count}`);
  }

  const onPageBefore = () => {
    updateFilterParams(`page=${data.currentPage - 1}&pageSize=${selectedPageSize}`);
  }

  const onPageNext = () => {
    updateFilterParams(`page=${data.currentPage + 1}&pageSize=${selectedPageSize}`);
  }

  return (
    <div className='p44 df fdc'>
      <HelpContextMenu menuItems={helpMenuItems}></HelpContextMenu>
      {data ?
        <div>
          <h1 className="fs24 bold mb20">Digital Twins</h1>
          <ListCountSelector activeCount={selectedPageSize} onCountClick={onItemCountClick}/>
          {data.items.length > 0 ?
            <div className="df fwrap mt20">
              {data.items.map(twin => (
                <Link key={twin.identification} className="m5 p20 bgpanel flex40 br4 bsdatacatalog tdn" to={{
                  pathname: `/home/digitaltwin/${twin.identification}`
                }}>
                  <h2 className='fs24 fg191 bold mb20'>{twin.idShort}</h2>
                  {twin.submodelDescriptors && <DescriptionList title="Submodel count:" description={twin.submodelDescriptors.length}/>}
                  {twin.specificAssetIds && <DescriptionList title="specific assets count:" description={twin.specificAssetIds.length}/>}
                </Link>
              ))}
              <Pagination pageNumber={data.currentPage + 1}
                onPageBefore={onPageBefore}
                onPageNext={onPageNext}
                totalPages={data.totalPages}>
              </Pagination>
            </div> :
            <div className="df fdc aic">
              <span className="fs20">No matches found!</span>
          </div>
          }
        </div> :
      <div className="h100pc df jcc">
        {error ? <ErrorMessage error={error} /> : <Loading />}
      </div>
    }
    </div>
  );
}

export default DigitalTwinOverview;

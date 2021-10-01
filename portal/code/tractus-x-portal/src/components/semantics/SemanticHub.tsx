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

import * as React from 'react';
import { Link } from 'react-router-dom';
import { Dropdown, IDropdownOption, IDropdownStyles, PrimaryButton, SearchBox } from '@fluentui/react';
import DescriptionList from '../lists/descriptionlist';
import { encodeID, getModels } from './data';
import ErrorMessage from '../ErrorMessage';
import Loading from '../loading';

export default class SemanticHub extends React.Component<any, any>{
  
  constructor(props) {
    super(props);
    this.state = { 
      models: null, 
      filterParams: new URLSearchParams(''),
      searchInput: '',
      error: null
    };

    this.clearFilter = this.clearFilter.bind(this);
    this.onSearchChange = this.onSearchChange.bind(this);
    this.onSearchClear = this.onSearchClear.bind(this);
    this.onInputSearch = this.onInputSearch.bind(this);
    this.onTypeDropdownChange = this.onTypeDropdownChange.bind(this);
    this.onAvailableDropdownChange = this.onAvailableDropdownChange.bind(this);
  }

  componentDidMount() {
    this.setModels();
  }

  componentDidUpdate(prevProps, prevState){
    if (this.state.filterParams !== prevState.filterParams) {
      this.setModels();
    }
  }

  setModels(){
    getModels(this.state.filterParams)
      .then(
        models => this.setState({models}), 
        error => this.setState({error: error.message})
      );
  }

  private getIcon(data: any) {
    return <span className='pt5'><svg version="1.1" x="0px" y="0px" width="20px" height="20px" viewBox="0 0 32 32" enableBackground="new 0 0 32 32" id="svg2"><g id="background"><rect fill="none" width="32" height="32" id="rect6" /></g><path id="path9"
      d="M 26.997,20.09 V 11.91 C 29.834,11.435 32,8.973 32,6 32,2.687 29.312,0 26,0 c -3.316,0 -6,2.687 -6,6 0,0.609 0.092,1.196 0.261,1.75 l -9.423,4.711 C 9.747,10.972 7.989,10 6,10 c -3.314,0 -6,2.687 -6,6 0,3.312 2.686,6 6,6 1.99,0 3.748,-0.973 4.839,-2.464 l 9.423,4.712 C 20.092,24.803 20,25.391 20,26 c 0,3.312 2.684,6 6,6 3.312,0 6,-2.688 6,-6 0,-2.972 -2.166,-5.435 -5.003,-5.91 z M 11.739,17.751 C 11.908,17.197 12,16.609 12,16 12,15.39 11.908,14.801 11.738,14.247 l 9.422,-4.71 c 0.903,1.235 2.266,2.109 3.839,2.374 v 8.18 c -1.573,0.265 -2.934,1.139 -3.838,2.372 l -9.422,-4.712 z" />
      </svg>
    </span>
  }

  setFilter(name, value){
    let currentFilter = new URLSearchParams(this.state.filterParams);
    if(currentFilter.has(name)){
      currentFilter.set(name, value);
    } else {
      currentFilter.append(name, value);
    }
    console.log(value);
    this.setState({filterParams: currentFilter});
  }

  clearFilter(){
    this.setState({searchInput: ''});
    this.setState({filterParams:  new URLSearchParams('')});
  }

  onSearchChange(value){
    this.setState({searchInput: value});
  }

  onSearchClear(){
    this.setState({searchInput: ''});
    this.onInputSearch('');
  }

  onInputSearch(input){
    if(input.includes('.')){
      this.setFilter('namespaceFilter', encodeID(input));
    } else {
      this.setFilter('nameFilter', input);
    }
  }
  
  onTypeDropdownChange(ev, option){
    this.setFilter('type', option.text);
  }

  onAvailableDropdownChange(ev, option){
    const convertedInput = option.key === 1;
    this.setFilter('isPrivate', convertedInput);
  }

  encodeID(id){
    return encodeID(id);
  }

  public render() {
    const dropdownStyles: Partial<IDropdownStyles> = {
      dropdown: { width: 150, marginRight: 20 },
    };
    const availableOptions: IDropdownOption[] = [
      { key: 1, text: 'Private' },
      { key: 0, text: 'Public' }
    ];
    const vocabOptions: IDropdownOption[] = [
      { key: 'bamm', text: 'BAMM' },
      { key: 'owl', text: 'OWL' }
    ];
    return (
      <div className='p44'>
        {this.state.models ? 
          <div>
            <div className="df aife jcfe mb20">
              <Dropdown placeholder="Filter"
                label="Bas Vocabulary"
                options={vocabOptions}
                styles={dropdownStyles}
                onChange={this.onTypeDropdownChange}
              />
              <Dropdown placeholder="Filter"
                label="Availability"
                options={availableOptions}
                styles={dropdownStyles}
                onChange={this.onAvailableDropdownChange}
              />
              <SearchBox className="w300" placeholder="Filter for Name or Namespace" value={this.state.searchInput} onSearch={this.onInputSearch} onClear={this.onSearchClear} onChange={(_, newValue) => this.onSearchChange(newValue)}/>
            </div>
            {this.state.models.length > 0 ? 
              <div className="df fwrap">
                {this.state.models.map((data, index) => (
                  <div key={index} className='m5 p20 bgpanel flex40 br4 bsdatacatalog'>
                    <div className='df aifs mb15'>
                      <Link className="mr20 tdn" to={{
                        pathname: `/home/semanticmodel/${this.encodeID(data.id)}`
                      }}>
                        <span className='fs24 bold fg191'>{data.name}</span>
                      </Link>
                    </div>
                    <span className='fs14 pt8'>{data.description}</span>
                    <div className='mt20 mb30'>
                      <DescriptionList title="Publisher" description={data.publisher} />
                      <DescriptionList title="Namespace" description={data.id ? data.id : '-'} />
                      <DescriptionList title="Model Version" description={data.version} />
                      <DescriptionList title="Vocabulary Type" description={data.type} />
                      <DescriptionList title="Private" description={String(data.private)} />
                    </div>
                  </div>
                ))}
              </div> : 
              <div className="df fdc aic">
                <span className="fs20">No matches found!</span>
                <PrimaryButton text='Reset Filter' className="mt20" onClick={this.clearFilter} />
              </div>
            }
          </div> : 
          <div className="h100pc df jcc">
            {this.state.error ? <ErrorMessage error={this.state.error} /> : <Loading />}
          </div>
        }
      </div>
    );
  }
}

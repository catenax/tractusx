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

import { Dropdown, IDropdownOption, IDropdownStyles, PrimaryButton, SearchBox } from '@fluentui/react';
import * as React from 'react';
import { Link } from 'react-router-dom';
import ErrorMessage from '../ErrorMessag';
import DescriptionList from '../lists/descriptionlist';
import Loading from '../loading';
import { DigitalTwin, getTwins } from './data';

export default class DigitalTwins extends React.Component<DigitalTwin, any>{

  constructor(props) {
    super(props);
    this.state = { 
      twins: null,
      error: null,
      searchInput: ''
    };

    this.clearFilter = this.clearFilter.bind(this);
    this.onSearchChange = this.onSearchChange.bind(this);
    this.onSearchClear = this.onSearchClear.bind(this);
    this.onInputSearch = this.onInputSearch.bind(this);
    this.onDropdownChange = this.onDropdownChange.bind(this);
  }

  componentDidMount() {
    this.setTwins();
  }

  setTwins(){
    getTwins()
      .then(
        twins => this.setState({twins: twins.items}),
        error => this.setState({error: error.message})
      );
  }

  onSearchChange(value){
    this.setState({searchInput: value});
  }

  onSearchClear(){
    this.setState({searchInput: ''});
    this.onInputSearch('');
  }

  clearFilter(){
    this.setTwins();
  }

  onInputSearch(input){
    const filteredTwins = this.state.twins.filter(twin => twin.id.includes(input));
    this.setState({twins: filteredTwins});
  }

  onDropdownChange(ev, option){
    const filteredTwins = this.state.twins.filter(twin => twin.manufacturer === option);
    this.setState({twins: filteredTwins});
  }

  public render() {
    const dropdownStyles: Partial<IDropdownStyles> = {
      dropdown: { width: 150, marginRight: 20 },
    };
    const manufacturerOptions: IDropdownOption[] = [
      { key: 'bmw', text: 'BMW' },
      { key: 'bosch', text: 'Bosch' },
      { key: 'compA', text: 'Company A' },
      { key: 'compB', text: 'Company B' },
      { key: 'compC', text: 'Company C' }
    ];
    return (
      <div className='p44'>
        {this.state.twins ?
          <div>
            <h1 className="fs24 bold mb20">Digital Twins</h1>
            <div className="df aife jcfe mb20">
              <Dropdown placeholder="Filter"
                label="Twin Manufacturer"
                options={manufacturerOptions}
                styles={dropdownStyles}
                onChange={this.onDropdownChange}
              />
              <SearchBox className="w300"
                placeholder="Filter ID or description"
                value={this.state.searchInput}
                onSearch={this.onInputSearch}
                onClear={this.onSearchClear}
                onChange={(_, newValue) => this.onSearchChange(newValue)}/>
            </div>
            {this.state.twins.length > 0 ?
              <div className="df fwrap">
                {this.state.twins.map(twin => (
                  <Link key={twin.id} className="m5 p20 bgpanel flex40 br4 bsdatacatalog tdn" to={{
                    pathname: `/home/digitaltwin/${twin.id}`
                  }}>
                    <h2 className='fs24 fg191 bold'>{twin.id}</h2>
                    <p className='fs14 fg191 pt8 mb20'>{twin.description}</p>
                    <DescriptionList title="Manufacturer:" description={twin.manufacturer}/>
                    <DescriptionList title="Aspects count:" description={twin.localIdentifiers.length}/>
                    <DescriptionList title="local Identifiers count:" description={twin.aspects.length}/>
                  </Link>
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

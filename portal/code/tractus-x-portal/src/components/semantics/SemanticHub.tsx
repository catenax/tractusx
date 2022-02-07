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
import { Dropdown, IContextualMenuItem, IDropdownOption, IDropdownStyles, PrimaryButton, SearchBox } from '@fluentui/react';
import DescriptionList from '../lists/descriptionlist';
import { getModels, Status } from './data';
import ErrorMessage from '../ErrorMessage';
import Loading from '../loading';
import Pagination from '../navigation/Pagination';
import ListCountSelector from '../navigation/ListCountSelector';
import HelpContextMenu from '../navigation/HelpContextMenu/HelpContextMenu';

const defaultPage = 0;
const defaultPageSize = 10;

enum SearchType {
  '_NAME_' = "Name",
  '_DESCRIPTION_' = "Description",
  'bamm:Aspect' = "Aspect", 
  'bamm:Property' = "Property",
  'bamm:Entity' = "Entity",
  'bamm:Constraint' = "Constraint", 
  'bamm:Characteristic' = "Characteristic",
  'bamm-c:Measurement' = "Measurement", 
  'bamm-c:Quantifiable' = "Quantifiable",
  'bamm-c:Enumeration' = "Enumeration",  
  'bamm-c:SingleEntity' = "SingleEntity",  
  'bamm-c:Set' = "Set", 
  'bamm-c:Collection' = "Collection"
}

export default class SemanticHub extends React.Component<any, any>{
  filterActive = false;
  helpUrl = 'https://confluence.catena-x.net/display/ARTI/Semantic+Hub'

  constructor(props) {
    super(props);
    this.state = { 
      models: null,
      reloadDropdown: false,
      filterParams: new URLSearchParams(`page=${defaultPage}&pageSize=${defaultPageSize}`),
      searchNSInput: '',
      searchInput: '',
      error: null,
      currentPage: defaultPage,
      pageSize: defaultPageSize,
      totalPages: 1
    };

    this.clearFilter = this.clearFilter.bind(this);
    this.onNSSearchChange = this.onNSSearchChange.bind(this);
    this.onSearchChange = this.onSearchChange.bind(this);
    this.onNSSearchClear = this.onNSSearchClear.bind(this);
    this.onSearchClear = this.onSearchClear.bind(this);
    this.onNSInputSearch = this.onNSInputSearch.bind(this);
    this.onInputSearch = this.onInputSearch.bind(this);
    this.onSearchTypeDropdownChange = this.onSearchTypeDropdownChange.bind(this);
    this.onTypeDropdownChange = this.onTypeDropdownChange.bind(this);
    this.onStatusDropdownChange = this.onStatusDropdownChange.bind(this);
    this.onPageBefore = this.onPageBefore.bind(this);
    this.onPageNext = this.onPageNext.bind(this);
    this.onItemCountClick = this.onItemCountClick.bind(this);
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
        models => { 
          this.setState({totalPages: models.totalPages});
          this.setState({models: models.items});
        }, 
        error => this.setState({error: error.message}));
  }

  setFilter(...params: { name: string, value: any }[]){
    let currentFilter = new URLSearchParams(this.state.filterParams);
    params.map(param => {
      if(currentFilter.has(param.name)){
        currentFilter.set(param.name, param.value);
      } else {
        currentFilter.append(param.name, param.value);
      }
      if(param.name !== 'pageSize') this.filterActive = true;
      return null;
    })
    this.setState({filterParams: currentFilter});
  }

  clearFilter(){
    this.reloadDropdown();
    this.setState({
      searchInput: '',
      searchNSInput: '',
      currentPage: defaultPage,
      filterParams: new URLSearchParams(`page=${defaultPage}&pageSize=${this.state.pageSize}`)
    });
    this.filterActive = false;
  }

  reloadDropdown(){
    this.setState({reloadDropdown: true}, () => this.setState({reloadDropdown: false}));
  }

  onNSSearchChange(value){
    if(value === undefined) value = '';
    this.setState({searchNSInput:value});
  }

  onSearchChange(value){
    if(value === undefined) value = '';
    this.setState({searchInput: value});
  }

  onSearchClear(){
    this.setState({searchInput: ''});
    this.onInputSearch('');
  }

  onNSSearchClear(){
    this.setState({searchNSInput:''});
    this.onNSInputSearch('');
  }

  onInputSearch(input){
    this.setFilter({name: 'nameFilter', value: encodeURIComponent(input)});
  }
  
  onNSInputSearch(input){
    this.setFilter({name: 'namespaceFilter', value: encodeURIComponent(input)});
  }

  onTypeDropdownChange(ev, option){
    this.setFilter({name: 'type', value: option.text});
  }

  onStatusDropdownChange(ev, option){
    this.setFilter({name: 'status', value: option.text});
  }

  onSearchTypeDropdownChange(ev, option){
    this.setFilter({name: 'nameType', value: option.key});
  }

  onPageBefore(){
    this.setState({currentPage: this.state.currentPage - 1}, () => this.updatePageFilter());
  }

  onPageNext(){
    this.setState({currentPage: this.state.currentPage + 1}, () => this.updatePageFilter());
  }

  updatePageFilter(){
    this.setFilter({name: 'page', value: this.state.currentPage});
  }

  onItemCountClick(count: number){
    const paramPageSize = { name: 'pageSize', value: count };
    if(this.state.currentPage > defaultPage){
      const paramDefaultPage = { name: 'page', value: defaultPage };
      this.setState({pageSize: count, currentPage: defaultPage});
      this.setFilter(paramPageSize, paramDefaultPage);
    } else {
      this.setState({pageSize: count}, () => this.setFilter(paramPageSize))
    }
  }

  public render() {
    const helpMenuItems: IContextualMenuItem[] = [
      {
        key: 'semhub',
        text: 'How To',
        href: 'https://confluence.catena-x.net/x/dEMAAQ',
        target: '_blank',
      },
      {
        key: 'modelling',
        text: 'Best Practices Modelling',
        href: 'https://confluence.catena-x.net/x/fEMAAQ',
        target: '_blank',
      },
      {
        key: 'govproc',
        text: 'Governance Process',
        href: 'https://confluence.catena-x.net/x/d0MAAQ',
        target: '_blank',
      },
      {
        key: 'faq',
        text: 'FAQ',
        href: 'https://confluence.catena-x.net/x/ekMAAQ',
        target: '_blank',
      },
    ];
    const dropdownStyles: Partial<IDropdownStyles> = {
      dropdown: { width: 150, marginRight: 20 },
    };
    const dropdownStyles2: Partial<IDropdownStyles> = {
      dropdown: { width: 150, marginLeft:20, marginRight: 20 },
    };
    const vocabOptions: IDropdownOption[] = [
      { key: 'bamm', text: 'BAMM' },
      { key: 'rdf', text: 'RDF' },
      { key: 'owl', text: 'OWL' }
    ];
    const statusOptions: IDropdownOption[] = Object.keys(Status).map(key => (
      { key: key, text: Status[key]}
    ));
    const searchTypeOptions: IDropdownOption[] = Object.keys(SearchType).map(key => (
      { key: key, text: SearchType[key]}
    ));
    const filterStyles = {minHeight: '60px'};
    return (
      <div className='p44 df fdc'>
        <HelpContextMenu menuItems={helpMenuItems}></HelpContextMenu>
        {this.state.models ? 
          <div>
            <div className="df aife jcfe mb20" style={filterStyles}>
              { !this.state.reloadDropdown &&
                <div className="df">
                  <Dropdown placeholder="Filter"
                    label="Vocabulary Type"
                    options={vocabOptions}
                    styles={dropdownStyles}
                    onChange={this.onTypeDropdownChange}
                  />
                  <Dropdown placeholder="Filter"
                    label="Status"
                    options={statusOptions}
                    styles={dropdownStyles}
                    onChange={this.onStatusDropdownChange}
                  />
                </div>
              }
              <SearchBox className="w300"
                placeholder="Filter for Namespace"
                value={this.state.searchNSInput}
                onSearch={this.onNSInputSearch}
                onClear={this.onNSSearchClear}
                onChange={(_, newValue) => this.onNSSearchChange(newValue)}
              />
              { !this.state.reloadDropdown && 
                <Dropdown placeholder="Select"
                    label="Object Type"
                    options={searchTypeOptions}
                    styles={dropdownStyles2}
                    onChange={this.onSearchTypeDropdownChange}
                />}
              <SearchBox className="w300"
                placeholder="Filter for Object Name"
                value={this.state.searchInput}
                onSearch={this.onInputSearch}
                onClear={this.onSearchClear}
                onChange={(_, newValue) => this.onSearchChange(newValue)}
              />
              {this.filterActive && <PrimaryButton onClick={this.clearFilter} text="Clear Filter" className="ml20"/> }
            </div>
            {this.state.models.length > 0 ?
              <div>
                <ListCountSelector activeCount={this.state.pageSize} onCountClick={this.onItemCountClick}/>
                <div className="df fwrap mt20">
                  {this.state.models.map((data, index) => (
                    <div key={index} className='m5 p20 bgpanel flex40 br4 bsdatacatalog'>
                      <div className='df aifs mb15'>
                        <Link className="mr20 tdn" to={{
                          pathname: `/home/semanticmodel/${encodeURIComponent(data.urn)}`
                        }}>
                          <span className='fs24 bold fg191'>{data.name}</span>
                        </Link>
                      </div>
                      <span className='fs14 pt8'>{data.description}</span>
                      <div className='mt20 mb30'>
                        <DescriptionList title="Namespace" description={data.urn ? data.urn : '-'} />
                        <DescriptionList title="Model Version" description={data.version} />
                        <DescriptionList title="Vocabulary Type" description={data.type} />
                        <DescriptionList title="Status" description={data.status} />
                      </div>
                    </div>
                  ))}
                  <Pagination pageNumber={this.state.currentPage + 1}
                    onPageBefore={this.onPageBefore}
                    onPageNext={this.onPageNext}
                    totalPages={this.state.totalPages}>
                  </Pagination>
                </div>
              </div>

              : 
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

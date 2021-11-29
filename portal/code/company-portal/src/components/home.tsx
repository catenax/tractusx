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
import Header from './header';
import { Nav, INavStyles, INavLinkGroup, INavLink } from '@fluentui/react/lib/Nav';
import { Redirect, Route, RouteComponentProps, Switch, withRouter } from 'react-router-dom';
import { ThemeProvider } from '@fluentui/react';
import { observable } from 'mobx';
import Onboarding from './onboarding';

const navStyles: Partial<INavStyles> = {
  root: {
    width: 250,
    boxSizing: 'border-box',
    border: '1px solid #eee',
    overflowY: 'auto'
  },
  // these link styles override the default truncation behavior
  link: {
    whiteSpace: 'normal',
    lineHeight: 'inherit'
  }
};

// adding an empty title string to each link removes the tooltip;
// it's unnecessary now that the text wraps, and will not truncate
const navLinkGroups: INavLinkGroup[] = [
  {
    links: [
      {
        name: 'Getting Started',
        url: '/home/onboarding',
        key: 'key1',
        expandAriaLabel: 'Expand section',
        collapseAriaLabel: 'Collapse section',
        title: ''
      }
    ]
  }
];


const noNav = [];

@observer
class Home extends React.Component<RouteComponentProps> {
  @observable public static selectedKey1 = 'key1';
  @observable public static selectedKey2 = 'key0';

  linkClick(ev: React.MouseEvent<HTMLElement, MouseEvent>, item: INavLink): void {
    ev.stopPropagation();
    ev.preventDefault();
    Home.selectedKey1 = 'key' + (navLinkGroups[0].links.indexOf(item) + 1).toString();
    if (Home.selectedKey1 === 'key0') {
      Home.selectedKey1 = 'key1';
    }

    this.props.history.push(item.url);
  }

  hasLeftTopNavi(): boolean{
    return noNav.filter(nav => window.location.href.includes(nav)).length === 0;
  }

  public render() {
    let groups = navLinkGroups;
  
    return (
      <div className='w100pc h100pc df fdc bgf5'>
        <Header href={window.location.href} />
        <div className='df w100pc flex1'>
          <ThemeProvider theme={{ palette: { themePrimary: '#E6AA1E' } }}>
            <div className='df fdc w250 h100pc'>
              {this.hasLeftTopNavi() && <Nav className='bgwhite' selectedKey={Home.selectedKey1} ariaLabel='Navigation panel' styles={navStyles} groups={groups}
                onLinkClick={(ev, item) => this.linkClick(ev, item)} />}
              <div className='flex1 bgwhite' />
           
            </div>
          </ThemeProvider>
          <div className='flex1 h100pc ova'>
            <Switch>
              <Redirect path='/home' exact to='/home/onboarding' />
              <Route path='/home/onboarding' component={(props) => <Onboarding {...props} />} />
            </Switch>
          </div>
        </div>
      </div>
    );
  }
}

export default withRouter(Home);

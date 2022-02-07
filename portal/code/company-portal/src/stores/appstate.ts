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

import { observable } from 'mobx';
import { Application } from '../data/application';
import UserService from '../helpers/UserService';

const A = {
  id: '0253dd4d-35af-43f5-a84c-7cc28084032a',
  title: 'Data Upload App', rating: 4.9, downloads: 577, tags: ['FREE FOR USE', 'UPLOADER', 'DATA UPLOAD', 'CONNECTOR', 'PRODUCTIVITY', 'IDS', 'MASTER DATA'],
  screenshots: ['/dataupload1.png', '/dataupload2.png', '/dataupload1.png', '/dataupload2.png'],
  description: '<b>Upload your data via standard connector</b><br/>Far far away, behind the word mountains, far from the countries Vokalia and Consonantia, there live the blind texts. Separated they live in Bookmarksgrove right at the coast of the Semantics, a large language ocean. A small river named Duden flows by their place and supplies it with the necessary regelialia. It is a paradisematic country, in which roasted parts of sentences fly into your mouth. <br/><br/><b>Easy to use and state of the art</b><br/>Even the all-powerful Pointing has no control about the blind texts it is an almost unorthographic life One day however a small line of blind text by the name of Lorem Ipsum decided to leave for the far World of Grammar. The Big Oxmox advised her not to do so, because there were thousands of bad Commas, wild  Question Marks and devious Semikoli, but the Little Blind Text didn’t listen.',
  usage: 'free for use',
  purchase: 'OPEN',
  companyName: 'Catena-X', url: '/dataupload'
};

const B = {
  id: '0253dd4d-35af-43f5-a84c-7cc28084032b',
  title: 'PartChain', rating: 4.7, downloads: 183987, tags: ['FREE FOR USE', 'TRACKING', 'TRACEABILITY', 'CATENA-X'],
  screenshots: ['/PartChain4.png', '/PartChain2.png', '/PartChain3.png', '/PartChain1.png', '/PartChain5.png'],
  description: `<b>Seamless part traceability through the n-tier supply chain.</b><br />

  PartChain is an open-source software tool that allows you to track components in the n-tier supply chain. In addition to the unique relationship knowledge you get detailed information about the components of your direct suppliers as well as your direct customers. This lets you answer questions such as:<br />
  <br/>
  <li>What’s the exact lead time between the production of a subcomponent and your own components?</li>
  <li>To where in the world are my components distributed and where are my suppliers located?</li>
  <li>What’s the exact composition of my component on a unique ID level?</li>
  <br />Because all of that is important information, PartChain keeps a strong one-up one-down visibility rule. You and the other parties in the network always only see their direct suppliers and customers data as well as their own data. Your competitors won’t be able to get any sensitive information about your production data.<br />

  <br/><b>Recall management made easy</b><br />
  Because you and all other partners in the network now know the exact composition of the components that are produced, recall management is much simpler than before. No more tedious and messy emails, excel sheets and database dumps in case of a quality issue. You simply select the components that you need to recall, choose a quality alert criticality and distribute the information in the network. Of course, only your direct customers will be notified. In case you receive a quality alert, you can see at one glimpse, which of your parts could be affected by defective supplier parts and also immediately warn your own customers. All with one click.<br />

  <br/><b>Unlock the potential for dozens of new use cases</b><br />
  Recall management of course isn’t the only use case for PartChain. As soon as you have enough relationship about your supply chain, you can drastically simplify i.e.<br />
  <br/>
  <li>CO2 Emission handling</li>
  <li>Circular Economy</li>
  <li>Analysis of Quality Documents</li>
  <li>Customs processes</li>
  <li>Demand and Capacity Management</li>
  <li>and many more</li>
`,
  purchase: 'OPEN',
  companyName: 'Catena-X',
  usage: 'free for use', url: 'https://ui.zf.dev.catenax.partchain.dev/'
}

const C = {
  id: '0253dd4d-35af-43f5-a84c-7cc28084032c',
  title: 'CO2 Fußabdruck', rating: 3.8, downloads: 577, tags: ['FREE FOR USE', 'UPLOADER', 'DATA UPLOAD', 'CONNECTOR', 'PRODUCTIVITY', 'IDS', 'MASTER DATA'],
  screenshots: ['/dataupload1.png', '/dataupload2.png', '/dataupload1.png', '/dataupload2.png'],
  description: '<b>Calculate your ECO Footprint</b><br/>Far far away, behind the word mountains, far from the countries Vokalia and Consonantia, there live the blind texts. Separated they live in Bookmarksgrove right at the coast of the Semantics, a large language ocean. A small river named Duden flows by their place and supplies it with the necessary regelialia. It is a paradisematic country, in which roasted parts of sentences fly into your mouth. <br/><br/><b>Easy to use and state of the art</b><br/>Even the all-powerful Pointing has no control about the blind texts it is an almost unorthographic life One day however a small line of blind text by the name of Lorem Ipsum decided to leave for the far World of Grammar. The Big Oxmox advised her not to do so, because there were thousands of bad Commas, wild  Question Marks and devious Semikoli, but the Little Blind Text didn’t listen.',
  usage: 'per month',
  purchase: 'PURCHASE',
  companyName: 'T-System', url: ''
}

const D = {
  id: '0253dd4d-35af-43f5-a84c-7cc28084032d',
  title: 'Material Traceability', rating: 4.5, downloads: 577,
  tags: ['SUPPLY CHAIN ANALYTICS', 'SUPPLY CHAIN VISUALIZATION', 'PART TRACEABILITY', 'ALARMS', 'RECALLS', 'ROLE VIEWS', 'NETWORK VIEW', 'UPSTREAM VIEW', 'DOWNSTREAM VIEW'],
  screenshots: ['/material1.png'],
  description: `<b>Capabilities</b><br /><br />
  <li>Visualization of complex supply chains - connecting multiple business partners for inter-company collaboration and transparency</li>
<li>Role-Based Access for network participant - trust chain across an n-tiered value chain from raw material batch origin to finished product.</li>
<li>Streamlined Recalls - Initiate cross-company alerting for product issues</li>
<li>Tight integration with your logistics business - Linkage of data across suppliers is enabled by matching logistic events</li>
<br />
<b>Description</b><br />
Create an Intelligent Enterprise with Advanced Logistics Collaboration and Insights. SAP Logistics Business Network, material traceability option connects business partners for inter-company collaboration and transparency. It supports a comprehensive set of capabilities, allowing to manage freight more efficiently, benefit from situational awareness through track and trace, and create a trust chain for up- and downstream product genealogy.
`,
  usage: 'per year',
  purchase: 'PURCHASE',
  companyName: 'SAP', url: ''
};

const E = {
  id: '0253dd4d-35af-43f5-a84c-7cc28084032e',
  title: 'Bedarfs-& kapazitätsmanagement', rating: 4.1, downloads: 577, tags: ['FREE FOR USE', 'UPLOADER', 'DATA UPLOAD', 'CONNECTOR', 'PRODUCTIVITY', 'IDS', 'MASTER DATA'],
  screenshots: ['/dataupload1.png', '/dataupload2.png', '/dataupload1.png', '/dataupload2.png'],
  description: '<b>Upload your data via standard connector</b><br/>Far far away, behind the word mountains, far from the countries Vokalia and Consonantia, there live the blind texts. Separated they live in Bookmarksgrove right at the coast of the Semantics, a large language ocean. A small river named Duden flows by their place and supplies it with the necessary regelialia. It is a paradisematic country, in which roasted parts of sentences fly into your mouth. <br/><br/><b>Easy to use and state of the art</b><br/>Even the all-powerful Pointing has no control about the blind texts it is an almost unorthographic life One day however a small line of blind text by the name of Lorem Ipsum decided to leave for the far World of Grammar. The Big Oxmox advised her not to do so, because there were thousands of bad Commas, wild  Question Marks and devious Semikoli, but the Little Blind Text didn’t listen.',
  usage: 'demo',
  purchase: 'PURCHASE',
  companyName: 'German Edge Cloud', url: ''
};

const F = {
  id: '0253dd4d-35af-43f5-a84c-7cc28084032f',
  title: 'Kreislaufwirtschaft', rating: 4.7, downloads: 577, tags: ['FREE FOR USE', 'UPLOADER', 'DATA UPLOAD', 'CONNECTOR', 'PRODUCTIVITY', 'IDS', 'MASTER DATA'],
  screenshots: ['/dataupload1.png', '/dataupload2.png', '/dataupload1.png', '/dataupload2.png'],
  description: '<b>Upload your data via standard connector</b><br/>Far far away, behind the word mountains, far from the countries Vokalia and Consonantia, there live the blind texts. Separated they live in Bookmarksgrove right at the coast of the Semantics, a large language ocean. A small river named Duden flows by their place and supplies it with the necessary regelialia. It is a paradisematic country, in which roasted parts of sentences fly into your mouth. <br/><br/><b>Easy to use and state of the art</b><br/>Even the all-powerful Pointing has no control about the blind texts it is an almost unorthographic life One day however a small line of blind text by the name of Lorem Ipsum decided to leave for the far World of Grammar. The Big Oxmox advised her not to do so, because there were thousands of bad Commas, wild  Question Marks and devious Semikoli, but the Little Blind Text didn’t listen.',
  usage: 'free for use',
  purchase: 'PURCHASE',
  companyName: 'Catena-X', url: ''
};

const G = {
  id: '0253dd4d-35af-43f5-a84c-7cc28084033a',
  title: 'SAP Adaptor', rating: 4.8, downloads: 577, tags: ['FREE FOR USE', 'UPLOADER', 'DATA UPLOAD', 'CONNECTOR', 'PRODUCTIVITY', 'IDS', 'MASTER DATA'],
  screenshots: ['/dataupload1.png', '/dataupload2.png', '/dataupload1.png', '/dataupload2.png'],
  description: '<b>Upload your data via standard connector</b><br/>Far far away, behind the word mountains, far from the countries Vokalia and Consonantia, there live the blind texts. Separated they live in Bookmarksgrove right at the coast of the Semantics, a large language ocean. A small river named Duden flows by their place and supplies it with the necessary regelialia. It is a paradisematic country, in which roasted parts of sentences fly into your mouth. <br/><br/><b>Easy to use and state of the art</b><br/>Even the all-powerful Pointing has no control about the blind texts it is an almost unorthographic life One day however a small line of blind text by the name of Lorem Ipsum decided to leave for the far World of Grammar. The Big Oxmox advised her not to do so, because there were thousands of bad Commas, wild  Question Marks and devious Semikoli, but the Little Blind Text didn’t listen.',
  usage: 'free for use',
  purchase: 'PURCHASE',
  companyName: 'SAP', url: ''
};

const H = {
  id: '0253dd4d-35af-43f5-a84c-7cc280840330',
  title: 'Siemens Adaptor', rating: 4.7, downloads: 877, tags: ['FREE FOR USE', 'UPLOADER', 'DATA UPLOAD', 'CONNECTOR', 'PRODUCTIVITY', 'IDS', 'MASTER DATA'],
  screenshots: ['/dataupload1.png', '/dataupload2.png', '/dataupload1.png', '/dataupload2.png'],
  description: '<b>Upload your data via standard connector</b><br/>Far far away, behind the word mountains, far from the countries Vokalia and Consonantia, there live the blind texts. Separated they live in Bookmarksgrove right at the coast of the Semantics, a large language ocean. A small river named Duden flows by their place and supplies it with the necessary regelialia. It is a paradisematic country, in which roasted parts of sentences fly into your mouth. <br/><br/><b>Easy to use and state of the art</b><br/>Even the all-powerful Pointing has no control about the blind texts it is an almost unorthographic life One day however a small line of blind text by the name of Lorem Ipsum decided to leave for the far World of Grammar. The Big Oxmox advised her not to do so, because there were thousands of bad Commas, wild  Question Marks and devious Semikoli, but the Little Blind Text didn’t listen.',
  usage: 'free for use',
  purchase: 'PURCHASE',
  companyName: 'Siemens', url: ''
};

const I = {
  id: '0253dd4d-35af-43f5-a84c-7cc280840331',
  title: 'Data Quality Service', rating: 4.5, downloads: 877, tags: ['FREE FOR USE', 'UPLOADER', 'DATA UPLOAD', 'CONNECTOR', 'PRODUCTIVITY', 'IDS', 'MASTER DATA'],
  screenshots: ['/dataupload1.png', '/dataupload2.png', '/dataupload1.png', '/dataupload2.png'],
  description: '<b>Upload your data via standard connector</b><br/>Far far away, behind the word mountains, far from the countries Vokalia and Consonantia, there live the blind texts. Separated they live in Bookmarksgrove right at the coast of the Semantics, a large language ocean. A small river named Duden flows by their place and supplies it with the necessary regelialia. It is a paradisematic country, in which roasted parts of sentences fly into your mouth. <br/><br/><b>Easy to use and state of the art</b><br/>Even the all-powerful Pointing has no control about the blind texts it is an almost unorthographic life One day however a small line of blind text by the name of Lorem Ipsum decided to leave for the far World of Grammar. The Big Oxmox advised her not to do so, because there were thousands of bad Commas, wild  Question Marks and devious Semikoli, but the Little Blind Text didn’t listen.',
  usage: 'free for use',
  purchase: 'PURCHASE',
  companyName: 'T-Systems', url: ''
};

const J = {
  id: '0253dd4d-35af-43f5-a84c-7cc280840332',
  title: 'GEC Adaptor', rating: 4.1, downloads: 377, tags: ['FREE FOR USE', 'UPLOADER', 'DATA UPLOAD', 'CONNECTOR', 'PRODUCTIVITY', 'IDS', 'MASTER DATA'],
  screenshots: ['/dataupload1.png', '/dataupload2.png', '/dataupload1.png', '/dataupload2.png'],
  description: '<b>Upload your data via standard connector</b><br/>Far far away, behind the word mountains, far from the countries Vokalia and Consonantia, there live the blind texts. Separated they live in Bookmarksgrove right at the coast of the Semantics, a large language ocean. A small river named Duden flows by their place and supplies it with the necessary regelialia. It is a paradisematic country, in which roasted parts of sentences fly into your mouth. <br/><br/><b>Easy to use and state of the art</b><br/>Even the all-powerful Pointing has no control about the blind texts it is an almost unorthographic life One day however a small line of blind text by the name of Lorem Ipsum decided to leave for the far World of Grammar. The Big Oxmox advised her not to do so, because there were thousands of bad Commas, wild  Question Marks and devious Semikoli, but the Little Blind Text didn’t listen.',
  usage: 'free for use',
  purchase: 'PURCHASE',
  companyName: 'German Edge Cloud', url: ''
};

const K = {
  id: '0253dd4d-35af-43f5-a84c-7cc280840333',
  title: 'T-Systems Adpator', rating: 4.3, downloads: 543, tags: ['FREE FOR USE', 'UPLOADER', 'DATA UPLOAD', 'CONNECTOR', 'PRODUCTIVITY', 'IDS', 'MASTER DATA'],
  screenshots: ['/dataupload1.png', '/dataupload2.png', '/dataupload1.png', '/dataupload2.png'],
  description: '<b>Upload your data via standard connector</b><br/>Far far away, behind the word mountains, far from the countries Vokalia and Consonantia, there live the blind texts. Separated they live in Bookmarksgrove right at the coast of the Semantics, a large language ocean. A small river named Duden flows by their place and supplies it with the necessary regelialia. It is a paradisematic country, in which roasted parts of sentences fly into your mouth. <br/><br/><b>Easy to use and state of the art</b><br/>Even the all-powerful Pointing has no control about the blind texts it is an almost unorthographic life One day however a small line of blind text by the name of Lorem Ipsum decided to leave for the far World of Grammar. The Big Oxmox advised her not to do so, because there were thousands of bad Commas, wild  Question Marks and devious Semikoli, but the Little Blind Text didn’t listen.',
  usage: 'free for use',
  purchase: 'PURCHASE',
  companyName: 'T-Systems', url: ''
};

const L = {
  id: '0253dd4d-35af-43f5-a84c-7cc280840334',
  title: 'Data Mapping Service', rating: 3.8, downloads: 543, tags: ['FREE FOR USE', 'UPLOADER', 'DATA UPLOAD', 'CONNECTOR', 'PRODUCTIVITY', 'IDS', 'MASTER DATA'],
  screenshots: ['/dataupload1.png', '/dataupload2.png', '/dataupload1.png', '/dataupload2.png'],
  description: '<b>Upload your data via standard connector</b><br/>Far far away, behind the word mountains, far from the countries Vokalia and Consonantia, there live the blind texts. Separated they live in Bookmarksgrove right at the coast of the Semantics, a large language ocean. A small river named Duden flows by their place and supplies it with the necessary regelialia. It is a paradisematic country, in which roasted parts of sentences fly into your mouth. <br/><br/><b>Easy to use and state of the art</b><br/>Even the all-powerful Pointing has no control about the blind texts it is an almost unorthographic life One day however a small line of blind text by the name of Lorem Ipsum decided to leave for the far World of Grammar. The Big Oxmox advised her not to do so, because there were thousands of bad Commas, wild  Question Marks and devious Semikoli, but the Little Blind Text didn’t listen.',
  usage: 'free for use',
  purchase: 'PURCHASE',
  companyName: 'DMG MORI', url: ''
};

export class AppState {
  public static state: AppState;
  public apps: Application[] = [F, E, C, A, D, B];
  public topApps: Application[] = [B, D, C, F, E];
  public bizApps: Application[] = [C, E, F];
  public installedApps: Application[] = [A, B];
  public sapapps: Application[] = [D];
  public connectedApps: Application[] = [B, D];
  public addOns: Application[] = [G, H, I, J, K, L];
  @observable public isAdmin: boolean;
  public email = '';
  public readonly categories: any[] = [
    { text: 'Top 10 Downloads', apps: this.topApps },
    { text: 'Business Apps', apps: this.bizApps },
    { text: 'Add-Ons for Connectors', apps: this.addOns }];
  public readonly dashboardCategories: any[] = [{ text: 'Installed apps', apps: this.installedApps }];
  // { text: 'All apps', apps: this.apps }];

  constructor() {
    //const domain = adalContext.getDomain(adalContext.getUsername());
    const domain = UserService.getDomain();
    switch (domain) {
      case 'BMW':
      case 'Microsoft':
        B.url = 'https://ui.bmw.dev.catenax.partchain.dev/';
        break;
      case 'Tier1':
        B.url = 'https://ui.tier1.dev.catenax.partchain.dev/';
        break;
      case 'ZF':
        B.url = 'https://ui.zf.dev.catenax.partchain.dev';
        break;
      case 'Gris':
        B.url = 'https://ui.gris.dev.catenax.partchain.dev/';
        break;
      case 'Bilstein':
        B.url = 'https://ui.bilstein.dev.catenax.partchain.dev';
        break;
      case 'Daimler':
        B.url = '';
        D.url = 'https://mtoemmercedes-mt-business-approuter-lbn-mt.cfapps.eu10.hana.ondemand.com/cp.portal/site#Shell-home';
        break;
      default:
        B.url = '';
        break;
    }

    if (!B.url) {
      this.dashboardCategories[0].apps = [A, D];
      this.installedApps = [A, D];
    }
  }
}
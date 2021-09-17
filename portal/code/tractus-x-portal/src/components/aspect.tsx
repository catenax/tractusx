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

import { ThemeGenerator } from '@fluentui/react';
import * as React from 'react';
import { isJsxOpeningElement } from 'typescript';
import BackLink from "./navigation/backlink";

export default class Aspect extends React.Component<any, any> {

  headers = new Headers(
    {"Content-Type":"application/json",
     "Authorization":"Basic "+process.env.REACT_APP_CONNECTOR_AUTHENTICATION
    }
  );

  mounted = false
  catalog = "catenax-catalog"
  offer = ""
  representation = ""
  artifact = ""

  constructor(props) {
    super(props);

    let params=props.match.params;
    this.offer=params.offer;
    this.representation=params.representation;
    this.artifact=params.artifact;

    this.state = { value: 'App Connector Session '+JSON.stringify(params)};
      
    this.handleChange = this.handleChange.bind(this);

    this.findCatalog()
  }

  componentDidMount() { 
    this.mounted = true;
  }
  
  componentWillUnmount() {
     this.mounted = false;
  }

  handleChange(event) {
    this.setState({value: event.target.value});
  }

  /** console output */
  appendOutput(text) {
    console.log(text);
    if(this.mounted) {
      this.setState({value: `${text}\n${this.state.value}`});
    } else {
      this.state = {value: `${text}\n${this.state.value}`};
    }
  }

  /** performs basic get and returns an any promise*/
  performGet(url,continuation,pp=false) {
    
    const that=this;

    let requestOptions = {
      method: 'GET',
      headers: that.headers
      //, body: raw
    };

    that.appendOutput(">>>GET "+url);

    fetch(url, requestOptions)
    .then( answer => answer.text())
    .then( text => {
      if(!pp) {
        that.appendOutput("<<<GET "+text);
      }
      let result=JSON.parse(text);
      if(pp) {
        that.appendOutput("<<<GET "+JSON.stringify(result,null,2));
      } 
      setTimeout(function (){

        continuation(result);
      
      }, 1000);
    })
    .catch( error => console.log(error.toString()));
  }

  /** performs basic get and returns an any promise*/
  performPost(url,bdy,continuation) {
    
    const that=this;

    let text = JSON.stringify(bdy);

    let requestOptions = {
      method: 'POST',
      headers: that.headers,
      body: text
    };

    that.appendOutput(">>>POST "+url+ " "+text);

    fetch(url, requestOptions)
    .then( answer => answer.text())
    .then( text => {
      that.appendOutput("<<<POST "+text); 
      let result=JSON.parse(text);
      continuation(result);
    })
    .catch( error => console.log(error.toString()));
  }

  findCatalog() {
    const that = this;

    that.performGet(process.env.REACT_APP_BROKER_ENDPOINT+"/api/catalogs", function(catalogs) {
      let catalogues=catalogs._embedded.catalogs;
      for(let catalog of catalogues) {
        if(catalog.title === that.catalog) {
          let fullId=catalog._links.self.href;
          that.appendOutput("$$$CATALOG found "+that.catalog+" under id "+fullId);
          that.appendOutput("");
          return that.findOffer(fullId);
        } 
      }
      that.appendOutput("!!!CATALOG "+that.catalog+" was not found.");        
    });
  }

  findOffer(catalogUrl) {
    const that = this;

    that.performGet(catalogUrl+"/offers", function(offers) {
      let offerings=offers._embedded.resources;
      for(let offer of offerings) {
        if(offer.title === that.offer) {
          let fullId=offer._links.self.href;
          that.appendOutput("$$$OFFER found "+that.offer+" under id "+fullId);
          that.appendOutput("");
          let shortId=fullId.substring(fullId.lastIndexOf('/') + 1)
          return that.findRepresentation(shortId,fullId);
        } 
      }
      that.appendOutput("$$$OFFER "+that.offer+" was not found.");        
    });
  }

  findRepresentation(offerId,offerUrl) {
    const that = this;

    that.performGet(offerUrl+"/representations", function(reps) {
      let representations=reps._embedded.representations;
      for(let rep of representations) {
        if(rep.title === that.representation) {
          let fullId=rep._links.self.href;
          that.appendOutput("$$$REPRESENTATION "+that.representation+" under id "+fullId);
          that.appendOutput("");
          let shortId=fullId.substring(fullId.lastIndexOf('/') + 1)
          return that.findArtifact(offerUrl,shortId,fullId);
        } 
      }
      that.appendOutput("!!!REPRESENTATION "+that.representation+" was not found.");        
    });
  }

  findArtifact(offerId,repId,repUrl) {
    const that = this;

    that.performGet(repUrl+"/artifacts", function(arts) {
      let artifacts=arts._embedded.artifacts;
      for(let art of artifacts) {
        if(art.title === that.artifact) {
          let fullId=art._links.self.href;
          that.appendOutput("$$$ARTIFACT found "+that.artifact+" under id "+fullId);
          that.appendOutput("");
          let shortId=fullId.substring(fullId.lastIndexOf('/') + 1)
          return that.agreement(offerId,repId,shortId,fullId);
        } 
      }
      that.appendOutput("!!!ARTIFACT "+that.artifact+" was not found.");        
    });
  }

  agreement(offerId,repId,artifactId,artifactFullId) {

    const that = this;
    
    const raw = [
    {
      "@type": "ids:Permission",
      "ids:action": [],
      "ids:target": artifactFullId
    }];

    that.performPost(process.env.REACT_APP_CONNECTOR_ENDPOINT+"/api/ids/contract?recipient=http://localhost:8080/api/ids/data&resourceIds="+offerId+"&artifactIds="+artifactFullId+"&download=false", 
      raw, function(agreement) {
        var remoteAgreement = agreement.remoteId
        var fullId=agreement._links.self.href
        var shortId=fullId.substring(fullId.lastIndexOf('/') + 1)
        that.appendOutput("$$$AGREEMENT negotiated "+fullId+ "with remote "+remoteAgreement);
        that.appendOutput("");
        that.findLocalArtifact(remoteAgreement,shortId,fullId);
        // that.appendOutput("! Artifact "+that.artifact+" was not found.");        
    });
  }

  findLocalArtifact(remoteAgreement,agreementId,agreementUrl) {
    const that = this;

    that.performGet(agreementUrl+"/artifacts", function(arts) {
      let artifacts=arts._embedded.artifacts;
      for(let art of artifacts) {
        let fullId=art._links.self.href;
        that.appendOutput("$$$ARTIFACT negotiated registered under id "+fullId);
        that.appendOutput("");
        let shortId=fullId.substring(fullId.lastIndexOf('/') + 1)
        that.download(fullId,remoteAgreement);
      }
    });
  }

  download(artifactUrl,remoteAgreement) {
    const that = this;
    that.performGet(artifactUrl+"/data/**?download=true&agreementUri="+remoteAgreement, function(body) {
      let text = JSON.stringify(body);
      that.appendOutput("^^^ Got Result with "+text.length+" bytes.");
    },true)
  }


  render() {
    let backlink;
    if(this.props.history !== undefined) {
      backlink=<BackLink history={this.props.history} />;
    }
    return(
      <div className='h100pc df fdc p44'>
        <div className="df jcsb w100pc">
          {backlink}
        </div>
        <div className="p4 fg1 bgindustrial fgf2 fs12"><pre>{this.state.value}</pre></div>
      </div>
    );
  }
    
}

//
// Copyright (c) 2021 T-Systems International GmbH (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//


import * as React from 'react';
import BackLink from './navigation/backlink';
import { Dropdown, IDropdownOption, IDropdownStyles} from '@fluentui/react';
import  Ajv from "ajv-draft-04";

import material from './semantics/material.json';
import traceability from './semantics/traceability.json';

const ajv = new Ajv();

export default class Aspect extends React.Component<any, any> {

  headers = new Headers(
    {'Content-Type':'application/json',
     'Authorization':`Basic ${process.env.REACT_APP_CONNECTOR_AUTHENTICATION}`
    }
  );

  mounted = false
  catalog = 'catenax-catalog'

  constructor(props) {
    super(props);

    var query=props.location.search.substr(1);
    if(query==='') {
      query=props.location.pathname.substring(props.location.pathname.indexOf("?")+1);
    }
    query.split("&").forEach(function(part) {
      var item = part.split("=");
      props.match.params[item[0]] = decodeURIComponent(item[1]);
    });

    console.log(props);

    let params=props.match.params;

    this.state = { params:params, schema:null, value: `App Connector Session ${JSON.stringify(params)}`, status:'blue'};
      
    this.handleValueChange = this.handleValueChange.bind(this);
    this.onOfferChange = this.onOfferChange.bind(this);
    this.onRepresentationChange = this.onRepresentationChange.bind(this);
    this.onArtifactChange = this.onArtifactChange.bind(this);

    if(params.representation !== undefined) {
      this.loadSchema(params.representation);
    }
    this.checkStateChange();
  }

  checkStateChange() {
    if(this.state.params.offer !== undefined && this.state.params.representation !== undefined && this.state.params.artifact !== undefined) {
      this.findCatalog();
    } 
  }

  componentDidMount() { 
    this.mounted = true;
  }
  
  componentWillUnmount() {
     this.mounted = false;
  }

  handleValueChange(event) {
    this.setState({value: event.target.value});
  }

  onOfferChange(event,option) {
    let params=this.state.params;
    params['offer']=option.key;
    this.setState({params:params});
  }

  onRepresentationChange(event,option) {
    let params=this.state.params;
    params['representation']=option.key;
    this.setState({params:params});
    this.loadSchema(option.key);
  }

  loadSchema(aspect) {
    let that=this;
    if(aspect==='bom-aspect') {
      this.performGet(`${process.env.REACT_APP_SEMANTIC_SERVICE_LAYER_URL}/models/urn:bamm:com.catenaX:0.0.1%23Traceability/json-schema`,function(schema) {
        that.appendOutput(`$$$SCHEMA urn:bamm:com.catenaX:0.0.1#Traceability found ${schema}`);
        that.setSchema(traceability);
      });
    } else if(aspect==='material-aspect') {
      this.performGet(`${process.env.REACT_APP_SEMANTIC_SERVICE_LAYER_URL}/models/urn:bamm:com.catenaX:0.0.1%23Material/json-schema`,function(schema) {
        that.appendOutput(`$$$SCHEMA urn:bamm:com.catenaX:0.0.1#Material found ${schema}`);
        that.setSchema(material);
      });
    }
  }

  onArtifactChange(event,option) {
    let params=this.state.params;
    params['artifact']=option.key;
    this.setState({params:params});
    this.checkStateChange();
  }

  /** 
   * console output, maybe already called before the 
   * component is mounted and setState can be used at all
   * So we know what we are doing here and disable lint rule
   */
  appendOutput(text) {
    console.log(text);
    if(this.mounted) {
      this.setState({value: `${text}\n${this.state.value}`});
    } else {
      this.state = {params:this.state.params, schema:this.state.schema, value: `${text}\n${this.state.value}`, status:this.state.status}; // eslint-disable-line 
    }
  }

  setStatus(status) {
    if(this.mounted) {
      this.setState({status:status});
    } else {
      this.state = {params:this.state.params, schema:this.state.schema, value: this.state.value, status:status}; // eslint-disable-line 
    }
  }

  setSchema(schema) {
    if(this.mounted) {
      this.setState({schema:schema});
    } else {
      this.state = {params:this.state.params, schema:schema, value: this.state.value, status:this.state.status}; // eslint-disable-line 
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

    that.appendOutput(`>>>GET ${url}`);

    fetch(url, requestOptions)
    .then( answer => answer.text())
    .then( text => {
      if(!pp) {
        that.appendOutput(`<<<GET ${text}`);
      }
      let result=JSON.parse(text);
      if(pp) {
        that.appendOutput(`<<<GET ${JSON.stringify(result,null,2)}`);
      } 
      setTimeout(function (){

        continuation(result);
      
      }, 100);
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

    that.appendOutput(`>>>POST ${url} ${text}`);

    fetch(url, requestOptions)
    .then( answer => answer.text())
    .then( text => {
      that.appendOutput(`<<<POST ${text}`); 
      let result=JSON.parse(text);
      continuation(result);
    })
    .catch( error => console.log(error.toString()));
  }

  dereference(url,provider=true) {
    let apiPos=url.indexOf('/api')
    if(provider) {
      return `${process.env.REACT_APP_BROKER_ENDPOINT}${url.substring(apiPos)}`;
    } else {
      return `${process.env.REACT_APP_CONNECTOR_ENDPOINT}${url.substring(apiPos)}`;
    }
  }

  /** find catena-x catalog (and trigger find offering afterwards) */
  findCatalog() {
    const that = this;

    that.performGet(`${process.env.REACT_APP_BROKER_ENDPOINT}/api/catalogs`, function(catalogs) {
      let catalogues=catalogs._embedded.catalogs;
      for(let catalog of catalogues) {
        if(catalog.title === that.catalog) {
          let fullId=that.dereference(catalog._links.self.href);

          that.appendOutput(`$$$CATALOG found ${that.catalog} under id ${fullId}`);
          that.appendOutput('');
          return that.findOffer(fullId);
        } 
      }
      that.appendOutput(`!!!CATALOG ${that.catalog} was not found.`);        
    });
  }

  /** find the selected offer (and trigger finding the representation, afterwards) */
  findOffer(catalogUrl) {
    const that = this;

    that.performGet(`${catalogUrl}/offers`, function(offers) {
      let offerings=offers._embedded.resources;
      for(let offer of offerings) {
        if(offer.title === that.state.params.offer) {
          let fullOid=offer._links.self.href;
          let fullId=that.dereference(fullOid);
          that.appendOutput(`$$$OFFER found ${that.state.params.offer} under id ${fullId}`);
          that.appendOutput('');
          //let shortId=fullId.substring(fullId.lastIndexOf('/') + 1)
          return that.findRepresentation(fullId,fullOid);
        } 
      }
      that.appendOutput(`$$$OFFER ${that.state.params.offer} was not found.`);        
    });
  }

  /** find the selected representation (and trigger finding the artifact, afterwards) */
  findRepresentation(offerUrl,offerOUrl) {
    const that = this;

    that.performGet(`${offerUrl}/representations`, function(reps) {
      let representations=reps._embedded.representations;
      for(let rep of representations) {
        if(rep.title === that.state.params.representation) {
          let fullId=that.dereference(rep._links.self.href);
          that.appendOutput(`$$$REPRESENTATION ${that.state.params.representation} under id ${fullId}`);
          that.appendOutput('');
          //let shortId=fullId.substring(fullId.lastIndexOf('/') + 1)
          return that.findArtifact(offerOUrl,fullId);
        } 
      }
      that.appendOutput(`!!!REPRESENTATION ${that.state.params.representation} was not found.`);        
    });
  }

  /** find the selected artifact (and trigger creation of an agreement, afterwards) */
  findArtifact(offerId,repUrl) {
    const that = this;

    that.performGet(`${repUrl}/artifacts`, function(arts) {
      let artifacts=arts._embedded.artifacts;
      for(let art of artifacts) {
        if(art.title === that.state.params.artifact) {
          let fullOId=art._links.self.href;
          let fullId=that.dereference(fullOId);
          that.appendOutput(`$$$ARTIFACT found ${that.state.params.artifact} under id ${fullId}`);
          that.appendOutput('');
          //let shortId=fullId.substring(fullId.lastIndexOf('/') + 1)
          return that.agreement(offerId,fullOId);
        } 
      }
      that.appendOutput(`!!!ARTIFACT ${that.state.params.artifact} was not found.`);        
    });
  }

  /** create an agreement (and trigger finding the resulting local artifact, afterwards) */
  agreement(offerId,artifactFullId) {

    const that = this;
    
    const raw = [
    {
      '@type': 'ids:Permission',
      'ids:action': [],
      'ids:target': artifactFullId
    }];

    that.performPost(`${process.env.REACT_APP_CONNECTOR_ENDPOINT}/api/ids/contract?recipient=${process.env.REACT_APP_BROKER_ENDPOINT}/api/ids/data&resourceIds=${offerId}&artifactIds=${artifactFullId}&download=false`, 
      raw, function(agreement) {
        var remoteAgreement = agreement.remoteId
        var fullId=that.dereference(agreement._links.self.href,false)
        //var shortId=fullId.substring(fullId.lastIndexOf('/') + 1)
        that.appendOutput(`$$$AGREEMENT negotiated ${fullId} with remote ${remoteAgreement}`);
        that.appendOutput('');
        that.findLocalArtifact(remoteAgreement,fullId);
        // that.appendOutput('! Artifact '+that.artifact+' was not found.');        
    });
  }

  /** find an agreed artifact (and trigger downloading, afterwards) */
  findLocalArtifact(remoteAgreement,agreementUrl) {
    const that = this;

    that.performGet(`${agreementUrl}/artifacts`, function(arts) {
      if(arts._embedded !== undefined) {
        let artifacts=arts._embedded.artifacts;
        for(let art of artifacts) {
          let fullId=that.dereference(art._links.self.href,false);
          that.appendOutput(`$$$ARTIFACT negotiated registered under id ${fullId}`);
          that.appendOutput('');
          //let shortId=fullId.substring(fullId.lastIndexOf('/') + 1)
          that.download(fullId,remoteAgreement);
        }
     }
    });
  }

  /** download an agree artifact */
  download(artifactUrl,remoteAgreement) {
    const that = this;
    that.performGet(`${artifactUrl}/data/**?download=true&agreementUri=${remoteAgreement}`, function(body) {
      let text = JSON.stringify(body);
      that.appendOutput(`^^^ Got Result with ${text.length} bytes.`);
      if(that.state.schema  == null) {
        that.setStatus("orange");
      } else {
          try {
            const compiledSchema = ajv.compile(that.state.schema);
            const valid = compiledSchema(body);
            if(valid) {
              that.setStatus("green");
            } else {
              compiledSchema.errors.forEach( (error,index) => {
                console.log(error);
                that.appendOutput(`!!!VALIDATION PROBLEM ${error.instancePath} ${error.message} ${error.params}`);
              });
              that.setStatus("red");
            }
          } catch(e) {
            console.log(e);
            that.setStatus("orange");
          }
      }
    },true)
  }

  /** render the debugging view */
  render() {
    let backlink;
    if(this.props.history !== undefined) {
      backlink=<BackLink history={this.props.history} />;
    }
    const dropdownStyles: Partial<IDropdownStyles> = {
      dropdown: { width: 400, marginRight: 20 },
    };
    const availableOffers: IDropdownOption[] = [
      { key: 'offer-windchill', text: 'A sample PLM offering representing a backend system.' },
      { key: 'offer-tdm', text: 'Catena-X Test Data Sample.' }
    ];
    const availableRepresentations: IDropdownOption[] = [
      { key: 'material-aspect', text: 'Sample Material Aspect realised as JSON.' },
      { key: 'bom-aspect', text: 'Sample BOM Aspect realised as JSON.' }
    ];
    const availableArtifacts: IDropdownOption[] = [
      { key: 'material-brake', text: 'Sample Material-Transformed File Source.' },
      { key: 'bom-brake', text: 'Sample BOM-Transformed File Source.' },
      { key: 'material-vehicle', text: 'Relational SQL-Transformed Material Info.' },
      { key: 'bom-vehicle', text: 'Relational SQL-Transformed Traceability Info.' }
    ];
    let offer=this.state.params.offer
    let representation=this.state.params.representation
    let artifact=this.state.params.artifact
    let consoleClass='p4 fg1 bgindustrial fgf2 fs12'
    if(this.state.status==='red') {
      consoleClass='p4 fg1 bgred fgf2 fs12'
    } else if(this.state.status==='green') {
      consoleClass='p4 fg1 bggreen fgf2 fs12'
    }else if(this.state.status==='orange') {
      consoleClass='p4 fg1 bgsignificant fgf2 fs12'
    }
    return(
      <div className='h100pc df fdc p44'>
        <div className='df jcsb w100pc'>
          {backlink}
          <Dropdown placeholder='Filter'
            label='Offer'
            options={availableOffers}
            defaultSelectedKey={offer}
            styles={dropdownStyles}
            onChange={this.onOfferChange}
          />
          <Dropdown placeholder='Filter'
            label='Representation'
            options={availableRepresentations}
            defaultSelectedKey={representation}
            styles={dropdownStyles}
            onChange={this.onRepresentationChange}
          />
          <Dropdown placeholder='Filter'
            label='Artifact'
            options={availableArtifacts}
            defaultSelectedKey={artifact}
            styles={dropdownStyles}
            onChange={this.onArtifactChange}
          />
        </div>
        <div className={consoleClass}><pre>{this.state.value}</pre></div>
      </div>
    );
  }
    
}

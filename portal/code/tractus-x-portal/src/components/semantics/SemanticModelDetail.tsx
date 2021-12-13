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

import { useEffect, useState } from "react";
import BackLink from "../navigation/BackLink";
import { Icon, PrimaryButton, TextField } from "@fluentui/react";
import Loading from "../loading";
import { getModelById, getModelDiagram, getDocumentationUrl, getJsonSchemaUrl, getFileUrl, getOpenApiUrl, getExamplePayloadUrl } from "./data";
import ErrorMessage from "../ErrorMessage";
import DeleteModel from "./DeleteModel";
import './SemanticModelDetail.css';

const SemanticModelDetail = (props) => {
  const id = props.match.params.id;
  const [apiBaseUrl, setApiBaseUrl] = useState<string | null>('https://swagger.io/docs/specification/api-host-and-base-path/')
  const [model, setModel] = useState<any | null>(undefined);
  const [error, setError] = useState<any | null>(undefined);
  const [imageUrl, setImageUrl] = useState<string | null>(undefined);
  const [documentationUrl, setDocumentationUrl] = useState<string | null>(undefined);
  const [jsonSchemaUrl, setJsonSchemaUrl] = useState<string | null>(undefined);
  const [openApiUrl, setOpenApiUrl] = useState<string | null>(undefined);
  const [examplePayloadUrl, setExamplePayloadUrl] = useState<string | null>(undefined);
  const [isImageLoading, setIsImageLoading] = useState(true);
  const [fileUrl, setFileUrl] = useState<string | null>(undefined);
  const [payloadError, setPayloadError] = useState(null);

  useEffect(() => {
    getModelById(id)
      .then(model => setModel(model), error => setError(error.message));
    setImageUrl(getModelDiagram(id));
    setDocumentationUrl(getDocumentationUrl(id));
    setJsonSchemaUrl(getJsonSchemaUrl(id));
    setExamplePayloadUrl(getExamplePayloadUrl(id));
    setOpenApiUrl(getOpenApiUrl(id, apiBaseUrl));
    setFileUrl(getFileUrl(id));
  }, [id]);

  const diagramOnLoad = () => {
    setIsImageLoading(false);
  }

  const changeExamplePayloadUrl = (_, value) => {
    setPayloadError('');
    if(value === '') {
      setExamplePayloadUrl(getExamplePayloadUrl(id))
    } else {
      setExamplePayloadUrl(value);
    }
    
  }

  const onExamplePayloadClick = () => {
    fetch(examplePayloadUrl)
      .then(res => res.json())
      .then(
        (result) => {
          window.open(examplePayloadUrl);
        },
        (error) => {
          setPayloadError('Your URL is invalid.')
        }
      )
    }

  return(
    <div>
      {model ? <div className='df fdc h100pc p44'>
        <div className="df jcsb w100pc">
          <BackLink history={props.history} />
          <div className="df">
            <DeleteModel id={id} name={model.name} version={model.version}></DeleteModel>
          </div>
        </div>
        <h1 className="pb20 fs42">{model.name}</h1>
        <p className="fs18">Version: {model.version}</p>
        <p className="fs18">Aspect Model URN: {model.id}</p>
        <p className="fs18 mb20">Release Status: {model.status}</p>
        <div>
          <img src={imageUrl} className="w100pc mb30" onLoad={diagramOnLoad}></img>
          {isImageLoading && <Loading />}
        </div>
        <div className="df fwrap">
          <a className='detail-link' href={fileUrl} target="_blank">
            <Icon className='fgblack fs20 mt2 mr7' iconName='Installation' />
            <span>Download TTL</span>
          </a>
          <a className='detail-link' href={documentationUrl} target="_blank">
            <Icon className='fgblack fs20 mt2 mr7' iconName='ReportDocument' />
            <span>Documentation</span>
          </a>
          <a className='detail-link' href={jsonSchemaUrl} target="_blank">
            <Icon className='fgblack fs20 mt2 mr7' iconName='Code' />
            <span>Download JSON Schema</span>
          </a>
          <a className='detail-link'href={openApiUrl} target="_blank">
            <Icon className='fgblack fs20 mt2 mr7' iconName='DataManagementSettings' />
            <span>Open API</span>
          </a>
        </div>
        <div className="df aife">
          <TextField onChange={changeExamplePayloadUrl} errorMessage={payloadError} className="mr10 w50-40" label="Enter Example Payload Link" />
          <PrimaryButton onClick={onExamplePayloadClick} title="Get Example Payload JSON">
            Get JSON
          </PrimaryButton>
        </div>
        </div> :
        <div className="df h100pc p44 jcc">
          {error ? <ErrorMessage error={error}/> : <Loading />}
        </div>
      }
    </div>
  );
}

export default SemanticModelDetail;


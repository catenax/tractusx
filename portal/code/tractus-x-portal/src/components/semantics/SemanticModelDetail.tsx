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
import BackLink from "../navigation/backlink";
import { Icon } from "@fluentui/react";
import Loading from "../loading";
import { getModelById, getModelDiagram, getModelDocumentation } from "./data";
import ErrorMessage from "../ErrorMessage";
import DeleteModel from "./DeleteModel"

const SemanticModelDetail = (props) => {
  const id = props.match.params.id;
  const [model, setModel] = useState<any | null>(undefined);
  const [error, setError] = useState<any | null>(undefined);
  const [imageUrl, setImageUrl] = useState<string | null>(undefined);
  const [documentation, setDocumentation] = useState();
  const [isImageLoading, setIsImageLoading] = useState(true);

  useEffect(() => {
    getModelById(id)
      .then(model => setModel(model), error => setError(error.message));
    setImageUrl(getModelDiagram(id));
    getModelDocumentation(id).then(doc => { 
      setDocumentation(doc); console.log(doc)}, 
      error => console.log(error))
  }, [id])

  const diagramOnLoad = () => {
    setIsImageLoading(false);
  }

  return(
    
    <div className='df fdc h100pc p44'>
      {model ? <div className='df fdc'>
        <div className="df jcsb w100pc">
          <BackLink history={props.history} />
          <div className="df">
            <a className='fgblack fs15 fw600 tdn df mt10 mb20 aic cpointer' href={model.download} download>
              <Icon className='fgblack fs20 mt2 mr7' iconName='Installation' />
              Download TTL
            </a>
            <DeleteModel id={id} name={model.name}></DeleteModel>
          </div>
        </div>
        <h1 className="pb20 fs42">{model.name}</h1>
        <div>
          <img src={imageUrl} className="w100pc mb30" onLoad={diagramOnLoad}></img>
          {isImageLoading && <Loading />}
        </div>
        {documentation}
        </div> :
        <div className="h100pc df jcc">
          {error ? <ErrorMessage error={error}/> : <Loading />}
        </div>
      }
    </div>
  );
}

export default SemanticModelDetail;


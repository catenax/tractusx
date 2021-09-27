import DescriptionList from "../lists/descriptionlist"
import { DigitalTwin } from "./data"

export function DigitalTwinDetail(props){
  const newModel: DigitalTwin = {
    aspects: [
      {
        httpEndpoints: [
          {
            id: "string",
            method: "POST",
            url: "string"
          }
        ],
        id: "string",
        urn: "URNxskjdskd"
      }
    ],
    description: "string",
    id: "string",
    localIdentifiers: [
      {
        key: "string",
        value: "string"
      }
    ],
    manufacturer: "string"
  }

  return(
    <div className="p44">
      <div className='m5 p20 bgpanel flex40 br4 bsdatacatalog'>
        <h2 className='fs24 bold'>{newModel.id}</h2>
        <span className='fs14 pt8'>{newModel.description}</span>
        <div className='mt20 mb30'>
          <DescriptionList title="Manufacturer" description={newModel.manufacturer} />
          <h3 className='fs20 bold mt20 mb20'>Aspects</h3>
          {newModel.aspects.map(aspect => (
            <div className="mb15">
              <DescriptionList title="ID" description={aspect.id} />
              <DescriptionList title="Model Reference URN" description={aspect.urn}/>
              <h4 className="dib mt20 fs14">HTTP Endpoints</h4>
              {aspect.httpEndpoints.map(httpEp => (
                <div className="ml20 mt10">
                  <DescriptionList title="ID" description={httpEp.id}/>
                  <DescriptionList title="Method" description={httpEp.method}/>
                  <DescriptionList title="URL" description={httpEp.url}/>
                </div>
              ))}
            </div>
          ))}
          <h3 className="fs20 bold mt20 mb20">Local Identifiers</h3>
          {newModel.localIdentifiers.map(identifier => (
            <div>
              <DescriptionList title="Key" description={identifier.key}/>
              <DescriptionList title="Value" description={identifier.value}/>
            </div>
          ))}
        </div>
      </div>
    </div>
  )
}

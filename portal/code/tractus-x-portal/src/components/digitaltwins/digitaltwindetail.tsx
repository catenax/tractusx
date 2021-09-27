import { useEffect, useState } from "react"
import DescriptionList from "../lists/descriptionlist"
import Loading from "../loading";
import BackLink from "../navigation/backlink"
import { DigitalTwin, getTwinById } from "./data"

export function DigitalTwinDetail(props){
  const id = props.match.params.id;
  const [twin, setTwin] = useState<DigitalTwin | any>(null);
  const [error, setError] = useState(null);
  const placeholderTwin: DigitalTwin = {
    aspects: [
      {
        httpEndpoints: [
          {
            id: "id sddds",
            method: "POST, GET",
            url: "URLdjnsdnsd"
          }
        ],
        id: "string",
        urn: "URNxskjdskd"
      }
    ],
    description: "description text sdlajd lskdlad",
    id: "aspect id",
    localIdentifiers: [
      {
        key: "dadasda",
        value: "Some Value"
      }
    ],
    manufacturer: "BMW"
  }

  useEffect(() => {
    getTwinById(id).then(twin => { 
      twin ? setTwin(twin) : setTwin(placeholderTwin);},
      error => setError(error.message))
  }, [id])


  return(
    <div className="p44">
      <BackLink history={props.history} />
      {twin &&
        <div className='m5 p20 bgpanel flex40 br4 bsdatacatalog'>
          <h2 className='fs24 bold'>{twin.id}</h2>
          <span className='fs14 pt8'>{twin.description}</span>
          <div className='mt20 mb30'>
            <DescriptionList title="Manufacturer" description={twin.manufacturer} />
            <h3 className='fs20 bold mt20'>Aspects</h3>
            {twin.aspects.map(aspect => (
              <div key={aspect.id} className="mb15 mt15">
                <DescriptionList title="ID" description={aspect.id} />
                <DescriptionList title="Model Reference URN" description={aspect.urn}/>
                <h4 className="dib mt20 fs14">HTTP Endpoints</h4>
                {aspect.httpEndpoints.map(httpEp => (
                  <div key={httpEp.id} className="ml20 mt10">
                    <DescriptionList title="ID" description={httpEp.id}/>
                    <DescriptionList title="Method" description={httpEp.method}/>
                    <DescriptionList title="URL" description={httpEp.url}/>
                  </div>
                ))}
              </div>
            ))}
            <h3 className="fs20 bold mb20">Local Identifiers</h3>
            {twin.localIdentifiers.map(identifier => (
              <div>
                <DescriptionList title="Key" description={identifier.key}/>
                <DescriptionList title="Value" description={identifier.value}/>
              </div>
            ))}
          </div>
        </div>
      }
      {error ? <p>Error: {error}</p> : <Loading />}
    </div>
  )
}

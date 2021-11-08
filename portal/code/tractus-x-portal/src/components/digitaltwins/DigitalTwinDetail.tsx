import { useEffect, useState } from "react"
import { Link } from "react-router-dom";
import ErrorMessage from "../ErrorMessage";
import DescriptionList from "../lists/descriptionlist"
import Loading from "../loading";
import BackLink from "../navigation/BackLink"
import { DigitalTwin, getTwinById } from "./data"

export function DigitalTwinDetail(props){
  const id = props.match.params.id;
  const [twin, setTwin] = useState<DigitalTwin | any>(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    getTwinById(id).then(twin => { 
      setTwin(twin);},
      error => {
        setError(error.message);
      })
  }, [id])


  return(
    <div className="p44">
      <BackLink history={props.history} />
      {twin ?
        <div className='m5 p20 bgpanel flex40 br4 bsdatacatalog'>
          <h2 className='fs24 bold'>{twin.description}</h2>
          <div className='mt20 mb30'>
            <DescriptionList title="Manufacturer" description={twin.manufacturer} />
            <div className='mt20'>
            <h3 className="fs20 bold">Local Identifiers</h3>
            {twin.localIdentifiers.map(identifier => (
              <div className='mt20'>
                <DescriptionList title="Key" description={identifier.key}/>
                <DescriptionList title="Value" description={identifier.value}/>
              </div>
            ))}
            </div>
            <h3 className='fs20 bold mt20'>Aspects</h3>
            {twin.aspects.map( (aspect, index) => (
              <div key={aspect.id} className={`mb15 mt25 bbss fgtab pb20 ${index === (twin.aspects.length -1) ? '' : 'bbw1'}`}>
                <dl>
                  <h4 className='dib minw150 fs14'>Model Reference URN</h4>
                  <dd className='fs14 fg5a dib'>
                    <Link className="ml25" to={{
                      pathname: `/home/semanticmodel/${aspect.modelReference.urn.replace("#","%23")}`,
                      state: aspect.modelReference.urn
                    }}>{aspect.modelReference.urn}</Link>
                  </dd>
                </dl>
                <h4 className="dib mt20 fs14">Endpoints</h4>
                {aspect.httpEndpoints.map(httpEp => (
                  <div key={httpEp.id} className="ml25 mt10">
                    <dl>
                      <dt className='dib minw150 fs14 fggrey'>URL</dt>
                      <dd className='fs14 fg5a dib'>
                        <Link className="mr20" to={{
                          pathname: `/home/aspect/${httpEp.url}`
                        }}>Aspect IDS Connector URL</Link>
                      </dd>
                    </dl>
                    <DescriptionList title="Method" description={httpEp.method}/>
                  </div>
                ))}
              </div>
            ))}
          </div>
        </div> :
        <Loading />
      }
      {error && <ErrorMessage error={error} />}
    </div>
  )
}

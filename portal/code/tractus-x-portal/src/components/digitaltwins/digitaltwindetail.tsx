import { useEffect, useState } from "react"
import { Link } from "react-router-dom";
import ErrorMessage from "../ErrorMessag";
import DescriptionList from "../lists/descriptionlist"
import Loading from "../loading";
import BackLink from "../navigation/backlink"
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
          <h2 className='fs24 bold'>{twin.id}</h2>
          <span className='fs14 pt8'>{twin.description}</span>
          <div className='mt20 mb30'>
            <DescriptionList title="Manufacturer" description={twin.manufacturer} />
            <h3 className='fs20 bold mt20'>Aspects</h3>
            {twin.aspects.map(aspect => (
              <div key={aspect.id} className="mb15 mt15 bbw1 bbss fgtab pb20">
                <DescriptionList title="ID" description={aspect.id} />
                <dl>
                  <dt className='dib minw150 fs14 fggrey'>Model Reference URN</dt>
                  <dd className='fs14 fg5a dib'>
                    <Link className="mr20" to={{
                      pathname: `/home/semanticmodel/${aspect.modelReference.urn.replace("#","%23")}`,
                      state: aspect.modelReference.urn
                    }}>{aspect.modelReference.urn}</Link>
                  </dd>
                </dl>
                <h4 className="dib mt20 fs14">HTTP Endpoints</h4>
                {aspect.httpEndpoints.map(httpEp => (
                  <div key={httpEp.id} className="ml20 mt10">
                    <DescriptionList title="ID" description={httpEp.id}/>
                    <DescriptionList title="Method" description={httpEp.method}/>
                    <dl>
                      <dt className='dib minw150 fs14 fggrey'>URL</dt>
                      <dd className='fs14 fg5a dib'>
                        <Link className="mr20" to={{
                          pathname: `/home/aspect/${httpEp.url}`
                        }}>{httpEp.url}</Link>
                      </dd>
                    </dl>
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
        </div> :
        <Loading />
      }
      {error && <ErrorMessage error={error} />}
    </div>
  )
}

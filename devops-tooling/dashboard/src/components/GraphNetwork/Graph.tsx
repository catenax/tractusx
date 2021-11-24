import Force from './Force';
import { data } from './data'; 
import { useEffect } from 'react';

export default function Graph(props:object){

   useEffect(()=> {

    Force(data)

     },[]) 


    return (<div >  
        
            <div id="graphContainer">
            
            </div> 
        </div>)

}


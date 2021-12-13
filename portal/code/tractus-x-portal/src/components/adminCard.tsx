import * as React from 'react';
import { observer } from 'mobx-react';
import { Administration } from '../data/administration';
import { RouteComponentProps, withRouter } from 'react-router';
import { Link } from 'react-router-dom';

interface IProp extends RouteComponentProps {
    app: Administration
}

@observer
class AdminCard extends React.Component<IProp> {
    public render() {
        const a = this.props.app;
        return (
            <Link className="m5 p20 bgpanel flex20 br4 bsdatacatalog tdn h40pc" to={{
                pathname: ``
            }}>
                <h1 className='fs16 fg191 bold mb20 mt50pc minh40'>{a.title}</h1>
                <div className='fglgreen bold fs14'>{a.subtitle}</div>
            </Link>
        )
    }
}

export default withRouter(AdminCard);

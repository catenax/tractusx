import { Icon } from '@fluentui/react/lib/Icon';

export default function ErrorMessage(props) {
  return <p className="df colorgraphred">
    <Icon iconName="Error" className='mt4 mr15' />
    <span>{props.error}</span>
  </p>;
}

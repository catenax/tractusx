import { ContextualMenu, Icon } from "@fluentui/react";
import React from "react";
import { useState } from "react";
import './HelpContextMenu.css'

export default function ContextMenu(props){
  const linkRef = React.useRef(null);
  const [showMenu, setShowMenu] = useState(false);

  const showContextMenu = (e) => {
    e.preventDefault();
    setShowMenu(true);
  }

  return(
    <div className="df fdc">
      <a ref={linkRef} className='help-link' href="#" onClick={showContextMenu}>
        <Icon className='help-link__icon' iconName='Help' />
        <span className="help-link__text">Help</span>
      </a>
      <ContextualMenu
        items={props.menuItems}
        hidden={!showMenu}
        target={linkRef}
        onItemClick={() => setShowMenu(false)}
        onDismiss={() => setShowMenu(false)}
      />
    </div>
  )
}

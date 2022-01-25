import { Box } from "@mui/material"
import NavLink from "./NavLink"

export default function Navigation(){

  const navigationLinks  = [
    {
      id:1,
      name: 'Connector Landscape',
      path: '/dashboard'
    },
    {
      id:2,
      name: 'Tools',
      path: '/tools'
    },
    {
      id:3,
      name: 'Warnings',
      path: '/warnings'
    },
    {
      id:4,
      name: 'FAQ',
      path: '/faq'
    },
    {
      id:5,
      name: 'Configuration',
      path: '/configuration'
    }
  ]


  return (
    <Box sx={{ display: 'flex', justifyContent: 'center'}}>
      {navigationLinks.map(item =>
        <NavLink to={item.path} name={item.name} key={item.id} />
      )}
    </Box>
  )
}
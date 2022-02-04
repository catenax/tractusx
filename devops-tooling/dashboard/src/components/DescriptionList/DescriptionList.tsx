import { Link, Typography } from '@mui/material';
import theme from '../../Theme';

export default function DescriptionList({ topic, link = null, description = null }:
  {topic:string, link?:string|null, description?:string|null}) {
  return (
    <div style={{ marginBottom: theme.spacing(1) }}>
      <Typography component="span" sx={{ fontWeight: 'bold' }}>
        {topic}
        :
        {' '}
      </Typography>
      {link
        ? <Link href={link} target="_blank">{link}</Link>
        : <Typography component="span">{description}</Typography>}
    </div>
  );
}

import {
  Button, Grid, Link, Typography,
} from '@mui/material';
import Close from '@mui/icons-material/Close';
import theme from '../../Theme';
import DescriptionList from '../DescriptionList/DescriptionList';

export default function NodeSelfDescription({ onClose, item }) {
  return (
    <Grid
      item
      container
      direction="column"
      xs={3}
      sx={{ p: theme.spacing(2), border: '1px solid #000', position: 'relative' }}
      data-testid="self-description-id"
    >
      <Button
        color="secondary"
        onClick={() => onClose(null)}
        sx={{
          alignSelf: 'end', cursor: 'pointer', position: 'absolute', right: theme.spacing(1), top: theme.spacing(1),
        }}
      >
        <Close />
      </Button>
      <Typography variant="subtitle1" component="h3">Connector Details</Typography>
      <Typography variant="h5" component="h4" sx={{ mb: theme.spacing(2) }}>
        <Link href={item['@id']} target="_blank" color="black" underline="none">
          {item['ids:title'][0]['@value']}
        </Link>
      </Typography>
      <DescriptionList topic="Format" link={item['@context'].ids} />
      <DescriptionList topic="Type" description={item['@type']} />
      <DescriptionList topic="Maintainer" link={item['ids:maintainer']['@id']} />
      <DescriptionList topic="Curator" link={item['ids:curator']['@id']} />
      <DescriptionList topic="Version" description={item['ids:outboundModelVersion']} />
    </Grid>
  );
}

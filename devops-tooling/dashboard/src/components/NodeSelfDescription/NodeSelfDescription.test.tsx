
import { render, screen } from "@testing-library/react";
import NodeSelfDescription from "./NodeSelfDescription";
import data from '../../pages/Dashboard/sd-data.json';

const item = data.filter(item => item['@id'] === `https://w3id.org/idsa/autogen/baseConnector/1`);

describe("NetworkGraph rendering", () => {

  test('it renders connectors self description', () => {
    render(<NodeSelfDescription item={item[0]}/>);
    const selfDesc = screen.getByTestId('self-description-id');
    expect(selfDesc).toHaveTextContent('Format:');
    expect(selfDesc).toHaveTextContent(item[0]['ids:title'][0]['@value']);

    expect(selfDesc).toHaveTextContent('Type: ');
    expect(selfDesc).toHaveTextContent(item[0]['@type']);

    expect(selfDesc).toHaveTextContent('Maintainer: ');
    expect(selfDesc).toHaveTextContent(item[0]['ids:maintainer']["@id"]);

    expect(selfDesc).toHaveTextContent('Curator: ');
    expect(selfDesc).toHaveTextContent(item[0]['ids:curator']['@id']);

    expect(selfDesc).toHaveTextContent('Version: ');
    expect(selfDesc).toHaveTextContent(item[0]['ids:outboundModelVersion']);
  });
});

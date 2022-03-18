package net.catenax.semantics.framework.idconversion;

import net.catenax.semantics.framework.aas.model.IdentifierKeyValuePair;

import java.nio.ByteBuffer;
import java.util.UUID;
import java.util.stream.Collectors;

public class Pseudonymizer implements IdConversion {
    private static int hash2=Pseudonymizer.class.getCanonicalName().hashCode();
    private static int hash3=IdConversion.class.getCanonicalName().hashCode();

    @Override
    public ConversionOutput convert(final ConversionInput input) {
        ConversionOutput output= new ConversionOutput();
        output.setIdentifiers(input.getIdentifiers().stream().map( identifier -> {
            IdentifierKeyValuePair pseudonym=new IdentifierKeyValuePair();
            pseudonym.setKey(input.getTargetDomain());
            int hash1=identifier.getKey().hashCode();
            int hash4=identifier.getValue().hashCode();
            ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
            byte[] buffer=new byte[16];
            bb.putInt(hash1);
            bb.putInt(hash2);
            bb.putInt(hash3);
            bb.putInt(hash4);
            UUID pseudoUUID= UUID.nameUUIDFromBytes(bb.array());
            pseudonym.setValue(pseudoUUID.toString());
            return pseudonym;
        }).collect(Collectors.toList()));
        return output;
    }
}

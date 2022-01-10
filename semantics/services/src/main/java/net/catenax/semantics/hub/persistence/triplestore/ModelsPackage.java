/*
 * Copyright (c) 2022 Bosch Software Innovations GmbH. All rights reserved.
 */

package net.catenax.semantics.hub.persistence.triplestore;

import io.openmanufacturing.sds.aspectmodel.urn.AspectModelUrn;

public class ModelsPackage {
   private final String urn;

   public ModelsPackage( String urn ) {
      this.urn = urn;
   }

   public String getUrn() {
      return urn;
   }

   public static ModelsPackage from( AspectModelUrn aspectModelUrn ) {
      return new ModelsPackage( aspectModelUrn.getUrnPrefix() );
   }
}

/*
Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/
package net.catenax.semantics.framework.dsc.test;

import net.catenax.semantics.framework.IdsConnector;
import net.catenax.semantics.framework.dsc.DsConnector;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests setting up a default adapter wo special config
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { DscConfiguration.class })
class DscConfigurationTest {

	/**
	 * the actual test code is rather small ;-)
	 * @param context created spring context
	 */
	@Test
	void contextLoads(ApplicationContext context) {
		assertThat(context).isNotNull();
		IdsConnector connector=context.getBean(IdsConnector.class);
		assertThat(connector).isNotNull();
		assertThat(connector.getClass()).isEqualTo(DsConnector.class);
	}

}

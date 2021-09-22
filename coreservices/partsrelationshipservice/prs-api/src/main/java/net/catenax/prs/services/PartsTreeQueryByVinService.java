//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.prs.services;

import com.catenax.partsrelationshipservice.dtos.PartRelationshipsWithInfos;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.catenax.prs.configuration.PrsConfiguration;
import net.catenax.prs.entities.PartAttributeEntity;
import net.catenax.prs.entities.PartIdEntityPart;
import net.catenax.prs.entities.PartInformationKey;
import net.catenax.prs.repositories.PartAttributeRepository;
import net.catenax.prs.requests.PartsTreeByObjectIdRequest;
import net.catenax.prs.requests.PartsTreeByVinRequest;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service for retrieving parts tree by VIN.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PartsTreeQueryByVinService {
    private final PartsTreeQueryService queryService;
    /**
     * Repository for retrieving {@link PartAttributeEntity} data.
     */
    private final PartAttributeRepository attributeRepository;

    /**
     * Get a parts tree for a {@link PartsTreeByVinRequest}.
     *
     * @param request Request.
     * @return PartsTree with parts info.
     */
    public Optional<PartRelationshipsWithInfos> getPartsTree(final PartsTreeByVinRequest request) {

        // Find vehicle, i.e. part with attribute partTypeName="vehicle" and objectId=VIN
        final var searchFilter = Example.of(
                PartAttributeEntity.builder()
                        .key(
                                PartInformationKey.builder()
                                        .partId(
                                                PartIdEntityPart.builder()
                                                        .objectIDManufacturer(request.getVin())
                                                        .build()
                                        )
                                        .name(PrsConfiguration.PART_TYPE_NAME_ATTRIBUTE_NAME)
                                        .build())
                        .value(PrsConfiguration.VEHICLE_ATTRIBUTE_VALUE).build());
        final var vehicles = attributeRepository.findAll(searchFilter);
        switch (vehicles.size()) {
            case 0:
                return Optional.empty();
            case 1:
                final var vehicle = vehicles.get(0).getKey().getPartId();
                return queryService.getPartsTree(PartsTreeByObjectIdRequest.builder()
                        .oneIDManufacturer(vehicle.getOneIDManufacturer())
                        .objectIDManufacturer(vehicle.getObjectIDManufacturer())
                        .aspect(request.getAspect().orElse(null))
                        .depth(request.getDepth().orElse(null))
                        .build());
            default:
                return Optional.empty(); // FIXME what to do??
        }
    }
}

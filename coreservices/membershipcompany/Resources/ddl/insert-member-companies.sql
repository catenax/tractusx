INSERT INTO member_companies
SELECT * FROM json_to_recordset('[
  {
    "bpn": "CAXLZJVJEBYWYYZZ",
    "name": "Bayerische Motoren Werke Aktiengesellschaft",
    "parent": null,
    "roles": [
      "ACTIVE_PARTICIPANT"
    ]
  },
  {
    "bpn": "CAXLXCLMNDAAWEZZ",
    "name": "Daimler AG",
    "parent": null,
    "roles": [
      "ACTIVE_PARTICIPANT"
    ]
  },
  {
    "bpn": "CAXLTHAJNAHZXGZZ",
    "name": "ZF Friedrichshafen AG",
    "parent": null,
    "roles": [
      "ACTIVE_PARTICIPANT"
    ]
  },
  {
    "bpn": "CAXLXZZDURIFEUZZ",
    "name": "Robert Bosch GmbH",
    "parent": null,
    "roles": [
      "ACTIVE_PARTICIPANT"
    ]
  },
  {
    "bpn": "CAXLBRHHQAJAIOZZ",
    "name": "BASF",
    "parent": null,
    "roles": [
      "ACTIVE_PARTICIPANT"
    ]
  },
  {
    "bpn": "CAXLHNJURNRLPCZZ",
    "name": "Henkel",
    "parent": null,
    "roles": [
      "ACTIVE_PARTICIPANT"
    ]
  },
  {
    "bpn": "CAXLJXFARPBQZQZZ",
    "name": "Kaputt GmbH",
    "parent": null,
    "roles": [
      "ACTIVE_PARTICIPANT",
      "APP_PROVIDER"
    ]
  },
  {
    "bpn": "CAXLAPHGVJJFHZZZ",
    "name": "SAP AG",
    "parent": null,
    "roles": [
      "APP_PROVIDER"
    ]
  },
  {
    "bpn": "CAXLCPOZSGFCTJZZ",
    "name": "Microsoft",
    "parent": null,
    "roles": [
      "OPERATIONS_INFRASTRUCTURE_PROVIDER"
    ]
  },
  {
    "bpn": "CAXLNDDMHMMNCOZZ",
    "name": "T-Systems",
    "parent": null,
    "roles": [
      "OPERATIONS_INFRASTRUCTURE_PROVIDER"
    ]
  }
]') AS (
    bpn VARCHAR(200),
    name VARCHAR(200),
    parent VARCHAR(200),
    roles JSON
);

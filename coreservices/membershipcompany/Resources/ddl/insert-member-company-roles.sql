INSERT INTO member_company_roles
SELECT * FROM json_to_recordset('[
  { "rolename": "ACTIVE_PARTICIPANT" },
  { "rolename": "APP_PROVIDER" },
  { "rolename": "OPERATIONS_INFRASTRUCTURE_PROVIDER" },
  { "rolename": "CONSULTING" },
  { "rolename": "CLEARING_HOUSE" }
]') AS (
    rolename VARCHAR(200)
);

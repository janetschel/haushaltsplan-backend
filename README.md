# Haushaltsplan Backend
This backend provides the [Haushaltsplan](https://github.com/janetschel/haushaltsplan/releases/tag/v1.0.0) with an REST-interface to send requests to. 
It stores the data for the Haushaltsplan on a cloud-hosted MongoDb database fore easy access. This is the reason why some of the naming conventions seem so off, but they do make sense in the MongoDb environment.

### Summary of all REST resources
This is a summary of all the REST resources this backend provides with all the expected answers they will yield on a specific input

**Heads-Up**: **ALL** REST resource (except /healthcheck) require either an Auth-String or an Auth-Token header to accept the request. The authentication string and token are specified as environment variables (or if you run it locally in application-localhost.properties). The secret therefore do not get leaked.

## REST resources
### Healthcheck with /healthcheck
#### Request
- Request to: `/healthcheck`
- Request Method: `GET`
- Required headers: _none_
- Required body: _none_

#### Response

Response on success with status code `200 OK`

<img width="405" alt="Bildschirmfoto 2020-04-09 um 09 37 39" src="https://user-images.githubusercontent.com/46886724/78870021-c2ef0d80-7a45-11ea-97bc-6ab72e204027.png">

### Login User with /login
#### Request
- Request to: `/login`
- Request Method: `GET`
- Required headers: `Auth-String` 
set to a base64 string with a username and password of the user
- Required body: _none_

#### Responses
Response on success with status code `200 OK`

<img width="405" alt="Bildschirmfoto 2020-04-09 um 09 42 33" src="https://user-images.githubusercontent.com/46886724/78870460-72c47b00-7a46-11ea-9c89-71eb790045a6.png">

Response on failure with status code `403 FORBIDDEN` if the base64 string does not match any registered users

<img width="405" alt="Bildschirmfoto 2020-04-09 um 09 42 59" src="https://user-images.githubusercontent.com/46886724/78870494-81ab2d80-7a46-11ea-85dd-a3b9eefc2e80.png">

### Get AuthToken required for future requests with /getAuthToken
#### Request
- Request to: `/getAuthToken`
- Request Method: `GET`
- Required headers: `Auth-String` 
set to a base64 string with a username and password of the user
- Required body: _none_

#### Responses
Response on success with status code `200 OK`

<img width="405" alt="Bildschirmfoto 2020-04-09 um 09 47 51" src="https://user-images.githubusercontent.com/46886724/78870955-3c3b3000-7a47-11ea-8dcd-2a4c7061feb2.png">

Response on failure with status code `403 FORBIDDEN` if the base64 string does not match any registered users

<img width="405" alt="Bildschirmfoto 2020-04-09 um 09 57 27" src="https://user-images.githubusercontent.com/46886724/78871795-896bd180-7a48-11ea-82b0-af4a768e96c0.png">

### Get all tasks saved with /getDocuments
#### Request
- Request to: `/getDocuments`
- Request Method: `GET`
- Required headers: `Auth-Token` set to the authtoken
- Required body: _none_

#### Responses
Response on success with status code `200 OK`

<img width="405" alt="Bildschirmfoto 2020-04-09 um 09 51 01" src="https://user-images.githubusercontent.com/46886724/78871196-a18f2100-7a47-11ea-9601-127f26899b59.png">

Response on failure with status code `403 FORBIDDEN` if the authtoken is invalid

<img width="405" alt="Bildschirmfoto 2020-04-09 um 09 51 27" src="https://user-images.githubusercontent.com/46886724/78871236-b075d380-7a47-11ea-9a84-1a1484ce243a.png">

### Add a task to the collection with /addDocument
#### Request
- Request to: `/addDocument`
- Request Method: `POST`
- Required headers: `Auth-Token` set to the authtoken
- Required body: `Valid JSON of a new document` **without** an Id set

#### Responses
Response on success with status code `201 CREATED`

<img width="405" alt="Bildschirmfoto 2020-04-09 um 09 56 01" src="https://user-images.githubusercontent.com/46886724/78871666-532e5200-7a48-11ea-971b-f3a8839fa23f.png">

Response on failure with status code `409 CONFLICT` if the provided Id already exists

<img width="405" alt="Bildschirmfoto 2020-04-09 um 09 55 43" src="https://user-images.githubusercontent.com/46886724/78871640-47db2680-7a48-11ea-9589-14f8f3ed0d77.png">

Response on failure with status code `403 FORBIDDEN` if the authtoken is invalid
See `Response 403 FORBIDDEN`for `/getDocuments`

### Update an existing document with /updateDocument
#### Request
- Request to: `/updateDocument`
- Request Method: `PUT`
- Required headers: `Auth-Token` set to the authtoken
- Required body: `Valid JSON of a new document` **with** an Id set

#### Responses
Response on success with status code `200 OK`

<img width="405" alt="Bildschirmfoto 2020-04-09 um 10 31 39" src="https://user-images.githubusercontent.com/46886724/78874850-4eb86800-7a4d-11ea-8c6e-36766d9a5a38.png">

Response on failure/success with status code `202 ACCEPTED` if the provided Id does not exist -> this document gets created instead

<img width="405" alt="Bildschirmfoto 2020-04-09 um 10 32 18" src="https://user-images.githubusercontent.com/46886724/78874897-668fec00-7a4d-11ea-9c63-6c44558ec4a9.png">

Response on failure with status code `403 FORBIDDEN` if the authtoken is invalid
See `Response 403 FORBIDDEN`for `/getDocuments`

### Delete an existing document with /deleteDocument
#### Request
- Request to: `/deleteDocument?id=<id_to_delete>`
- Request Method: `DELETE`
- Required headers: `Auth-Token` set to the authtoken
- Required body: _none_

#### Responses
Response on success with status code `200 OK`

<img width="405" alt="Bildschirmfoto 2020-04-09 um 10 35 01" src="https://user-images.githubusercontent.com/46886724/78875182-cb4b4680-7a4d-11ea-8cd6-77b9983eaa8e.png">

Response on failure with status code `404 NOT_FOUND` if the provided Id does not exist

<img width="405" alt="Bildschirmfoto 2020-04-09 um 10 35 56" src="https://user-images.githubusercontent.com/46886724/78875260-e7e77e80-7a4d-11ea-8b5c-a2c50db8fc17.png">

Response on failure with status code `403 FORBIDDEN` if the authtoken is invalid
See `Response 403 FORBIDDEN`for `/getDocuments`

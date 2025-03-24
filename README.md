# tasky
`Tasky` is an AI powered web based task manager.

It leverages a ScalaJS & Laminar frontend to create a reactive UI.

## Building

`Tasky` utilises the SBT build framework for building all parts of the project.

### Frontend
- `$ sbt ~buildFrontend` - For development mode.
- `$ sbt buildFrontend` - For production mode (set env RELEASE=TRUE).

### Backend
- `$ sbt devBackend` - For development mode.
- `$ sbt runBackend` - For production mode.
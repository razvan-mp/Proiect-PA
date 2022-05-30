# RouteSeeker
RouteSeeker is an application created for managing the creation of jogging routes based on a custom length specified by a user, finding all cycles with such constraints.
It currently contains a map of two areas from Iasi (Parcul Expozitiei and Rond Dacia), each having a number of intersection mapped, ready for routes to be generated.
The main use of the app is to provide an easy to use platform for users to find the best curated routes for them to jog on.
## Server
The Server application provides a host for clients to connect to, allowing multiple clients to use the service at the same time.
## Client (RouteSeeker)
The Client application provides the map (made with ```JavaFX```) and a way for the user to input commands for the server to process, providing the aforementioned functionalities.
It has a map from the ```OpenStreetMap API``` and two predefined sections, accessed via the interactive menu with buttons.
On click, the view is moved to the respective section and a graph with all the possible paths is generated on the view.
On the length specifier input field, on each keystroke the drawing algorithm is called and cycles are calculated such that they respect the contraint given by the user.

## Technologies used
- JavaFX → for the map and view
- JPA → for DB communication
- JDBC → for DB communication
- Sockets → for Client-Server communication
- JavaDoc API → for creating project documentation

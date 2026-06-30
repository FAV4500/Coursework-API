MLOps Pipeline Management API — Technical Report

Student ID: W1947505

Part 1: Service Architecture & Setup
Question 1.1: Explain the role of a MessageBodyWriter or a JSON provider (like Jackson) in this conversion process.
A MessageBodyWriter is a core JAX-RS provider interface responsible for converting Java objects into a low-level byte stream suitable for network transmission over HTTP. When a resource method returns a custom Java object (such as an ML entity), the JAX-RS runtime matches the media type specified in the @Produces annotation with an appropriate implementation. Jackson acts as this concrete JSON provider. It reads the Java class structure, maps fields through reflection or getters, converts the object data into a structured JSON string, and updates the outbound HTTP Content-Type header to application/json.

Question 1.2: REST architecture dictates that APIs should be strictly 'stateless'. Define what statelessness means in this context and explain why it makes cloud APIs easier to scale horizontally across multiple servers.
Statelessness means that the server retains no conversational information or state context about the client between requests. Each inbound request must function as an isolated transaction containing all the metadata, parameter strings, and authentication keys needed to process it completely.

This design simplifies horizontal scaling across cloud infrastructures because requests can be dynamically routed to any server instance behind a load balancer. Since no instance is anchored to an in-memory session or local cache state, any machine can fulfill any request instantly, enabling effortless system replication and failure recovery.

Part 2: Workspace Management
Question 2.1: Discuss how implementing HTTP Cache-Control headers on the GET /workspaces endpoint could improve performance for the client and reduce unnecessary processing load on the server.
The Cache-Control header allows the server to establish explicit data-freshness instructions for downstream consumers and intermediary proxy gateways. By introducing directives such as Cache-Control: public, max-age=300, the server indicates that the workspace list remains reliable for 5 minutes.

For the client, this eliminates network latency because subsequent reads pull instantly from local browser memory. For the server, this removes repetitive database lookup routines and repetitive serialization cycles, freeing up critical hardware resources for write operations.

Question 2.2: If a client needs to verify whether a specific workspace exists but wants to save bandwidth by not downloading the entire JSON body, which HTTP method should they use instead of GET? Explain your reasoning.
The client should use the HEAD method. Semantically, a HEAD request behaves exactly like a standard GET request; it travels through identical endpoint paths, validates identifiers, and encounters identical error logic on the server. However, the server intercepts the response right before transmission and completely strips the entity body. This returns identical header configurations and HTTP status codes (e.g., 200 OK or 404 Not Found), allowing the client to verify resource existence without consuming excess network bandwidth.

Part 3: Model Operations & Linking
Question 3.1: When creating a new Model via a POST request, it is considered best practice for the server to generate the unique id (e.g., using UUID.randomUUID()) rather than allowing the client to pass an id in their JSON payload. Discuss the security and data integrity reasons behind this architectural choice.
Relying on client-supplied identifiers introduces substantial architectural and security risks. A malicious or poorly configured client could pass an identifier that already exists in memory, accidentally overwriting active production model tracking data.

From a security perspective, letting clients control IDs simplifies data enumeration and brute-force attacks. Forcing server-side generation via secure random keys ensures uniform naming standards, enforces record encapsulation, and eliminates primary key collision vectors entirely.

Question 3.2: If a user attempts to search for a framework containing spaces or special characters (e.g., ?framework=Scikit Learn & Tools), how must the client modify the URL, and why is this encoding necessary?
The client must apply Percent-Encoding (URL Encoding) to the query text, transforming characters like spaces into %20 and ampersands into %26 (resulting in ?framework=Scikit%20Learn%20%26%20Tools).

This modification is mandatory because the HTTP URI standard restricts URLs to a specific subset of safe US-ASCII characters. Special characters have structural meanings within the protocol; for instance, a raw space signals a separator between protocol elements, and an ampersand delineates query parameters. Failing to encode them results in parsing failures or corrupted state values on the backend container.

Part 4: Deep Nesting with Sub-Resources
Question 4.1: You can place annotations like @Produces(MediaType.APPLICATION_JSON) at either the class level or the individual method level. What is the benefit of class-level placement, and how does method-level overriding work?
Placing @Produces at the class level establishes a global fallback media type for every resource method inside that file, reducing boilerplate configuration code.

Method-level overriding happens when an individual method defines its own distinct @Produces format (such as text/plain or application/octet-stream). The JAX-RS routing engine always prioritizes the method-level metadata over the class-level inheritance rule, allowing specialized routes to return alternative payload shapes cleanly.

Part 5: Advanced Error Handling, Exception Mapping & Logging
Question 5.2: HTTP status codes are categorised into classes (e.g., 2xx, 4xx, 5xx). Explain fundamentally why a validation failure caused by the user providing a non-existent workspaceId must return a 4xx code rather than a 5xx code.
HTTP status code classes communicate error accountability. The 4xx class signifies a client-side error, indicating that the server successfully processed the communication but found that the client sent malformed payloads or invalid relational links (such as a non-existent workspaceId).

Conversely, 5xx codes represent server-side errors, indicating internal server crashes or hardware drops during a technically faultless request. Misrepresenting a user input validation error with a 5xx code creates false alarms in automated logging systems and obscures true infrastructure breakages.

Question 5.4: If an operation throws a specific custom exception (e.g., LinkedWorkspaceNotFoundException) and you also have a global ExceptionMapper<Throwable>, how does the JAX-RS runtime determine which mapper to execute?
The JAX-RS framework resolves competing exceptions using a strict inheritance proximity matching algorithm. When an application error event is thrown, the container queries all registered ExceptionMapper classes and calculates the class distance between the thrown exception and the type parameter defined in each mapper. JAX-RS will always bind the event to the mapper closest in the object inheritance hierarchy. Therefore, it executes the specific LinkedWorkspaceNotFoundException mapper rather than falling back to the generic Throwable global mapper.

Question 5.5: In your filter, you interact with ContainerRequestContext and ContainerResponseContext. List two pieces of crucial HTTP metadata (e.g., headers, URIs) you can extract from these contexts that are highly valuable for debugging server issues.
The Inbound HTTP Method and Absolute Request URI: Accessible via requestContext.getMethod() and requestContext.getUriInfo().getRequestUri(). This allows engineers to track the exact runtime route and query variables that initiated a processing failure.

Outbound Response Status Codes and Content Length Values: Extracted via responseContext.getStatus() and headers inside the response context. This provides immediate confirmation of whether an endpoint succeeded or caught a mapping error, alongside the raw size of the payload sent back to the user.

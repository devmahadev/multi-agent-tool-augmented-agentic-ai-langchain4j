# tool-augmented-agentic-ai-langchain4j
Multi-agent coordination with Tool-Augmentation built with LangChain4j in Java, enabling intelligent agents to interact with external tools like search engines and document retrievers. 
It integrates tool calling into language models to perform complex tasks, bridging natural language understanding with real-world actions.

**Gate Agent - LLM Router with Multi‑Agent Coordination**
Project implements a multi‑agent assistant for airport gate operations using Java 25 + Spring Boot + LangChain4j. 
It uses a RouterClassifier (LLM) to classify each user request into BOARDING, GATE_CHANGE, or SECURITY, then dispatches to the corresponding specialized agent. 
Each agent uses LangChain4j @Tool methods to fetch live/operational data via a GateOpsClient.

![Application-UI](/src/main/resources/Schiphol-Agent-App.jpg "Application Home Page")

### **Sequence and Activity Diagrams**
![Multi-Agents Happy Flow](/src/main/resources/PlantUMLDiagrams/images/RouterClassifier_Decision_Flow.png "Router Classifier Flow")
![Multi-Agents Component View](/src/main/resources/PlantUMLDiagrams/images/ComponentDiagram-Multi_Gate_Agents_Coordination.png "Component View")
**Is_12A_already_boarded ?**
![Multi-Agents Component View](/src/main/resources/PlantUMLDiagrams/images/Seat_Lookup__BOARDING____Is_12A_already_boarded.png "Seat Lookup Flow ")
**Move Flight **HV6734-2025-11-03** from **D24** to **D26** at 10:15, reason **OPS_STAFFING**.**
![Multi-Agents Component View](/src/main/resources/PlantUMLDiagrams/images/Confirm_Updated_Gate__GATE_CHANGE-1A.png "Update Gate Change ")
![Multi-Agents Component View](/src/main/resources/PlantUMLDiagrams/images/Confirm_Updated_Gate__GATE_CHANGE-1B.png "Confirm Gate Change  ")
**Key Components**
* GateAgentsController – Single HTTP entrypoint (POST /api/gate-agents/ask).
* GateAgentsOrchestrator – Calls RouterClassifier then dispatches to the selected agent.
* RouterClassifier – LLM returning "BOARDING" | "GATE_CHANGE" | "SECURITY".

**Agents**
*     BoardingAgent – Boarding progress, seat lookups, manifest snapshots.
*     GateChangeAgent – Current gate, change requests, effective times.
*     SecurityAgent – Security queue estimates, holds.

**Tools (@Tool)**
* BoardingToolsService – summarizeBoarding, summarizeManifestPage, seatStatusByFlightAndSeat.
* GateChangeToolsService – summarizeGateStatus, requestGateChange.
* SecurityToolsService – summarizeQueue.


**GateOpsClient**
* GateOpsClientLocal – In‑process (backed by LocalGateOpsStore).
* GateOpsClientRest – RestTemplate to local stubs or external Schiphol APIs.
* GateOpsClientRepository – JPA/pgvector (DB‑backed).


**Sequence of Operations**
See PlantUML diagrams in docs/diagrams:

* Component overview
* Simple gate status (GATE_CHANGE)
* Seat 12A lookup (BOARDING)
* Gate change + confirmation
* Security then boarding follow‑up
* Router decision flow

**Running Locally**
* Start app
* Start NextJs
* Test via Postman

Example Prompts (router ⇒ agent ⇒ tool)

**“Is 12A already boarded on HV6734-2025-11-03?”**
Router → BOARDING → Tool: seatStatusByFlightAndSeat
Answer: Seat 12A — checked-in ✓, boarded ✗.

**“Move HV6734-2025-11-03 from D24 to D26 at 10:15, reason OPS_STAFFING.”**
Router → GATE_CHANGE → Tool: requestGateChange
Answer: Gate change accepted: #GC-… D24→D26 eff 10:15 …
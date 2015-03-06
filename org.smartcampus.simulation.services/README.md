# Sensor simulation framework - Service

The service allows to launch some specific simulations remotely. It is mainly used to benchmark SmartCampus' middleware.

Two requests are available :
 - POST on **simulations/simulation**?simulationParams=jsonParams will launch a normal simulation
 - POST on **simulations/eventsimulation**?simulationParams=jsonParams will launch a simulation which only sends event to SmartCampus' middleware if data value changes

JsonParams is a json string which must have the following content :
 - **name**, string, the name of the sensors group (values for sensors named NAME_0, NAME_1 will be generated)
 - **sensors**, int, the number of sensors in the simulation
 - **ip**, string, the ip of SmartCampus' middleware to send values to
 - **duration**, long, the duration of the simulation in ms
 - **frequency**, long, the period at which values are generated and sent
 - **start**, long, the offset in ms at which to start the simulation after the request is received
 - **virtual**, boolean depicting whether we want the simulator to generate Doubles or Booleans
		if virtual, the simulator will send doubles
		if not, it will generate booleans 


 ## How to deploy

in the SimulationFramework file :
mvn clean package tomee:run

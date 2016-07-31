import sys
sys.path.append("lib/AgentAPI.jar")
from me.sciion.agent.api import API
state1 = API.loadState("states/TaskBar.xml")
if API.exists(state1):
	print "Found state ", state1
else:
	print state1, " not found."
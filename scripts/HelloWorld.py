import sys
sys.path.append("lib/AgentAPI.jar")
from me.sciion.agent.api import API
from me.sciion.agent.utils import Location
print API.getVersion()

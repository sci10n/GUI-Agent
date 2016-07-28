import sys
sys.path.append("lib/AgentAPI.jar")
from me.sciion.agent.api import API
print("Wating...")
API.waitForChange()
print("Done Wating...")
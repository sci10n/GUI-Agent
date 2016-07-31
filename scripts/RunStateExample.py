import sys
sys.path.append("lib/AgentAPI.jar")
from me.sciion.agent.api import API
run_state = API.loadState("states/Run.xml")
API.type("W-r")
API.waitForState(run_state)
API.type("notepad")
location = API.locate(run_state.getTemplate("ok"))
API.move(location)
API.click()
API.waitForChange()
API.type("Hello WorldS-1")
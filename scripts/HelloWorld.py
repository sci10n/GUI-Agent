import sys
sys.path.append("lib/AgentAPI.jar")
from me.sciion.agent.api import API
from me.sciion.agent.utils import Location

location1 = Location(0,0)
template1 = API.loadTemplate("assets/templates/template-chrome-icon-start-menu.png")
if API.exists(template1):
	location1 = API.locate(template1)
	API.move(location1)
	API.click()
	print(location1)
else:
	print("Template not found")

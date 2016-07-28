import sys
sys.path.append("lib/AgentAPI.jar")
from me.sciion.agent.api import API
template1 = API.loadTemplate("assets/templates/KeepassIcon.png")
if API.exists(template1):
	location1 = API.locate(template1)
	API.move(location1)
	API.click()
	API.click()
	template2 = API.loadTemplate("assets/templates/KeepassIconKeys.png")
	API.waitForTemplate(template2)
	API.move(API.locate(template2))
	template3 = API.loadTemplate("assets/templates/KeepassPWField.png")
	API.waitForTemplate(template3)
	API.move(API.locate(template3))
else:
	print("Template not found")
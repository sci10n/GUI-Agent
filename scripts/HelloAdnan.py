import sys
sys.path.append("lib/AgentAPI.jar")
from me.sciion.agent.api import API
template1 = API.loadTemplate("assets/templates/template-sublime.png")
API.move(API.locate(template1))
API.click()
API.type("C-aC-c")

template2 = API.loadTemplate("assets/templates/template-firefox.png")
API.move(API.locate(template2))
API.click()

template2 = API.loadTemplate("assets/templates/Template-Adnane.png")
API.move(API.locate(template3))

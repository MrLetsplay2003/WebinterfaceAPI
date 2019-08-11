import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceActionHandler;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceHandler;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceRequestEvent;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceResponse;

public class TestHandler implements WebinterfaceActionHandler {

	@WebinterfaceHandler(requestTarget = "webinterface", requestTypes = "lol")
	public WebinterfaceResponse lol(WebinterfaceRequestEvent event) {
		System.out.println(event.getRequestData());
		JSONObject res = new JSONObject();
		res.put("yourvalue", event.getRequestData());
		return WebinterfaceResponse.success(res);
	}
	
}

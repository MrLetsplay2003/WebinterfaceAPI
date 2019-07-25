import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceActionHandler;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceHandler;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceRequestEvent;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceResponse;

public class TestHandler implements WebinterfaceActionHandler {

	@WebinterfaceHandler(requestTarget = "webinterface", requestTypes = "lol")
	public WebinterfaceResponse lol(WebinterfaceRequestEvent event) {
		System.out.println(event.getRequestData());
		return WebinterfaceResponse.error("lol u gex");
	}
	
}

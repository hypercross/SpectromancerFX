package network;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javafx.scene.web.WebView;

public class LogUpdate {
	public String log;
	public String tag;
	
	public LogUpdate(){}
	
	public LogUpdate(String log)
	{
		this.log = log;
		this.tag = "p";
	}
	
	public LogUpdate(String tag, String log)
	{
		this.tag = tag;
		this.log = log;
	}

	public void apply(WebView webview_log) {
		Document doc = webview_log.getEngine().getDocument();
		
		Element line = doc.createElement(tag);
		line.setAttribute("style", "text-align:center;");
		line.appendChild(doc.createTextNode(log));
		
		doc.getLastChild().getLastChild().appendChild(line);
		webview_log.getEngine().reload();	
	}
}

package mataf.notes.agent.called;
import java.util.Vector;

import lotus.domino.Agent;
import lotus.domino.AgentBase;
import lotus.domino.AgentContext;
import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.Session;

public class JavaAgent extends AgentBase {
	public void NotesMain() {

		try {
			Session session = getSession();
			AgentContext agentContext = session.getAgentContext();
			Database db = agentContext.getCurrentDatabase();
			Agent ag1 = agentContext.getCurrentAgent();
			String paramid = ag1.getParameterDocID();
			System.out.println("paramid: " + paramid);
			Document doc = db.getDocumentByID(paramid);
			Vector v = doc.getItemValue("AgentList");
			String sAgList = (String) v.elementAt(0);
			System.out.println("sAgList: " + sAgList);
			String newValue = sAgList + " > " + ag1.getName();
			System.out.println("newValue: " + newValue);
			doc.replaceItemValue("AgentList", newValue);
			doc.save(true, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

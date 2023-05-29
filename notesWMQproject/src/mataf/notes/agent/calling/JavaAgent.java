package mataf.notes.agent.calling;

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
			Agent ag2 = db.getAgent("JavaAgentGetParameterDocID");
			Document doc = db.createDocument();
			// Document item "AgentList" will collect the names of agents called
			doc.replaceItemValue("AgentList", ag1.getName() + " performing agent.run(NoteID)");
			doc.save(true, true);
			String paramid = doc.getNoteID();
			ag2.runOnServer(paramid);
			// remove old doc object from db cache
			doc.recycle();
			// get updated document
			Document doc2 = db.getDocumentByID(paramid);
			Vector v = doc2.getItemValue("AgentList");
			String sAgList = (String) v.elementAt(0);
			System.out.println("Agent calling sequence: " + sAgList);
			// cleanup
			doc2.remove(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
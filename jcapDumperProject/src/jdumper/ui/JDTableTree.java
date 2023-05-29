package jdumper.ui;

import java.awt.BorderLayout;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import jdumper.JDPacketAnalyzerLoader;
import jdumper.analyzer.JDPacketAnalyzer;
import jpcap.packet.Packet;

class JDTableTree extends JComponent {
	JTree tree;
	DefaultMutableTreeNode root = new DefaultMutableTreeNode();
	JDPacketAnalyzer[] analyzers = JDPacketAnalyzerLoader.getAnalyzers();

	JDTableTree() {
		tree = new JTree(root);
		tree.setRootVisible(false);
		JScrollPane treeView = new JScrollPane(tree);
		setLayout(new BorderLayout());
		add(treeView, BorderLayout.CENTER);
	}

	void analyzePacket(Packet packet) {
		boolean[] isExpanded = new boolean[root.getChildCount()];
		for (int i = 0; i < root.getChildCount(); i++)
			isExpanded[i] = tree.isExpanded(new TreePath(
					((DefaultMutableTreeNode) root.getChildAt(i)).getPath()));
		root.removeAllChildren();
		DefaultMutableTreeNode node;
		for (int i = 0; i < analyzers.length; i++) {
			if (analyzers[i].isAnalyzable(packet)) {
				analyzers[i].analyze(packet);
				node = new DefaultMutableTreeNode(analyzers[i]
						.getProtocolName());
				root.add(node);
				String[] names = analyzers[i].getValueNames();
				Object[] values = analyzers[i].getValues();
				if (names == null)
					continue;
				for (int j = 0; j < names.length; j++) {
					if (values[j] instanceof Vector) {
						addNodes(node, names[j], (Vector) values[j]);
					} else if (values[j] != null) {
						addNode(node, names[j] + ": " + values[j]);
					}/*
					 * else{ addNode(node,names[j]+": Not available"); }
					 */
				}
			}
		}
		((DefaultTreeModel) tree.getModel()).nodeStructureChanged(root);
		for (int i = 0; i < Math.min(root.getChildCount(), isExpanded.length); i++)
			if (isExpanded[i])
				tree.expandPath(new TreePath(((DefaultMutableTreeNode) root
						.getChildAt(i)).getPath()));
	}

	private void addNode(DefaultMutableTreeNode node, String str) {
		node.add(new DefaultMutableTreeNode(str));
	}

	private void addNodes(DefaultMutableTreeNode node, String str, Vector v) {
		DefaultMutableTreeNode subnode = new DefaultMutableTreeNode(str);
		for (int i = 0; i < v.size(); i++)
			subnode.add(new DefaultMutableTreeNode(v.elementAt(i)));
		node.add(subnode);
	}

	private void setUserObject(TreeNode node, Object obj) {
		((DefaultMutableTreeNode) node).setUserObject(obj);
	}
}

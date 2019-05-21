package mycontroller.router;

import java.util.Comparator;

public class NodeComparator implements Comparator<Node> {

	@Override
	public int compare(Node arg0, Node arg1) {
		return arg0.dist - arg1.dist;
	}
}

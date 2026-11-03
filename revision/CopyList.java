/*
// Definition for a Node.
class Node {
    int val;
    Node next;
    Node random;

    public Node(int val) {
        this.val = val;
        this.next = null;
        this.random = null;
    }
}
*/

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.w3c.dom.Node;

class CopyList {
    public Node copyRandomList(Node head) {

        Node resultHead = new Node(head.val);
        Node node = resultHead;
        Queue<Integer> queue = new LinkedList<>();

        Map<Node, Node> newNodesMap = new HashMap<>();
        map.put(head, resultHead);

        queue.put(head);

        while (!queue.isEMpty()) {
            Node tempNode = queue.poll();
            Node tempNewNode = newNodesMap.get(tempNode);
            Node randomNode = tempNode.random;
            Node nextNode = tempNode.next;
            if (randomNode != null) {
                if (!newNodesMap.containsKey(randomNode)) {
                    Node tempRandomNode = new Node(randomNode.val);
                    newNodesMap.put(randomNode, tempRandomNode);
                    queue.put(randomNode);
                }
                tempNewNode.random = newNodesMap.get(randomNode);
            }
            if (nextNode != null) {
                if (!newNodesMap.containsKey(nextNode)) {
                    Node tempNextNode = new Node(nextNode.val);
                    newNodesMap.put(nextNode, tempNextNode);
                    queue.put(nextNode);
                }
                tempNewNode.next = newNodesMap.get(nextNode);

            }
        }

        return resultHead;
    }

}

package com.gongshw.idea.mar.gui.util;

import java.util.HashMap;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import lombok.Getter;

/**
 * @author gongshiwei@baidu.com
 */
public class HashNode {
    private HashMap<String, HashNode> nodes = new HashMap<>();
    @Getter
    private Object data;

    public void appendPath(String path, Object data) {
        if (!path.contains("/")) {
            HashNode leaf = new HashNode();
            leaf.data = data;
            nodes.put(path, leaf);
            return;
        }
        String nodeName = path.substring(0, path.indexOf("/"));
        String nextPath = path.substring(path.indexOf("/") + 1);
        HashNode nextNode = nodes.getOrDefault(nodeName, new HashNode());
        nextNode.appendPath(nextPath, data);
        nodes.put(nodeName, nextNode);
    }

    public MutableTreeNode toTreeNode(String name) {
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(name);
        if (nodes.isEmpty()) {
            rootNode.setUserObject(data);
            return rootNode;
        }
        if (nodes.size() == 1) {
            Map.Entry<String, HashNode> child = nodes.entrySet().iterator().next();
            String childName = child.getKey();
            HashNode childNode = child.getValue();
            if (!childNode.nodes.isEmpty()) {
                return childNode.toTreeNode(name + "/" + childName);
            }
        }
        nodes.forEach((nextName, nexNode) -> rootNode.add(nexNode.toTreeNode(nextName)));
        return rootNode;
    }
}

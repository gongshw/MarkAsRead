package com.gongshw.idea.mar.gui.util;

import static org.junit.Assert.assertEquals;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import org.junit.Test;

/**
 * @author gongshiwei@baidu.com
 */
public class HashNodeTest {

    @Test
    public void appendPath() {
        HashNode root = new HashNode();
        root.appendPath("a/b/c/d", "100/100");
        root.appendPath("a/b/c/e", "100/100");
        root.appendPath("a/b/f/g", "100/100");
        root.appendPath("a/h/f/g", "100/100");
        root.appendPath("h/k", "100/100");
        root.appendPath("j", "100/100");

        MutableTreeNode mutableTreeNode = root.toTreeNode("root");
        assertEquals(3, mutableTreeNode.getChildCount());
        printTreeNode(mutableTreeNode, 0);
    }

    void printTreeNode(TreeNode treeNode, int depth) {
        for (int i = 0; i < depth; i++) {
            System.out.print("\t");
        }
        System.out.println(treeNode.toString());
        for (int i = 0; i < treeNode.getChildCount(); i++) {
            printTreeNode(treeNode.getChildAt(i), depth + 1);
        }
    }
}

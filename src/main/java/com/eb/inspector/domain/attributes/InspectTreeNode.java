package com.eb.inspector.domain.attributes;

import lombok.Getter;
import lombok.Setter;

import javax.swing.tree.TreeNode;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Getter
@Setter
public class InspectTreeNode implements TreeNode {

    String name;
    InspectTreeNode parent;
    List<InspectTreeNode> children;
    Object object;
    InspectMapping mapping;

    @Override
    public String toString() {
        return name;
    }

    public InspectTreeNode(String name) {
        this.name = name;
        children = new ArrayList<InspectTreeNode>();
    }

    public InspectTreeNode(String name, Object object) {
        this.name = name;
        mapping = InspectAttributeProvider.getInspectMapping(object.getClass());
        children = new ArrayList<InspectTreeNode>();
        this.object = object;
    }

    @Override
    public TreeNode getChildAt(int childIndex) {
        return children.get(childIndex);
    }

    @Override
    public int getChildCount() {
        return children.size();
    }

    @Override
    public int getIndex(TreeNode node) {
        return children.indexOf(node);
    }

    @Override
    public boolean getAllowsChildren() {
        return true;
    }

    @Override
    public boolean isLeaf() {
        return children.size() == 0;
    }

    @Override
    public Enumeration<? extends TreeNode> children() {

        return new Enumeration<TreeNode>() {
            int index = -1;

            @Override
            public boolean hasMoreElements() {
                return index+1 < children.size();
            }

            @Override
            public TreeNode nextElement() {
                if (index + 1 < children.size()) {
                    index++;
                    return children.get(index);
                }
                return null;
            }
        };
    }

    public InspectMappingAttribute getAttribute(String toString) {
        List attributes = mapping.getAttributes();
        Stream<InspectMappingAttribute> stream = attributes.stream();
        Optional<InspectMappingAttribute> optRes = stream.filter(x -> x.getName().equals(toString)).findFirst();
        if (optRes.isPresent()) {
            return optRes.get();
        }
        return null;
    }

    public Object getAttributeValue(String toString, Object object) {
        InspectMappingAttribute attrib = getAttribute(toString);
        if (attrib != null) {
            return attrib.getValueProvider().apply(object);
        }
        return null;
    }
}

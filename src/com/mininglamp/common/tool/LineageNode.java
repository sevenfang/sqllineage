package com.mininglamp.common.tool;

import java.util.ArrayList;
import java.util.List;

public class LineageNode {

    private LNode parent;
    private List<LNode> tabs = new ArrayList<>();
    private String filePath;

    public LNode getParent() {
        return parent;
    }

    public void setParent(LNode parent) {
        this.parent = parent;
    }

    public List<LNode> getTabs() {
        return tabs;
    }

    public void setTabs(List<LNode> tabs) {
        this.tabs = tabs;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void addChild(LNode child) {
        this.tabs.add(child);
    }
}

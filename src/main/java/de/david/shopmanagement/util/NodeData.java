package de.david.shopmanagement.util;

/**
 * @author Marvin
 */
public class NodeData {
    private long nodeId;
    private String nodeName;
    private String nodeDescription;
    private Double nodePrice = null;

    public long getNodeId() {
        return nodeId;
    }

    public void setNodeId(long nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeDescription() {
        return nodeDescription;
    }

    public void setNodeDescription(String nodeDescription) {
        this.nodeDescription = nodeDescription;
    }

    public Double getNodePrice() {
        return nodePrice;
    }

    public void setNodePrice(Double nodePrice) {
        this.nodePrice = nodePrice;
    }

    public boolean setNodePrice(String nodePrice) {
        boolean ret = false;
        try {
            this.nodePrice = new Double(nodePrice);
            ret = true;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return ret;
    }
}

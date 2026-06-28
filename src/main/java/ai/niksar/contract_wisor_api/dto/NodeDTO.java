package ai.niksar.contract_wisor_api.dto;

import java.util.*;

public class NodeDTO {
    private UUID id;
    private String name;
    private String nodeType;
    private UUID parentId;
    private Object data;
    private List<NodeDTO> children = new ArrayList<>();


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }

    public List<NodeDTO> getChildren() {
        return children;
    }

    public void setChildren(List<NodeDTO> children) {
        this.children = children;
    }


    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

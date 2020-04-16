package de.simpleprojectmanager.simpleprojectmanager.group;

public class Group {

    private final int id;
    private String name;
    private int permissions,color;
    private int hierarchy;

    public Group(int id, String name, int permissions, int color,int hierarchy) {
        this.id = id;
        this.name = name;
        this.permissions = permissions;
        this.color = color;
        this.hierarchy=hierarchy;
    }

    public int getHierarchy() {
        return hierarchy;
    }

    public void setHierarchy(int hierarchy) {
        this.hierarchy = hierarchy;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPermissions() {
        return permissions;
    }

    public void setPermissions(int permissions) {
        this.permissions = permissions;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}

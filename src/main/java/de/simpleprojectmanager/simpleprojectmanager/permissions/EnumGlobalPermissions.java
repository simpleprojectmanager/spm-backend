package de.simpleprojectmanager.simpleprojectmanager.permissions;

public enum EnumGlobalPermissions {

    CREATE_PROJECT(1,"project.create"),
    DELETE_PROJECT(2,"project.delete"),
    EDIT_PROJECT(3,"project.edit"),
    CREATE_USER(4,"user.create"),
    DELETE_USER(5,"user.delete"),
    EDIT_USER(6,"user.delete"),
    MODIFY_SETTINGS(7,"settings.modify");

    private String key;
    private int id;

    private EnumGlobalPermissions(int id,String key){
        this.id=id;
        this.key=key;
    }

    /**
     * @return the permissionNumber with all permissions active
     */
    public static int getAllPermissions(){
        return (int) Math.pow(2,EnumGlobalPermissions.values().length)-1;
    }

    /**
     * @param permissionNumber the permission-number
     * @return Returns if the given permissionNumber has the permission
     */
    public boolean hasPermission(int permissionNumber){
        return ((permissionNumber>>this.id-1)&1)==1;
    }

    /**
     * @param num the old permission number
     * @param hasPermission if the permission should be set
     * @return the new calculated number
     */
    public int setPermission(int num,boolean hasPermission){
        int id = this.getId();

        //Calculates the bits behind the change bit
        int back = num-((num>>(id-1))<<(id-1));

        //Checks if the bit should be set
        if(hasPermission)
            //Set 1
            return back | ((num>>(id-1))|1)<<(id-1);
        else
            //Set 0
            return back | ( ((num>>id)<<id) );
    }

    public String getKey() {
        return key;
    }

    public int getId() {
        return id;
    }
}

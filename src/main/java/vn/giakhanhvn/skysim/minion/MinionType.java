package vn.giakhanhvn.skysim.minion;

import vn.giakhanhvn.skysim.minion.types.CoalMinion;

public enum MinionType {

    COAL(CoalMinion.class);


    private final Class<?> clazz;

    MinionType(Class<?> clazz){
        this.clazz = clazz;
    }

    public Object getGenericInstance() {
        try {
            return this.clazz.newInstance();
        } catch (final InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }




}

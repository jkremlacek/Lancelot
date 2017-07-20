package cz.mzk.osdd.lancelot.utils;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Jakub Kremlacek
 */
public class Mets {
    public String mods;
    public String oaiDc;
    public List<String> connectedObjects = new LinkedList<>();
    public List<String> files = new LinkedList<>();

    public Boolean isComplete() {
        return mods != null && oaiDc != null;
    }
}

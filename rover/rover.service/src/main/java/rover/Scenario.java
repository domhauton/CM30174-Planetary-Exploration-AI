package rover;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

/**
 * Created by suegy on 11/08/15.
 */
@Root
public class Scenario {
    @Element(name="width")
    private int width = 0;
    @Element(name="height")
    private int height = 0;

    @Element(name="resources")
    private int rscount = 0;

    @Element(name ="resourceDistribution")
    private int rsDist = 0;

    @Attribute(name="id")
    private int id = 0;

    @Element(name="energy")
    private int initialEnergy = 0;

    @Element(name="isCompetitive")
    private boolean isCompetitive = false;

    @Element(name="resourceTypes", required=false)
    private int resourceTypes = 1;

    @Element(name="resourceTypeDist",required=false)
    private int[] resourceTypeDist;

    public static Scenario Empty(){
        return new Scenario(0,0,0,0,0,200,false);
    }

    public Scenario(@Attribute(name="id") int id,@Element(name="width") int width,@Element(name="height") int height,
                    @Element(name="resources") int resources,@Element(name="resourceDistribution") int resourceDist, @Element(name="energy") int energy,@Element(name="isCompetitive") boolean competitive, @Element(name="resourceTypes") int resourceTypes, @Element(name="resourceTypeDist") int[] resourceTypeDist) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.rscount  =resources;
        this.rsDist = resourceDist;
        this.initialEnergy = energy;
        this.isCompetitive = competitive;
        this.resourceTypes = resourceTypes;
        this.resourceTypeDist = resourceTypeDist;
    }

    public Scenario(@Attribute(name="id") int id,@Element(name="width") int width,@Element(name="height") int height,
                    @Element(name="resources") int resources,@Element(name="resourceDistribution") int resourceDist, @Element(name="energy") int energy,@Element(name="isCompetitive") boolean competitive) {
        this(id, width, height, resources, resourceDist, energy, competitive, 1,new int[]{1});
    }

    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }
    public int getResourceCount(){
        return rscount;
    }
    public int getResourceDistribution(){
        return rsDist;
    }
    public int getEnergy(){
        return initialEnergy;
    }
 
    public int getResourceTypes() {
	return resourceTypes;
    }

    public int[] getResourceTypeDist() {
	return resourceTypeDist;
    }

    public boolean isCompetitive(){
        return isCompetitive;
    }

    public int getId() {
        return id;
    }
}

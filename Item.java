
/**
 * This class is a part of the Zuul text based adventure game. Instances of this class are items
 * in various rooms of the Zuul World. 
 * 
 * @author Sahil Sharma
 * @version October 20, 2017
 */
public class Item
{
    private String description; 
    private double weight; 

    /**
     * Constructor for objects of class Item
     * contructs an item with properties of the parameters being passed in. 
     * 
     * 
     * @param description The properties of the item 
     * @param weight The weight of the item. 
     */
    public Item(String description, double weight)
    {
        this.description = description; 
        this.weight = weight; 
    }
    
    /**
     * creates a formatted string with the items description and weight. 
     *
     * @return returns a String containing all attributes of the item. 
     */
    
    public String getRepresentation(){
        return description + " " + weight + "g"; 
    }
    
}

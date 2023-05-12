package org.example;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

abstract class Category {
    //Create the abstract class Category to storage all types of categories, including parents and children
    protected String name;
    protected Category parent;

    protected List<String> keywords;

    public Category(String name, Category parent, List<String> keywords) {
        this.name = name;
        this.parent = parent;
        this.keywords = keywords;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public abstract int getLevel();

    public abstract List<String> getAllKeywords();

}


 class ContainerCategory extends Category{
    //Create a container category to handle the logic of children to make easier to assign the categories with his parents
    private List<Category> children;

    public ContainerCategory(String name, Category parent, List<String> keywords) {
        super(name, parent, keywords);
        this.children = new ArrayList<>();
    }

    public void addChild(Category child){
        this.children.add(child);
    }

    public void remove(Category child){
        this.children.remove(child);
    }

    public List<Category> getChildren(){
        return children;
    }

    //Create a get level method to recursively calculate the value of a level depending of how many parents does he has
    @Override
    public int getLevel() {
        if(parent == null){
            return 0;
        }
        return parent.getLevel() + 1;
    }

    //Create a getAllKeywords method to return all the keywords including the parents keywords
    @Override
    public List<String> getAllKeywords() {
    List<String> allKeywords = new ArrayList<>(keywords);
    if(parent != null){
        allKeywords.addAll(parent.getAllKeywords());
    }
    return allKeywords;
    }
}



// Create a principal Class SquareTrade for the logic
public class SquareTrade {

    //Create a categorymap to map the categories with an Id and perform operations
    private static Map<Integer,Category> categoryMap= new HashMap<>();

    //create a static block of code to declare the data of the categories
    static{
        //We are going to start declaring the root, who has no parent and empty keywords
        Category root = new ContainerCategory("Root", null, Collections.emptyList());
        //Then we are declaring the other elementes of the categories, including the parents in the creation of the object and the keywords
        Category furniture = new ContainerCategory("Furniture", root, Arrays.asList("Furniture"));
        Category electronics = new ContainerCategory("Electronics",root,Arrays.asList("Electronics"));
        Category homeAppliances = new ContainerCategory("Home Appliances",root,Arrays.asList("Home Appliances"));

        Category majorAppliances = new ContainerCategory("Major Appliances",homeAppliances,Arrays.asList("Major Appliances"));
        Category minorAppliances = new ContainerCategory("Minor Appliances",homeAppliances,Arrays.asList("Minor Appliances"));
        Category lawnGarden = new ContainerCategory("Lawn Garden",homeAppliances,Arrays.asList("Lawn Garden"));

        Category kitchenAppliances = new ContainerCategory("Kitchen Appliances",majorAppliances,Arrays.asList("Kitchen Appliances"));
        Category generalAppliances = new ContainerCategory("General Appliances",majorAppliances,Arrays.asList("General Appliances"));

        //Assign the categories to the map
        ((ContainerCategory) root).addChild(furniture);
        ((ContainerCategory) root).addChild(electronics);
        ((ContainerCategory) root).addChild(homeAppliances);

        ((ContainerCategory) homeAppliances).addChild(majorAppliances);
        ((ContainerCategory) homeAppliances).addChild(minorAppliances);
        ((ContainerCategory) homeAppliances).addChild(lawnGarden);


        ((ContainerCategory) majorAppliances).addChild(kitchenAppliances);
        ((ContainerCategory) majorAppliances).addChild(generalAppliances);

        categoryMap.put(1,root);
        categoryMap.put(2,furniture);
        categoryMap.put(3,electronics);
        categoryMap.put(4,homeAppliances);
        categoryMap.put(5,majorAppliances);
        categoryMap.put(6,minorAppliances);
        categoryMap.put(7,lawnGarden);
        categoryMap.put(8,kitchenAppliances);
        categoryMap.put(9,generalAppliances);


    }

    //We create a searchKeywords Method to prepare the output, this will include the level of the category and all the keywords, it is not necessary
    //to make a loop because each method do that
    public static String [] SearchKeywords(int categoryId){
        Category category = categoryMap.get(categoryId);
        if(category==null){
            return new String[0];
        }
        List<String> result = new ArrayList<>();
        result.add(String.valueOf(category.getLevel()));
        result.addAll(category.getAllKeywords());

        return result.toArray(new String[0]);

    }
// Main program to create the output text
    public static void main(String[] args) {
        String[] result = SearchKeywords(9);
        //System.out.println(Arrays.toString(result));
        String formattedResult = "{"+String.join(",",result)+"}";
        System.out.println(formattedResult);
    }

    //Test the main functionality to compare generalAppliances (id 9) with a valid result
    @Test
    public void testSquareTrade(){
    String[] expected = {"3","General Appliances","Major Appliances","Home Appliances"};
    String[] result = SquareTrade.SearchKeywords(9);
    assertArrayEquals(expected,result);
    }

    // Test a failure case with an inexistent Id
    @Test
    public void testFailSquareTrade(){
        String[] expected = {};
        String[] result = SquareTrade.SearchKeywords(100);
        assertArrayEquals(expected,result);
    }

}
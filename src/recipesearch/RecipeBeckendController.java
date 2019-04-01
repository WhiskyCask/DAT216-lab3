package recipesearch;

import se.chalmers.ait.dat215.lab2.Recipe;
import se.chalmers.ait.dat215.lab2.RecipeDatabase;
import se.chalmers.ait.dat215.lab2.SearchFilter;

import java.util.List;

public class RecipeBeckendController {

    private RecipeDatabase db = RecipeDatabase.getSharedInstance();
    private String cuisine;
    private String mainIngredient;
    private String difficulty;
    private int maxPrice;
    private int maxTime;

    public List<Recipe> getRecipes(){
        return db.search(new SearchFilter(difficulty,maxTime,cuisine,maxPrice,mainIngredient));
    }

    public void setCuisine(String cuisine){
        this.cuisine = cuisine;
    }

    public void setMainIngredient(String mainIngredient){
        this.mainIngredient = mainIngredient;
    }

    public void setDifficulty(String difficulty){
        this.difficulty = difficulty;
    }

    public void setMaxPrice(int maxPrice){
        this.maxPrice = maxPrice;
    }

    public void setMaxTime(int maxTime){
        this.maxTime = maxTime;
    }


}

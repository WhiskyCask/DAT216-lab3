package recipesearch;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import se.chalmers.ait.dat215.lab2.Recipe;

import java.io.IOException;

public class RecipeListItem extends AnchorPane {

    private RecipeSearchController parentController;
    private Recipe recipe;

    @FXML private Label nameLabel;
    @FXML private Label timeLabel;
    @FXML private Label priceLabel;
    @FXML private ImageView imgItemList;
    @FXML private Label descriptionItemList;
    @FXML private ImageView imgCuisine;
    @FXML private ImageView imgMainIngredient;
    @FXML private ImageView imgDifficulty;

    public RecipeListItem(Recipe recipe, RecipeSearchController recipeSearchController){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("recipe_listitem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.recipe = recipe;
        this.parentController = recipeSearchController;

        imgItemList.setImage(recipe.getFXImage());
        descriptionItemList.setText(recipe.getDescription());

        imgCuisine.setImage(parentController.getCuisineImage(recipe.getCuisine()));
        imgMainIngredient.setImage(parentController.getMainIngredientImage(recipe.getMainIngredient()));
        imgDifficulty.setImage(parentController.getDifficultyImage(recipe.getDifficulty()));

        nameLabel.setText(recipe.getName());
        priceLabel.setText(Integer.toString(recipe.getPrice()));
        timeLabel.setText(Integer.toString(recipe.getTime()));

    }

    @FXML
    protected void onClick(Event event) {
        parentController.openRecipeView(recipe);
    }
}

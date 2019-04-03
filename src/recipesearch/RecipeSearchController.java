
package recipesearch;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.layout.FlowPane;
import se.chalmers.ait.dat215.lab2.Recipe;
import se.chalmers.ait.dat215.lab2.RecipeDatabase;


public class RecipeSearchController implements Initializable {


    private RecipeBackendController rbc;

    @FXML private ComboBox mainIngredientComboBox;
    @FXML private ComboBox cuisineComboBox;
    @FXML private RadioButton radioButtonAll;
    @FXML private RadioButton radioButtonEasy;
    @FXML private RadioButton radioButtonMedium;
    @FXML private RadioButton radioButtonHard;
    @FXML private Spinner maxPriceSpinner;
    @FXML private Slider maxTimeSlider;
    @FXML private FlowPane displayRecipe;


    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    private void updateRecipeList() {
        displayRecipe.getChildren().clear();

        RecipeListItem recipeListItem;

        for (Recipe recipe : rbc.getRecipes()) {

            recipeListItem = new RecipeListItem(recipe, this);

            displayRecipe.getChildren().add(recipeListItem);

        }

    }

}